package org.example.flowerswebsite.services.Impl;

import org.apache.catalina.User;
import org.example.flowerswebsite.DTO.UserRegistrationDto;
import org.example.flowerswebsite.Entities.Enums.UserRoleEnum;
import org.example.flowerswebsite.Entities.UserEntity;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Repositories.UserRepository;
import org.example.flowerswebsite.Repositories.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private UserRepository userRepository;

    private UserRoleRepository userRoleRepository;


    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public void register(UserRegistrationDto registrationDTO) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new RuntimeException("passwords.match");
        }

        Optional<UserEntity> byEmail = this.userRepository.findByEmail(registrationDTO.getEmail());

        if (byEmail.isPresent()) {
            throw new RuntimeException("email.used");
        }

        var userRole = userRoleRepository.
                findRoleByName(UserRoleEnum.USER).orElseThrow();

        UserEntity user = new UserEntity(
                registrationDTO.getUsername(),
                passwordEncoder.encode(registrationDTO.getPassword()),
                registrationDTO.getEmail(),
                registrationDTO.getPhoneNumber()
        );

        user.setRoles(List.of(userRole));

        this.userRepository.save(user);
    }

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username + " was not found!"));
    }
}
