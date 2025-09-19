package com.ecommerce.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to E-Commerce Backend API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("documentation", "/swagger-ui.html");
        response.put("health", "/actuator/health");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("Authentication", "/auth/**");
        endpoints.put("Users", "/users/**");
        endpoints.put("Products", "/products/**");
        endpoints.put("Categories", "/categories/**");
        endpoints.put("Brands", "/brands/**");
        endpoints.put("Inventory", "/inventory/**");
        
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }
}