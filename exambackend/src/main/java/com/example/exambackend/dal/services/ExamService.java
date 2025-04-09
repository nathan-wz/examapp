package com.example.exambackend.dal.services;

import com.example.exambackend.models.Exam;

import java.util.List;
import java.util.Optional;

public interface ExamService {
    Exam createExam(Exam exam);
    Optional<Exam> getExamById(Long id);
    List<Exam> getAllExams();
    List<Exam> getExamsByUserId(Long userId);
    Exam updateExam(Long id, Exam updatedExam);
    void deleteExam(Long id);
}
