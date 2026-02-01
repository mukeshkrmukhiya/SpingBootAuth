package com.SpringApp.app.controllers;

import com.SpringApp.app.SecurityConfig.JwtUtil;
import com.SpringApp.app.Services.AuthService;
import com.SpringApp.app.dto.JwtResp;
import com.SpringApp.app.dto.LoginReq;
import com.SpringApp.app.dto.SignUpReqDto;
import com.SpringApp.app.models.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResp> signIn(@RequestBody LoginReq loginreq) {
        JwtResp loginToken = authService.login(loginreq);
        return ResponseEntity.ok(loginToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Validated @RequestBody SignUpReqDto reqDto) {
        try {
            AppUser created = authService.registerNewAppUser(reqDto);
            return ResponseEntity.status(201).body(Map.of("id", created.getId(), "username", created.getUsername(),  "role", created.getRole()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(Map.of("error1", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error2", "registration_failed" + e.getMessage()));
        }
    }

}
