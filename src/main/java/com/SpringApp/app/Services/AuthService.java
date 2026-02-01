package com.SpringApp.app.Services;

import com.SpringApp.app.SecurityConfig.CustomUserDetailsService;
import com.SpringApp.app.SecurityConfig.JwtUtil;
import com.SpringApp.app.dto.JwtResp;
import com.SpringApp.app.dto.LoginReq;
import com.SpringApp.app.dto.SignUpReqDto;
import com.SpringApp.app.models.AppUser;
import com.SpringApp.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public JwtResp login(LoginReq loginreq) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginreq.getUsername(), loginreq.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginreq.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return new JwtResp(token);
    }

    public AppUser registerNewAppUser(SignUpReqDto signUpReqDto) {
        if (userRepository.existsByUsername(signUpReqDto.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }

        // create AppUser entity directly (do NOT use Spring Security's User builder here)
        AppUser user = new AppUser();
        user.setUsername(signUpReqDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpReqDto.getPassword()));
        user.setEmail(signUpReqDto.getEmail());
        user.setFullName(signUpReqDto.getFullName());
        user.setProvider("LOCAL");

        // store role without ROLE_ prefix (recommended). Use "USER" as default.
        String role = (signUpReqDto.getRole() == null || signUpReqDto.getRole().isBlank()) ? "USER" : signUpReqDto.getRole().trim();
        user.setRole(role);

        // save and return (beware: password is persisted encoded)
        return userRepository.save(user);
    }
}
