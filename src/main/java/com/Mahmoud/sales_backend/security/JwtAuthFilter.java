package com.Mahmoud.sales_backend.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Mahmoud.sales_backend.shared.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ================= SKIP AUTH ROUTES =================
        if (path.startsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        //if (path.startsWith("/auth/create")) {
        //    filterChain.doFilter(request, response);
        //    return;
        //}
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String authHeader = request.getHeader("Authorization");

            // ================= NO TOKEN =================
            if (authHeader == null || authHeader.isEmpty()) {
                sendError(response, "Missing Authorization header");
                return;
            }

            // ================= FORMAT CHECK =================
            if (!authHeader.startsWith("Bearer ")) {
                sendError(response, "Invalid Authorization format");
                return;
            }

            String token = authHeader.substring(7);

            // ================= VALID TOKEN =================
            if (!jwtUtil.isTokenValid(token)) {
                sendError(response, "Invalid or expired token");
                return;
            }

            // ================= EXTRACT DATA =================
            Integer userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);

            if (userId == null) {
                sendError(response, "Invalid token payload");
                return;
            }

            // ================= STORE IN REQUEST =================
            request.setAttribute("userId", userId);
            request.setAttribute("role", role);
            // ================= STORE IN CONTEXT =================
            UserContext.setUserId(userId);
            UserContext.setRole(role);

            // ================= CONTINUE =================
            filterChain.doFilter(request, response);

        } catch (Exception e) {
             e.printStackTrace();
            sendError(response, "Authentication failed");
        }
    }

    // ================= CLEAN ERROR RESPONSE =================
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}