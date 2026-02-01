package com.SpringApp.app.Oauth;

import com.SpringApp.app.models.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final OAuthUserProcessor processor;

    @Override
    public OidcUser loadUser(OidcUserRequest request) {

        OidcUser oidcUser = super.loadUser(request);
        String username = oidcUser.getEmail().split("@")[0];

        AppUser user = processor.process(
                username,
                oidcUser.getEmail(),
                oidcUser.getFullName(),
                "GOOGLE",
                oidcUser.getSubject()
        );

        return new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority(user.getRole())), // ✅ from DB
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "email"
        );
    }
}
