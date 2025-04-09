package com.example.examfrontend.controllers;


import com.example.examfrontend.dtos.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class MainController {

    private User currentUser;
    private final String viewsPath = "/com/example/examfrontend/views/";

    @FXML
    private AnchorPane contentPane;


    // Roles
    public void switchToRolesView(ActionEvent event) throws IOException {
        switchToView("Roles.fxml");
    }

    // Users
    public void switchToUsersView(ActionEvent event) throws IOException {
        switchToView("Users.fxml");
    }

    // Exam Creator
    public void switchToExamCreatorView(ActionEvent event) throws IOException {
        switchToView("ExamCreator.fxml");
    }

    // Exams
    public void switchToExamsToDoView(ActionEvent event) throws IOException {
        switchToView("ExamsToDo.fxml");
    }

    // Attempts
    public void switchToAttemptsView(ActionEvent event) throws IOException {
        switchToView("Attempts.fxml");
    }

    public void switchToView(String path) throws IOException {
        URL fxmlUrl = getClass().getResource(viewsPath + path);
        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML not found: " + path);
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent view = loader.load();
        contentPane.getChildren().setAll(view);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
