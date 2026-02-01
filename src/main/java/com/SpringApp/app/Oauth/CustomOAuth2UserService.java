package com.SpringApp.app.Oauth;

import com.SpringApp.app.models.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthUserProcessor processor;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        OAuth2User oauthUser = super.loadUser(request);


        String username = oauthUser.getAttribute("login");
        String email = oauthUser.getAttribute("email"); // may be null
        String fullName = Objects.requireNonNullElse(
                oauthUser.getAttribute("name"),
                username
        );
        String provider = "GITHUB";
        String providerId = oauthUser.getAttribute("id").toString();

        // 🔴 Get saved / linked user
        AppUser user = processor.process(
                username,
                email,
                fullName,
                provider,
                providerId
        );

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getRole())), // ✅ from DB
                oauthUser.getAttributes(),
                "id"
        );
    }
}
