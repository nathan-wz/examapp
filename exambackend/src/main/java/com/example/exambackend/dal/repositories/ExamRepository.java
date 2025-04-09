package com.example.exambackend.dal.repositories;

import com.example.exambackend.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByUserId(Long userId);
}
