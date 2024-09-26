package com.frigotermica.tema.service;

import java.util.Random;

public class BasicPasswordGenerator {

    public static String getRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "@$!%*?&?";
        String allCharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        StringBuilder password = new StringBuilder();
        Random rnd = new Random();

        password.append(upperCaseLetters.charAt(rnd.nextInt(upperCaseLetters.length())));

        password.append(lowerCaseLetters.charAt(rnd.nextInt(lowerCaseLetters.length())));

        password.append(digits.charAt(rnd.nextInt(digits.length())));

        password.append(specialCharacters.charAt(rnd.nextInt(specialCharacters.length())));

        while (password.length() < 18) {
            password.append(allCharacters.charAt(rnd.nextInt(allCharacters.length())));
        }

        StringBuilder randomPassword = new StringBuilder(password.toString());
        for (int i = 0; i < randomPassword.length(); i++) {
            int randomIndex = rnd.nextInt(randomPassword.length());
            char temp = randomPassword.charAt(i);
            randomPassword.setCharAt(i, randomPassword.charAt(randomIndex));
            randomPassword.setCharAt(randomIndex, temp);
        }

        return randomPassword.toString();
    }

}