package org.example.flowerswebsite;

import org.example.flowerswebsite.Entities.Enums.UserRoleEnum;
import org.example.flowerswebsite.Entities.Role;
import org.example.flowerswebsite.Entities.UserEntity;
import org.example.flowerswebsite.Repositories.UserRepository;
import org.example.flowerswebsite.Repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Init implements CommandLineRunner {
    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public Init(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initUsers();
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            var adminRole = new Role(UserRoleEnum.ADMIN);
            var normalUserRole = new Role(UserRoleEnum.USER);
            userRoleRepository.save(adminRole);
            userRoleRepository.save(normalUserRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initNormalUser();
        }
    }

    private void initAdmin(){
        var adminRole = userRoleRepository.
                findRoleByName(UserRoleEnum.ADMIN).orElseThrow();

        var adminUser = new UserEntity("admin", passwordEncoder.encode(defaultPassword), "admin@example.com", "89001231212");
        adminUser.setRoles(List.of(adminRole));

        userRepository.save(adminUser);
    }

    private void initNormalUser(){
        var userRole = userRoleRepository.
                findRoleByName(UserRoleEnum.USER).orElseThrow();

        var normalUser = new UserEntity("user", passwordEncoder.encode(defaultPassword), "user@example.com", "89001232222");
        normalUser.setRoles(List.of(userRole));

        userRepository.save(normalUser);
    }
}
