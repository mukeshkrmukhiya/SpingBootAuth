package com.SpringApp.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "new-users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔑 Single identity (JWT subject)
    @Column(unique = true, nullable = false)
    private String username;

    // 👤 Display name (not used for auth)
    private String fullName;

    // 📧 Used for account linking
    @Column(unique = true)
    private String email;

    // 🔐 Only for LOCAL users
    private String password;

    // LOCAL, GOOGLE, GITHUB
    @Column(nullable = false)
    private String provider;

    // Provider user id (sub / github id)
    private String providerId;

    // ROLE_USER, ROLE_ADMIN
    @Column(nullable = false)
    private String role;

    private boolean enabled = true;
}
