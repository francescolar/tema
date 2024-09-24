package com.frigotermica.tema.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class CryptoPassword {

    public static String cryptoPassword(String clearPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(clearPassword, salt);
    }
}