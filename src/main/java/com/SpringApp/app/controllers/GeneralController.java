package com.SpringApp.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GeneralController {

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication.toString();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "admin only";
    }

    @GetMapping("/public/")
    public ResponseEntity<String> homePage() {
        return ResponseEntity.ok("This is public Home Page");
    }

    @GetMapping("/msg")
    public ResponseEntity<String> getParam() {
        return ResponseEntity.ok("This is " + "message" + "page");
    }


    @GetMapping("/home")
    public ResponseEntity<?> home(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return ResponseEntity.ok(Map.of(
                    "message", "Welcome!",
                    "user", principal.getAttribute("email"),
                    "name", principal.getAttribute("name")
            ));
        }
        return ResponseEntity.ok("Welcome!");
    }

}
