package com.frigotermica.tema.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frigotermica.tema.models.UserModel;
import com.frigotermica.tema.util.DbUtilityUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.sql.SQLException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        try {
            String errorCode;
            UserModel dbUser = DbUtilityUser.getUserDetails(DbUtilityUser.findByUsername(request.getParameter("username")).getId());
            if (dbUser.getUsername() == null) {
                errorCode = "1";
            } else if (!WebSecurityConfig.checkPasswordWithUsername(dbUser.getPassword(), request.getParameter("password"))) {
                errorCode = "2";
            } else {
                errorCode = "3";
            }
            response.sendRedirect("/login?error=" + errorCode);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
