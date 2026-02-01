package com.SpringApp.app.Oauth;

import com.SpringApp.app.models.AppUser;
import com.SpringApp.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Component
@RequiredArgsConstructor

public class OAuthUserProcessor {

    private final UserRepository userRepository;

    @Transactional
    public AppUser process(
            String username,
            String email,
            String fullName,
            String provider,
            String providerId
    ) {

        // 1️⃣ Try linking by email (best)
        if (email != null) {
            Optional<AppUser> byEmail = userRepository.findByEmail(email);
            if (byEmail.isPresent()) {
                return updateUser(byEmail.get(), fullName, provider, providerId);
            }
        }

        // 2️⃣ Try linking by username (CRITICAL FIX)
        Optional<AppUser> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return updateUser(byUsername.get(), fullName, provider, providerId);
        }

        // 3️⃣ Create new user
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setRole("USER");
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private AppUser updateUser(
            AppUser user,
            String fullName,
            String provider,
            String providerId
    ) {
        if (user.getFullName() == null && fullName != null) {
            user.setFullName(fullName);
        }

        if (user.getProvider() == null || "LOCAL".equals(user.getProvider())) {
            user.setProvider(provider);
            user.setProviderId(providerId);
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        return user;
    }
}

//public class OAuthUserProcessor {
//
//    private final UserRepository userRepository;
//
//    @Transactional
//    public AppUser process(
//            String username,
//            String email,
//            String fullName,
//            String provider,
//            String providerId
//    ) {
//
//        Optional<AppUser> existingByEmail =
//                (email != null) ? userRepository.findByEmail(email) : Optional.empty();
//
//        if (existingByEmail.isPresent()) {
//            AppUser user = existingByEmail.get();
//
//            // update optional fields
//            if (user.getFullName() == null && fullName != null) {
//                user.setFullName(fullName);
//            }
//
//            if (user.getProvider() == null || "LOCAL".equals(user.getProvider())) {
//                user.setProvider(provider);
//                user.setProviderId(providerId);
//            }
//
//            // 🔴 ROLE SAFETY (HERE!)
//            if (user.getRole() == null || user.getRole().isBlank()) {
//                user.setRole("USER");
//            }
//
//            return user;
//        }
//
//        // create new user
//        AppUser user = new AppUser();
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setFullName(fullName);
//        user.setProvider(provider);
//        user.setProviderId(providerId);
//        user.setRole("USER");   // ✅ DEFAULT ROLE
//        user.setEnabled(true);
//
//        return userRepository.save(user);
//    }
//}
