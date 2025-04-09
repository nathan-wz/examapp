package com.example.exambackend.dal.services;

import com.example.exambackend.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsernameAndPassword(String username, String password);
    Optional<User> getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getUsersByRoleId(Long roleId);
    User updateUser(User user);
    void deleteUser(Long id);
}
