package com.frigotermica.tema.service;

import com.frigotermica.tema.models.UserModel;
import com.frigotermica.tema.util.DbUtilityUser;
import com.frigotermica.tema.util.EmailMessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private EmailMessageService emailMessageService;

    public int registerUser(@Valid UserModel user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return 4;
        }
        try {
            boolean userExist = DbUtilityUser.checkUsername(user.getUsername());
            boolean emailExist = DbUtilityUser.checkEmail(user.getEmail());

            if (!userExist) {
                return 1;
            }

            if (!emailExist) {
                return 2;
            }

            String randomPassword = BasicPasswordGenerator.getRandomPassword();
            user.setPassword(randomPassword);
            System.out.println(randomPassword);
            DbUtilityUser.insertPreparedStatement(user);
            emailMessageService.sendWelcomeEmail(user.getEmail(), user.getUsername(), user.getName(), randomPassword);
            return 3;

        } catch (Exception e) {
            logger.error("An error occurred while doing something", e);
            return 4;
        }
    }
}