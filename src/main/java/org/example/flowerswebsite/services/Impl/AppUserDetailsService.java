package org.example.flowerswebsite.services.Impl;

import org.example.flowerswebsite.Entities.UserEntity;
import org.example.flowerswebsite.Repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(u -> new User(
                        u.getUsername(),
                        u.getPassword(),
                        u.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
                                .toList()

                )).orElseThrow(() -> new UsernameNotFoundException(username + " was not found!"));
    }
}
