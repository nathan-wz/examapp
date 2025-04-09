package com.example.exambackend.dal.services;

import com.example.exambackend.models.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerService {
    Answer createAnswer(Answer answer);
    Optional<Answer> getAnswerById(Long id);
    List<Answer> getAnswersByQuestionId(Long questionId);
    void deleteAnswer(Long id);
}
