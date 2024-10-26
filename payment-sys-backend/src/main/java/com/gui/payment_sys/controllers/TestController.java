package com.gui.payment_sys.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String HelloAdmin() {
        return "Hello, admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String HelloUser() {
        return "Hello, user.";
    }

    @GetMapping("/hello")
    public String Hello() {
        return "Hello";
    }

    @GetMapping("/debug")
    public String debugUser(Authentication authentication) {
        if (authentication == null) {
            return "No authentication found";
        }

        StringBuilder debug = new StringBuilder();
        debug.append("User: ").append(authentication.getName())
                .append("\nAuthorities: ").append(authentication.getAuthorities())
                .append("\nDetails: ").append(authentication.getDetails())
                .append("\nPrincipal: ").append(authentication.getPrincipal())
                .append("\nAuthentication Class: ").append(authentication.getClass().getName());

        return debug.toString();
    }

    @GetMapping("/roles")
    public String checkRoles(Authentication authentication) {
        if (authentication == null) {
            return "Not authenticated";
        }

        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

        return "Has ROLE_USER: " + hasUserRole +
                "\nAll authorities: " + authentication.getAuthorities();
    }
}