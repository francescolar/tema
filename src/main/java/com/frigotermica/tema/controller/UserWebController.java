package com.frigotermica.tema.controller;

import com.frigotermica.tema.models.OperationModel;
import com.frigotermica.tema.models.UserModel;
import com.frigotermica.tema.service.WebSecurityConfig;
import com.frigotermica.tema.util.CryptoPassword;
import com.frigotermica.tema.util.DbUtilityOperation;
import com.frigotermica.tema.util.DbUtilityUser;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
    public String insertNewOperation(@Valid @ModelAttribute OperationModel operation, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "insert-operation";
        }
        try {
            int userId = DbUtilityUser.getAuthenticatedUserId();
            DbUtilityOperation.insertPreparedStatement(operation, userId);
            return "redirect:/view-operations"; 
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
			e.printStackTrace();
            return "redirect:/error";
		}
	}

    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        try {
            int authUserId = DbUtilityUser.getAuthenticatedUserId();
            UserModel user = DbUtilityUser.findById(authUserId);
            model.addAttribute("user", user);
            return "edit-profile";
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "redirect:/homepage";
        }
    }

    @PostMapping("/edit-profile")
    public String editProfile(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("role") boolean isFirstLogin, Model model, RedirectAttributes redirectAttrs) {
        try {
            int authId = DbUtilityUser.getAuthenticatedUserId();
            UserModel dbUser = DbUtilityUser.getUserDetails(authId);
            if (isFirstLogin) {
                if (newPassword != null && !newPassword.isEmpty()) {
                    String salt = BCrypt.gensalt();
                    String newCryptedPassword = CryptoPassword.cryptoPasswordwithSalt(newPassword, salt);
                    DbUtilityUser.updateExistingUser(dbUser, newCryptedPassword, true);
                    WebSecurityConfig.logout();
                    redirectAttrs.addFlashAttribute("afterChangePsw", true);
                    return "redirect:/homepage";
                }
                return "/edit-profile";
            } else if (WebSecurityConfig.checkPassword(dbUser, oldPassword)) {
                if (newPassword != null && !newPassword.isEmpty()) {
                    String salt = BCrypt.gensalt();
                    String newCryptedPassword = CryptoPassword.cryptoPasswordwithSalt(newPassword, salt);
                    DbUtilityUser.updateExistingUser(dbUser, newCryptedPassword, false);
                    WebSecurityConfig.logout();
                    redirectAttrs.addFlashAttribute("afterChangePsw", true);
                } else {
                    DbUtilityUser.updateExistingUser(dbUser, dbUser.getPassword(), false);
                }
                return "redirect:/homepage";
            } else {
                model.addAttribute("pswCheck", true);
                return "edit-profile";
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}
