package com.example.exambackend.dal.services.impl;

import com.example.exambackend.dal.repositories.QuestionRepository;
import com.example.exambackend.dal.services.QuestionService;
import com.example.exambackend.models.Answer;
import com.example.exambackend.models.Question;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionServiceImpl (QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question createQuestion(Question question){
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        existingQuestion.setQuestion(updatedQuestion.getQuestion());
        existingQuestion.setTopic(updatedQuestion.getTopic());
        existingQuestion.setMarks(updatedQuestion.getMarks());

        // clear old answers
        existingQuestion.getAnswerChoices().clear();

        return questionRepository.save(existingQuestion);
    }


    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public List<Question> getQuestionsByExamId(Long examId) {
        return questionRepository.findByExamId(examId);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

}
