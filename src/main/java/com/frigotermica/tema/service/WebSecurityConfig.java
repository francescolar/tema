package com.frigotermica.tema.service;

import com.frigotermica.tema.models.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT users.username, soft_operation.recon, users.enabled FROM users INNER JOIN soft_operation ON users.username = soft_operation.username WHERE users.username = ?;");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(firstLoginRedirectFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/homepage", "/css/**", "/js/**", "/fragments/**", "/static/**", "/success").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/homepage")
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/homepage")
                        .permitAll());

        return http.build();
    }

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static boolean checkPassword(UserModel user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public static void logout() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        new SecurityContextLogoutHandler().logout(request, null, null);
    }

    @Bean
    public OncePerRequestFilter firstLoginRedirectFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                // Ottieni l'utente autenticato
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                // Permetti l'accesso alle risorse statiche come CSS, JS, immagini, ecc.
                String requestURI = request.getRequestURI();
                if (requestURI.startsWith("/css/") || requestURI.startsWith("/js/") ||
                        requestURI.startsWith("/images/") || requestURI.startsWith("/static/")) {
                    filterChain.doFilter(request, response); // Continua con la normale catena di filtri
                    return;
                }

                // Se l'utente è autenticato
                if (authentication != null && authentication.isAuthenticated()) {
                    // Controlla se l'utente ha il ruolo "ROLE_USER_FIRSTLOGIN"
                    if (authentication.getAuthorities().stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER_FIRSTLOGIN"))) {

                        // Se sta cercando di accedere a una pagina diversa da "/edit-profile", reindirizza
                        if (!requestURI.equals("/edit-profile")) {
                            response.sendRedirect("/edit-profile");
                            return;
                        }
                    }
                }

                // Continua con la normale catena di filtri
                filterChain.doFilter(request, response);
            }
        };
    }
}