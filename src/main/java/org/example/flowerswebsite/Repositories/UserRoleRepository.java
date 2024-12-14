package org.example.flowerswebsite.Repositories;

import org.example.flowerswebsite.Entities.Enums.UserRoleEnum;
import org.example.flowerswebsite.Entities.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends GeneralRepository<Role,String> {
    Optional<Role> findRoleByName(UserRoleEnum role);
}
