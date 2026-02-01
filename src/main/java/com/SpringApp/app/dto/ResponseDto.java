package com.SpringApp.app.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String token;      // JWT
    private String username;
    private String fullName;
    private String role;
}
