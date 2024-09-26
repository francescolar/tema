package com.frigotermica.tema.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frigotermica.tema.models.*;
import com.frigotermica.tema.service.BasicPasswordGenerator;
import com.frigotermica.tema.service.UserService;
import com.frigotermica.tema.util.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class AdminWebController implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(AdminWebController.class);

    @Autowired
    private EmailMessageService messageService;

    //    USER PART  -----------------------------------------------------------------------------------------------------------------------------------------------

    @Autowired
    private UserService userService;

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/admin/view-users").setViewName("admin/view-users");
        registry.addViewController("/admin/view-sites").setViewName("admin/view-sites");
        registry.addViewController("/admin/edit-site").setViewName("admin/edit-site");
        registry.addViewController("/admin/view-operations").setViewName("admin/view-operations");
        registry.addViewController("/admin/edit").setViewName("admin/edit");
    }

    @GetMapping("/admin/view-users")
    public String viewUsers() {
        try {
            return "admin/view-users";
        } catch (Exception e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/create-user")
    public String createUser(@Valid @ModelAttribute("user") UserModel user, BindingResult bindingResult, Model model,
                             RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "error";
        } else {
            int registrazioneEffettuata = userService.registerUser(user, bindingResult);

            switch (registrazioneEffettuata) {
                case 1:
                    model.addAttribute("usernameExist", true);
                    return "admin/view-users";
                case 2:
                    model.addAttribute("emailExist", true);
                    return "admin/view-users";
                case 3:
                    redirectAttrs.addFlashAttribute("userCreated", true);
                    redirectAttrs.addFlashAttribute("user", user);
                    return "redirect:/admin/view-users";
                case 4:
                    return "error";
            }
            return "";
        }
    }

//    SITES PART -----------------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/admin/insert-site")
    public String insertNewSite(@Valid @ModelAttribute SiteModel site, BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "error";
        }
        try {
            DbUtilitySite.insertPreparedStatement(site);
            redirectAttrs.addFlashAttribute("siteSuccess", true);
            return "redirect:/admin/view-sites";
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @GetMapping("/admin/view-sites")
    public String viewSites() {
        try {
            return "admin/view-sites";
        } catch (Exception e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

//    OPERATIONS PART  -----------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/admin/view-operations")
    public String viewUserOperation() {
        try {
            return "admin/view-operations";
        } catch (Exception e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/insert-operation")
    public String insertNewOperation(@Valid @ModelAttribute OperationModel operation, @RequestParam("userId") int userId,
                                     BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "error";
        }
        try {
            DbUtilityOperation.insertPreparedStatement(operation, userId);
            redirectAttrs.addFlashAttribute("opSuccess", true);
            return "redirect:/admin/view-operations";
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

//    SYSTEM PART   -----------------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/admin/insert-system")
    public String insertNewOperation(@Valid @ModelAttribute SystemModel system, BindingResult bindingResult, Model model,
                                     RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "error";
        }
        try {
            DbUtilitySystem.insertPreparedStatement(system);
            redirectAttrs.addFlashAttribute("systemSuccess", true);
            return "redirect:/admin/view-sites";
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

//    GENERAL PART  -----------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/admin/edit")
    public String editGet(@RequestParam("id") Integer id, @RequestParam("tableName") String tableName, Model model) {
        try {
            switch (tableName) {
                case "users":
                    UserModel user = DbUtilityUser.findById(id);
                    model.addAttribute("tableName", "users");
                    model.addAttribute("user", user);
                    return "/admin/edit";
                case "sites":
                    SiteModel site = DbUtilitySite.findById(id);
                    model.addAttribute("tableName", "sites");
                    model.addAttribute("site", site);
                    return "/admin/edit";
                case "systems":
                    SystemModel system = DbUtilitySystem.findById(id);
                    model.addAttribute("tableName", "systems");
                    model.addAttribute("system", system);
                    return "/admin/edit";
                case "operations":
                    FullOperationModel op = DbUtilityFullOperation.findById(id);
                    model.addAttribute("tableName", "operations");
                    model.addAttribute("op", op);
                    return "/admin/edit";
            }
            return "admin/edit";
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/homepage";
        }
    }

    @PostMapping("/admin/edit")
    public String editPost(@RequestParam("tableName") String tableName,
                           @ModelAttribute OperationModel operation,
                           @ModelAttribute UserModel user,
                           @ModelAttribute SiteModel site,
                           @ModelAttribute SystemModel system,
                           @RequestParam(name = "reset-psw", defaultValue = "false") boolean resetPsw,
                           BindingResult bindingResult,
                           Model model) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("error", bindingResult.getAllErrors());
                return "error";
            }
            switch (tableName) {
                case "users":
                    boolean userExist = DbUtilityUser.checkUsername(user.getUsername());
                    boolean emailExist = DbUtilityUser.checkEmail(user.getEmail());
                    UserModel dbUser = DbUtilityUser.getUserDetails(user.getId());
                    if (!user.getUsername().equals(dbUser.getUsername())) {
                        if (!userExist) {
                            model.addAttribute("usernameExist", true);
                            return "redirect:/admin/edit?id=" + user.getId() + "&tableName=users";
                        }
                    }
                    if (!user.getEmail().equals(dbUser.getEmail())) {
                        if (!emailExist) {
                            model.addAttribute("emailExist", true);
                            return "redirect:/admin/edit?id=" + user.getId() + "&tableName=users";
                        }
                    }
                    if (resetPsw) {
                        String randomPassword = BasicPasswordGenerator.getRandomPassword();
                        System.out.println(randomPassword);
                        messageService.sendResetEmail(user.getEmail(), user.getUsername(), user.getName(), randomPassword);
                        user.setPassword(CryptoPassword.cryptoPassword(randomPassword));
                    } else {
                        user.setPassword(dbUser.getPassword());
                    }
                    DbUtility.saveEditedLog("users", user.getId());
                    DbUtilityUser.adminUpdate(user, dbUser);
                    return "redirect:/admin/view-users";
                case "sites":
                    DbUtility.saveEditedLog("sites", site.getId());
                    DbUtilitySite.updatePreparedStatement(site);
                    return "redirect:/admin/view-sites";
                case "systems":
                    DbUtility.saveEditedLog("systems", system.getId());
                    DbUtilitySystem.updatePreparedStatement(system);
                    return "redirect:/admin/view-sites";
                case "operations":
                    DbUtility.saveEditedLog("operations", operation.getId());
                    DbUtilityOperation.update(operation);
                    return "redirect:/admin/view-operations";
            }
            return "admin/edit";
        } catch (JsonProcessingException | ClassNotFoundException | MessagingException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/homepage";
        }
    }

    @PostMapping("/admin/delete")
    public String delete(@RequestParam("id") Integer id, @RequestParam("tableName") String tableName) {
        try {
            DbUtility.softDeletePreparedStatement(id, tableName);
            switch (tableName) {
                case "users":
                    return "redirect:/admin/view-users";
                case "sites":
                case "systems":
                    return "redirect:/admin/view-sites";
                case "operations":
                    return "redirect:/admin/view-operations";
            }
            return "error";
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/toggle")
    public String toggle(@RequestParam("id") Integer userId, @RequestParam("tableName") String tableName) {
        try {
            DbUtility.toggleEnable(tableName, userId);
            switch (tableName) {
                case "users":
                    return "redirect:/admin/view-users";
                case "operations":
                    return "redirect:/admin/view-operations";
                case "sites":
                case "systems":
                    return "redirect:/admin/view-sites";
            }
            return "redirect:/error";
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return "redirect:/error";
        }
    }
}