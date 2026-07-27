package com.neerpilot.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Protects ESP32-facing endpoints with a shared secret header instead of JWT,
 * since the microcontroller does not perform interactive login.
 * ESP32 must send:  X-API-KEY: <device.api.key from application.properties>
 */
@Component
public class DeviceApiKeyFilter extends OncePerRequestFilter {

    @Value("${device.api.key}")
    private String deviceApiKey;

    private static final List<String> DEVICE_PATHS = List.of(
            "/api/tank/data",
            "/api/home/data",
            "/api/home/command"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (!DEVICE_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedKey = request.getHeader("X-API-KEY");

        if (providedKey == null || !providedKey.equals(deviceApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Invalid or missing X-API-KEY device header\"}");
            return;
        }

        // Grant a lightweight DEVICE authority so SecurityConfig's authorizeHttpRequests passes
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("esp32-device", null,
                        List.of(new SimpleGrantedAuthority("ROLE_DEVICE")));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
