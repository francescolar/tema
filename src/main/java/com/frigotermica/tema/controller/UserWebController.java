package com.frigotermica.tema.controller;

import com.frigotermica.tema.models.OperationModel;
import com.frigotermica.tema.models.UserModel;
import com.frigotermica.tema.service.WebSecurityConfig;
import com.frigotermica.tema.util.CryptoPassword;
import com.frigotermica.tema.util.DbUtilityOperation;
import com.frigotermica.tema.util.DbUtilityUser;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

@Controller
public class UserWebController implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(UserWebController.class);

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/insert-operation").setViewName("insert-operation");
        registry.addViewController("/view-operations").setViewName("view-operations");
        registry.addViewController("/edit-profile").setViewName("edit-profile");
    }

    @GetMapping("/insert-operation")
    public String insertNewOperation(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("operation", new OperationModel());
        return "insert-operation";
    }

    @PostMapping("/insert-operation")
    public String insertNewOperation(@Valid @ModelAttribute OperationModel operation, BindingResult bindingResult, Model model,
                                     RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "error";
        }
        try {
            int userId = DbUtilityUser.getAuthenticatedUserId();
            DbUtilityOperation.insertPreparedStatement(operation, userId);
            redirectAttrs.addFlashAttribute("success", true);
            return "redirect:/view-operations";
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @GetMapping("/view-operations")
    public String viewUserOperation(Model model) {
        try {
            int userId = DbUtilityUser.getAuthenticatedUserId();
            List<OperationModel> ownedOperationList = DbUtilityOperation.findByOwned(userId);
            model.addAttribute("ownedOps", ownedOperationList);
            model.addAttribute("userId", userId);
            return "view-operations";
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        try {
            return "edit-profile";
        } catch (Exception e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/homepage";
        }
    }

    @PostMapping("/edit-profile")
    public String editProfile(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("role") boolean isFirstLogin, Model model, RedirectAttributes redirectAttrs) {
        try {
            int authId = DbUtilityUser.getAuthenticatedUserId();
            UserModel dbUser = DbUtilityUser.getUserDetails(authId);
            boolean isPasswordChangeValid = newPassword != null && !newPassword.isEmpty();

            if (isFirstLogin || (WebSecurityConfig.checkPassword(dbUser, oldPassword) && isPasswordChangeValid)) {
                String newCryptedPassword = dbUser.getPassword();
                if (isPasswordChangeValid) {
                    newCryptedPassword = CryptoPassword.cryptoPassword(newPassword);
                }

                DbUtilityUser.updateExistingUser(dbUser.getUsername(), newCryptedPassword, isFirstLogin);
                WebSecurityConfig.logout();
                redirectAttrs.addFlashAttribute("afterChangePsw", true);
                return "redirect:/homepage";
            }

            model.addAttribute("pswCheck", true);
            return "/edit-profile";
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }
}