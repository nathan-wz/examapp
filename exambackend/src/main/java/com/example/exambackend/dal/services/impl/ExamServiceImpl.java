package com.example.exambackend.dal.services.impl;

import com.example.exambackend.dal.repositories.ExamRepository;
import com.example.exambackend.dal.services.ExamService;
import com.example.exambackend.models.Answer;
import com.example.exambackend.models.Exam;
import com.example.exambackend.models.Question;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }


    @Override
    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public List<Exam> getExamsByUserId(Long userId) {
        return examRepository.findByUserId(userId);
    }

    @Override
    public Exam updateExam(Long id, Exam updatedExam) {
        Exam existingExam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        existingExam.setTitle(updatedExam.getTitle());
        existingExam.setStartDateTime(updatedExam.getStartDateTime());
        existingExam.setEndDateTime(updatedExam.getEndDateTime());
        existingExam.setDurationInMinutes(updatedExam.getDurationInMinutes());
        existingExam.setAttemptsAllowed(updatedExam.getAttemptsAllowed());
        existingExam.setPassword(updatedExam.getPassword());

        // Clear and add new questions
        existingExam.getQuestions().clear();

        for (Question q : updatedExam.getQuestions()) {
            q.setExam(existingExam);
            for (Answer a : q.getAnswerChoices()) {
                a.setQuestion(q);
            }
            existingExam.getQuestions().add(q);
        }

        return examRepository.save(existingExam);
    }

    @Override
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

}
