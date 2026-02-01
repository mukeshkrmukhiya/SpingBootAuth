package com.SpringApp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReqDto {

    private String username;   // required
    private String password;   // required

    private String email;      // optional but recommended
    private String fullName;   // optional
    private String role;
}

