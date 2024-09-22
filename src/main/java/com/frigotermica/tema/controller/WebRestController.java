package com.frigotermica.tema.controller;

import com.frigotermica.tema.models.FullOperationModel;
import com.frigotermica.tema.models.SiteModel;
import com.frigotermica.tema.models.SystemModel;
import com.frigotermica.tema.models.UserModel;
import com.frigotermica.tema.util.DbUtilityFullOperation;
import com.frigotermica.tema.util.DbUtilitySite;
import com.frigotermica.tema.util.DbUtilitySystem;
import com.frigotermica.tema.util.DbUtilityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WebRestController {

    private static final Logger logger = LoggerFactory.getLogger(WebRestController.class);

    @Value("${API_GOOGLE}")
    private String apiKey;

    @GetMapping("/users/all")
    public List<UserModel> getAllUser() throws SQLException {
        try {
            return DbUtilityUser.getAll();
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/users/all-no-admin")
    public List<UserModel> getAllUserNoAdmin() throws SQLException {
        try {
            return DbUtilityUser.getAllNoAdmin();
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/operations/all")
    public List<FullOperationModel> getAllOps() throws SQLException {
        try {
            return DbUtilityFullOperation.getAll();
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/operations/owned")
    public List<FullOperationModel> getOwnedOps() throws SQLException {
        try {
            int authId = DbUtilityUser.getAuthenticatedUserId();
            return DbUtilityFullOperation.findByOwned(authId);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/sites/all")
    public List<SiteModel> getAllSites() throws SQLException {
        try {
            return DbUtilitySite.getAll();
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/sites/all-enabled")
    public List<SiteModel> getAllEnabledSites() throws SQLException {
        try {
            return DbUtilitySite.getAllEnabled();
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/systems/all")
    public List<SystemModel> getAllSystems() throws SQLException {
        try {
            return DbUtilitySystem.getAll();
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/systems/all-enabled")
    public List<SystemModel> getAllEnabledSystems() throws SQLException {
        try {
            return DbUtilitySystem.getAllEnabled();
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/systems")
    public List<SystemModel> getSystemsBySiteId(@RequestParam("site_id") int siteId) throws SQLException {
        try {
            return DbUtilitySystem.findBySiteId(siteId);
        } catch (SQLException e) {
            logger.error("An error occurred while doing something", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/google-maps-key")
    public String getGoogleMapsApiKey() {
        return apiKey;
    }

}