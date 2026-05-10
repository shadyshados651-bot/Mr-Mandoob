package com.Mahmoud.sales_backend.module.Mandoob;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import com.Mahmoud.sales_backend.model.User;

@Repository
public class MandoobRepository extends BaseRepository {

    public MandoobRepository(Connection conn) {
        super(conn);
    }

    // ================= Get All =================
    public List<User> getAll() {
        String sql = "SELECT id, name, email, role, status, created_at FROM users WHERE role = 'mandoob'";
        List<Map<String, Object>> result = query(sql);

        List<User> mandoobs = new ArrayList<>();
        for (Map<String, Object> row : result) {
            mandoobs.add(mapToUser(row));
        }
        return mandoobs;
    }

    // ================= Find By Email =================
    public User findByEmail(String email) {
        List<Map<String, Object>> result = query(
                "SELECT * FROM users WHERE email = ?", email);

        if (result.isEmpty())
            return null;
        return mapToUser(result.get(0));
    }

    // ================= Create =================
    public boolean create(User user) {
        try {
            Map<String, Object> data = Map.of(
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "phone", user.getPhone(),
                    "password", user.getPasswordHash(),
                    "role", user.getRole());
            return insert("users", data) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
public User getById(int id) {
    List<Map<String, Object>> result = query(
        "SELECT id, name, email, phone, role, status, created_at FROM users WHERE id = ? AND role = 'mandoob'", id);

    if (result.isEmpty()) return null;
    return mapToUser(result.get(0));
}

    // ================= Toggle Status =================
public boolean toggleStatus(int id) {
    try {
        return update("users", Map.of("status", "ABS(status - 1)"), "id", id) > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    // ================= Mapper =================
    private User mapToUser(Map<String, Object> row) {
        User user = new User();

        user.setId(((Number) row.get("id")).intValue());
        user.setName((String) row.get("name"));
        user.setEmail((String) row.get("email"));
        user.setRole((String) row.get("role"));
        user.setCreatedAt((Date) row.get("created_at"));

        // ✅ status ممكنييجي Boolean أو Number حسب نوع الـ column
        Object status = row.get("status");
        if (status instanceof Boolean) {
            user.setStatus((Boolean) status ? 1 : 0);
        } else if (status instanceof Number) {
            user.setStatus(((Number) status).intValue());
        }

        Object passwordHash = row.get("password");
        if (passwordHash != null)
            user.setPasswordHash((String) passwordHash);

        return user;
    }

}