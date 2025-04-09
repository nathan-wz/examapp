package com.example.exambackend.dal.repositories;

import java.util.List;
import java.util.Optional;

import com.example.exambackend.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"role"})
    List<User> findAll();

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    List<User> findByRoleId(Long roleId);
    boolean existsByIdNumber(String idNumber);
}
