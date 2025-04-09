package com.example.exambackend.dal.repositories;

import com.example.exambackend.models.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByUserId(Long userId);
    List<Attempt> findByExamId(Long examId);
}
