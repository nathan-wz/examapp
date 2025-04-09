package com.example.exambackend.dal.services.impl;


import com.example.exambackend.dal.repositories.RoleRepository;
import com.example.exambackend.dal.services.RoleService;
import com.example.exambackend.models.Role;
import com.example.exambackend.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(Role role) {
        for (User user : role.getUsers()) {
            user.setRole(role);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role createRole(Role role){
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

}
