package com.Mahmoud.sales_backend.security;
public class UserContext {

    private static final ThreadLocal<Integer> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> role = new ThreadLocal<>();

    public static void setUserId(Integer id) {
        userId.set(id);
    }

    public static Integer getUserId() {
        return userId.get();
    }

    public static void setRole(String r) {
        role.set(r);
    }

    public static String getRole() {
        return role.get();
    }

    public static void clear() {
        userId.remove();
        role.remove();
    }
}