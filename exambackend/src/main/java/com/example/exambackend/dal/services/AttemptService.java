package com.example.exambackend.dal.services;

import com.example.exambackend.models.Attempt;

import java.util.List;
import java.util.Optional;

public interface AttemptService {
    Attempt createAttempt(Attempt attempt);
    Attempt updateAttempt(Attempt attemmpt);
    List<Attempt> getAllAttempts();
    List<Attempt> getAllAttemptsByUserId(Long userId);
    Optional<Attempt> getAttemptById(Long id);
    List<Attempt> getAttemptsByExamId(Long examId);

}
