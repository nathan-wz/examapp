package com.example.exambackend.dal.services;

import com.example.exambackend.models.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Question createQuestion(Question question);
    Question updateQuestion(Long id, Question updatedQuestion);
    Optional<Question> getQuestionById(Long id);
    List<Question> getQuestionsByExamId(Long userId);
    void deleteQuestion(Long id);
}
