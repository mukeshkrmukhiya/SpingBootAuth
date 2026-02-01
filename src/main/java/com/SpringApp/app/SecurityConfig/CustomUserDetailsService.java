package com.SpringApp.app.SecurityConfig;

import com.SpringApp.app.models.AppUser;
import com.SpringApp.app.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        AppUser user = userRepository.findByUsername(username).
                orElseThrow(()->new UsernameNotFoundException("User not Found"));


        return User.
                withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_",""))
                .build();
    }

}
