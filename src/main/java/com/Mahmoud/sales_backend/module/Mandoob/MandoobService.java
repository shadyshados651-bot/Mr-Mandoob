package com.Mahmoud.sales_backend.module.Mandoob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.Mahmoud.sales_backend.model.User;
import com.Mahmoud.sales_backend.security.PasswordUtil;
import com.Mahmoud.sales_backend.security.UserContext;
import com.Mahmoud.sales_backend.shared.ApiResponse;
import com.Mahmoud.sales_backend.shared.PhoneValidator;

@Service
public class MandoobService {

    private final MandoobRepository mandoobRepository;

    public MandoobService(MandoobRepository mandoobRepository) {
        this.mandoobRepository = mandoobRepository;
    }

    public ApiResponse getAll() {
        String role = UserContext.getRole();
        if (!"admin".equals(role)) {
            return ApiResponse.error("not allowed");
        }
        List<User> mandoobs = mandoobRepository.getAll();

        Map<String, Object> response = new HashMap<>();
        response.put("total_mandoobs", mandoobs.size());
        response.put("mandoobs",       mandoobs);

        return ApiResponse.success(response);
    }
    public ApiResponse get(int id) {
        String role = UserContext.getRole();
        if (!"admin".equals(role)) {
            return ApiResponse.error("not allowed");
        }
        User mandoobs = mandoobRepository.getById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mandoob",       mandoobs);

        return ApiResponse.success(response);
    }
public ApiResponse create(User user) {
        String role = UserContext.getRole();
        if (!"admin".equals(role)) {
            return ApiResponse.error("Not allowed");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
        return ApiResponse.error("Name is required");
        }
        // ================= EMAIL VALIDATION =================
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ApiResponse.error("Email is required");
        }

        if (user.getEmail().length() > 50) {
            return ApiResponse.error("Email must not exceed 50 characters");
        }

        if (!user.getEmail().endsWith("@gmail.com")) {
            return ApiResponse.error("Email must end with @gmail.com");
        }

        if (user.getEmail().chars().filter(c -> c == '@').count() != 1) {
            return ApiResponse.error("Email must contain only one @");
        }

        String localPart = user.getEmail().split("@")[0];

        if (!localPart.matches("[a-z0-9]+")) {
            return ApiResponse.error("Email must contain only lowercase letters and numbers");
        }

        if (!PhoneValidator.isValidEgyptPhone(user.getPhone())) {
            return ApiResponse.error("Invalid Egyptian phone number");
        }

        // ================= PASSWORD VALIDATION =================
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ApiResponse.error("Password is required");
        }

        String password = user.getPassword().trim();
        user.setPassword(password);

        if (!password.matches("^[a-zA-Z0-9]+$")) {
            return ApiResponse.error("Password must contain only English letters and numbers");
        }

        // ================= CHECK USER EXISTS =================
        User user1 = mandoobRepository.findByEmail(user.getEmail());

        if (user1 != null) {
            return ApiResponse.error("User found before");
        }

        // ================= HASH PASSWORD =================
        String hashedPassword = PasswordUtil.hashPassword(password);
        user.setPasswordHash(hashedPassword);

        // ================= DEFAULT ROLE =================
        user.setRole("mandoob");

        // ================= SAVE USER =================
        boolean inserted = mandoobRepository.create(user);

        if(!inserted) {
            return ApiResponse.error("Create failed");
        }

        // ================= GENERATE TOKEN =================
        //String token = authRepository.generateToken(user);

        Map<String, Object> data = new HashMap<>();
        //data.put("token", token);
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());

        return ApiResponse.success(data);
    }
// ================= Toggle Status =================
public ApiResponse toggleStatus(int id) {
    if (!"admin".equals(UserContext.getRole()))
        return ApiResponse.error("Not allowed");

    if (id <= 0)
        return ApiResponse.error("Invalid ID");

    User mandoob = mandoobRepository.getById(id);
    if (mandoob == null)
        return ApiResponse.error("Mandoob not found");

    boolean done = mandoobRepository.toggleStatus(id);
    if (!done)
        return ApiResponse.error("Failed to toggle status");

    int newStatus = mandoob.getStatus() == 1 ? 0 : 1;
    String message = newStatus == 1 ? "Account activated" : "Account deactivated";

    return ApiResponse.success(message);
}
}