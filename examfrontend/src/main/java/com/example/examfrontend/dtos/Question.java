package com.example.examfrontend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Question {
    // Attributes

    private Long id;

    private String question;

    private String topic;

    private Integer marks;


    // Relationships

    @JsonIgnore
    private Exam exam;

    private List<Answer> answerChoices = new ArrayList<>();
    ;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<Answer> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(List<Answer> answerChoices) {
        this.answerChoices = answerChoices;
    }
}
