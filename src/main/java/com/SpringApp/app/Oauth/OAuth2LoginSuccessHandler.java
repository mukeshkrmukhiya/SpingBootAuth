package com.SpringApp.app.Oauth;

import com.SpringApp.app.SecurityConfig.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof OidcUser oidcUser) {
            username = oidcUser.getEmail();          // GOOGLE
        } else if (principal instanceof OAuth2User oauth2User) {
            username = oauth2User.getAttribute("login") != null
                    ? oauth2User.getAttribute("login") // GITHUB
                    : oauth2User.getAttribute("email"); // FACEBOOK
        } else {
            throw new IllegalStateException("Unknown principal");
        }

        UserDetails userDetails = User
                .withUsername(username)   // 🔴 JWT SUBJECT
                .password("")
                .authorities(authentication.getAuthorities())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    }
}
