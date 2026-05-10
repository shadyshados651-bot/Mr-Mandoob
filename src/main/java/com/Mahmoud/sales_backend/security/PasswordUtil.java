package com.Mahmoud.sales_backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    // تشفير الباسورد
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    // التحقق من الباسورد
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}