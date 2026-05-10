package com.Mahmoud.sales_backend.module.AdminAuth;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import com.Mahmoud.sales_backend.model.User;
import com.Mahmoud.sales_backend.shared.JwtUtil;

@Repository
public class AdminAuthRepository extends BaseRepository implements IAdminAuthRepository {

    private final JwtUtil jwtUtil;

    public AdminAuthRepository(Connection conn, JwtUtil jwtUtil) {
        super(conn);
        this.jwtUtil = jwtUtil;
    }

    // ================= FIND USER =================
    public User findByEmail(String email) {

        List<Map<String, Object>> result = query("SELECT * FROM users WHERE email = ?", email);

        if (result.isEmpty())
            return null;

        Map<String, Object> row = result.get(0);

        User user = new User();
        user.setId((int) row.get("id"));
        user.setName((String) row.get("name"));
        user.setEmail((String) row.get("email"));
        user.setPhone((String) row.get("phone"));
        user.setPasswordHash((String) row.get("password"));
        user.setRole((String) row.get("role"));

        return user;
    }

    
    // ================= TOKEN =================
    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getId(), user.getRole());
    }
}