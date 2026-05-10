package com.Mahmoud.sales_backend.shared;

import java.util.regex.Pattern;

public class PhoneValidator {

    private static final Pattern EGYPT_PHONE =
            Pattern.compile("^(01[0-25]\\d{8})$");

    private static final Pattern EGYPT_PHONE_INTERNATIONAL =
            Pattern.compile("^(\\+20)(1[0-25]\\d{8})$");

    public static boolean isValidEgyptPhone(String phone) {
        if (phone == null) return false;

        phone = phone.trim();

        return EGYPT_PHONE.matcher(phone).matches()
                || EGYPT_PHONE_INTERNATIONAL.matcher(phone).matches();
    }
}