package com.frigotermica.tema.controller;

import com.frigotermica.tema.models.*;
import com.frigotermica.tema.service.UserService;
import com.frigotermica.tema.util.*;
import jakarta.validation.Valid;
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

import java.sql.SQLException;
import java.util.List;

@Controller
public class AdminWebController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/admin/view-users").setViewName("admin/view-users");
        registry.addViewController("/admin/view-sites").setViewName("admin/view-sites");
        registry.addViewController("/admin/edit-site").setViewName("admin/edit-site");
        registry.addViewController("/admin/view-operations").setViewName("admin/view-operations");
        registry.addViewController("/admin/edit").setViewName("admin/edit");
    }

//    USER PART

    @Autowired
    private UserService userService;

    @GetMapping("/admin/view-users")
    public String viewUsers(Model model) {
        try {
            return "admin/view-users";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/create-user")
    public String createUser(@Valid @ModelAttribute("user") UserModel user, BindingResult bindingResult, Model model) {
        int registrazioneEffettuata = userService.registerUser(user, bindingResult);

        switch (registrazioneEffettuata) {
            case 1:
                model.addAttribute("usernameExist", true);
                return "admin/view-users";
            case 2:
                model.addAttribute("emailExist", true);
                return "admin/view-users";
            case 3:
                model.addAttribute("afterRegistration", true); //non serve
                model.addAttribute("user", user);
                return "admin/view-users";
            case 4:
                return "error";
        }
        return "";
    }

//    SITES PART

    @PostMapping("/admin/insert-site")
    public String insertNewSite(@Valid @ModelAttribute SiteModel site, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/error";
        }
        try {
            //int user_id = DbUtilityUser.getAuthenticatedUserId();
            DbUtilitySite.insertPreparedStatement(site);
            return "redirect:/admin/view-sites";
        } catch (/*ClassNotFoundException | */SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/admin/view-sites")
    public String viewSites(Model model) {
        try {
            List<SiteModel> siteList = DbUtilitySite.getAll();
            model.addAttribute("sites", siteList);
            return "admin/view-sites";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

//    OPERATIONS PART

    @GetMapping("/admin/view-operations")
    public String viewUserOperation(Model model) {
        try {
            List<FullOperationModel> operationList = DbUtilityFullOperation.getAll();
            model.addAttribute("ops", operationList);
            return "admin/view-operations";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/insert-operation")
    public String insertNewOperation(@Valid @ModelAttribute OperationModel operation, @RequestParam("userId") int userId, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/insert-operation";
        }
        try {
            DbUtilityOperation.insertPreparedStatement(operation, userId);
            return "redirect:/admin/view-operations";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

//    SYSTEM PART

    @PostMapping("/admin/insert-system")
    public String insertNewOperation(@Valid @ModelAttribute SystemModel system, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/error";
        }
        try {
            //int user_id = DbUtilityUser.getAuthenticatedUserId();
            DbUtilitySystem.insertPreparedStatement(system);
            return "redirect:/admin/view-sites";
        } catch (/*ClassNotFoundException | */SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

//    GENERAL PART

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
            e.printStackTrace();
            return "redirect:/homepage";
        }
    }

    @PostMapping("/admin/edit")
    public String editPost(@RequestParam("tableName") String tableName,
                           @ModelAttribute OperationModel operation,
                           @ModelAttribute UserModel user,
                           @ModelAttribute SiteModel site,
                           @ModelAttribute SystemModel system,
                           BindingResult result,
                           Model model) {
        try {
            switch (tableName) {
                case "users":
                    if (result.hasErrors()) {
                        model.addAttribute("tableName", "users");
                        return "/admin/edit";
                    }
                    DbUtilityOperation.update(operation);
                    return "redirect:/admin/view-users";
                case "sites":
                    if (result.hasErrors()) {
                        model.addAttribute("tableName", "sites");
                        return "/admin/edit";
                    }
                    DbUtilitySite.updatePreparedStatement(site);
                    return "redirect:/admin/view-sites";
                case "systems":
                    if (result.hasErrors()) {
                        model.addAttribute("tableName", "systems");
                        return "/admin/edit";
                    }
                    DbUtilitySystem.updatePreparedStatement(system);
                    return "redirect:/admin/view-sites";
                case "operations":
                    if (result.hasErrors()) {
                        model.addAttribute("tableName", "operations");
                        return "/admin/edit";
                    }
                    DbUtilityOperation.update(operation);
                    return "redirect:/admin/view-operations";
            }
            return "admin/edit";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/homepage";
        }
    }

    @PostMapping("/admin/delete")
    public String delete(@RequestParam("id") Integer id, @RequestParam("tableName") String tableName){
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
            e.printStackTrace();
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
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}
