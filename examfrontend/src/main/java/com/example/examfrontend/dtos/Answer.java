package com.example.examfrontend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Answer {
    // Attributes

    private Long id;

    private String answerText;

    private Boolean isCorrect;


    // Relationships

    @JsonIgnore
    private Question question;

    @JsonIgnore
    private Attempt attempt;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
