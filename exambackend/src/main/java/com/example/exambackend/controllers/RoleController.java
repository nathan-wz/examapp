package com.example.exambackend.controllers;

import com.example.exambackend.dal.services.RoleService;
import com.example.exambackend.models.Role;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController (RoleService roleService) {
        this.roleService = roleService;
    }

    // Create Role
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        System.out.println("Creating role: " + role.getName());
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(createdRole);
    }

    // Get All roles
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Get Role by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> roleOpt = roleService.getRoleById(id);
        return roleOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update role
    @PutMapping(path = "/{id}")
    public ResponseEntity<Role> updateRoleById(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);

        try {
            return ResponseEntity.ok(roleService.updateRole(role));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // Delete role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
