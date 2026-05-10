package com.Mahmoud.sales_backend.module.AdminAuth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mahmoud.sales_backend.model.User;
import com.Mahmoud.sales_backend.shared.ApiResponse;
@RestController
@RequestMapping("/auth")
public class AdminAuthController {

    private final AdminAuthService authService;

    public AdminAuthController(AdminAuthService authService) {
        this.authService = authService;
    }
    // localhost:8080/auth/login
    @PostMapping("/login")
    public ApiResponse login(@RequestBody User user) {
        return authService.login(user.getEmail(), user.getPassword());
    }
    

    @GetMapping("/verify-token")
    public ApiResponse VerifyToken() {
        return ApiResponse.success("true");
    }
}
