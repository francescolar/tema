package com.frigotermica.tema.controller;

import com.frigotermica.tema.models.UserModel;
import com.frigotermica.tema.util.DbUtilityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.SQLException;

@Controller
public class WebController implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("homepage");
        registry.addViewController("/homepage").setViewName("homepage");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
    }

    @GetMapping("/")
    public String showHome(Model model) {
        try {
            int authUserId = DbUtilityUser.getAuthenticatedUserId();
            UserModel user = DbUtilityUser.findById(authUserId);
            model.addAttribute("user", user);
            return "homepage";
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @GetMapping("/homepage")
    public String showHome2(Model model) {
        try {
            int authUserId = DbUtilityUser.getAuthenticatedUserId();
            UserModel user = DbUtilityUser.findById(authUserId);
            model.addAttribute("user", user);
            return "homepage";
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        try {
            if (DbUtilityUser.getAuthenticatedUserId() != -1) {
                return "redirect:/homepage";
            }
            if (error != null) {
                model.addAttribute("errorMessage", "Username o password non validi.");
                model.addAttribute("afterRegistration", false);
            }
            return "login";
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}