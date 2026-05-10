package com.Mahmoud.sales_backend.module.AdminAuth;

import com.Mahmoud.sales_backend.model.User;

public interface IAdminAuthRepository {
    User findByEmail(String email);
    String generateToken(User user);
}
