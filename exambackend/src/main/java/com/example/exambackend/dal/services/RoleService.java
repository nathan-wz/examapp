package com.example.exambackend.dal.services;

import com.example.exambackend.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(Role role);
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    void deleteRole(Long id);
}
