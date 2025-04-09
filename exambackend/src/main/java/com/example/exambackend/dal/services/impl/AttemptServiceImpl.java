package com.example.exambackend.dal.services.impl;

import com.example.exambackend.dal.repositories.AnswerRepository;
import com.example.exambackend.dal.repositories.AttemptRepository;
import com.example.exambackend.dal.repositories.ExamRepository;
import com.example.exambackend.dal.services.AttemptService;
import com.example.exambackend.models.Answer;
import com.example.exambackend.models.Attempt;
import com.example.exambackend.models.Exam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttemptServiceImpl implements AttemptService {
    private final AttemptRepository attemptRepository;
    private final ExamRepository examRepository;
    private final AnswerRepository answerRepository;

    public AttemptServiceImpl(AttemptRepository attemptRepository, ExamRepository examRepository, AnswerRepository answerRepository) {
        this.attemptRepository = attemptRepository;
        this.examRepository = examRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public List<Attempt> getAllAttempts() {

        return attemptRepository.findAll();
    }

    @Override
    public Attempt updateAttempt(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    @Override
    public List<Attempt> getAllAttemptsByUserId(Long userId) {
        return attemptRepository.findByUserId(userId);
    }

    @Override
    public Attempt createAttempt(Attempt attempt){
//        Long existingExamId = attempt.getExam().getId();
//        Exam existingExam = examRepository.findById(existingExamId)
//                .orElseThrow(() -> new RuntimeException("Exam not found"));
//
//        existingExam.getAttempts().add(attempt);
//

        return attemptRepository.save(attempt);
    }

    @Override
    public Optional<Attempt> getAttemptById(Long id){
        return attemptRepository.findById(id);
    }

    @Override
    public List<Attempt> getAttemptsByExamId(Long examId) {
        return attemptRepository.findByExamId(examId);
    }

}
