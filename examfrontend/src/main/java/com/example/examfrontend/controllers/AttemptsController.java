package com.example.examfrontend.controllers;

import com.example.examfrontend.dtos.Attempt;
import com.example.examfrontend.dtos.Exam;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttemptsController {
    private List<Attempt> attempts;

    @FXML
    private VBox attemptsVbox;

    public List<Attempt> loadAttempts() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/attempts"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());


            List<Attempt> loadedAttempts = new ArrayList<>();
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                loadedAttempts = mapper.readValue(
                        response.body(),
                        new TypeReference<List<Attempt>>() {
                        }
                );
            }
            return loadedAttempts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadAttemptsBtn(ActionEvent event) throws IOException {
        attempts = loadAttempts();

        for (Attempt attempt : attempts) {
            String examTitle = attempt.getExam().getTitle();

            Integer score = attempt.getTotalScore();

            String submittedTime = convertTimestampToString(attempt.getSubmittedTime());

            // Create UI elements
            HBox attemptBox = new HBox(10);
            attemptBox.setPadding(new Insets(10));
            attemptBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

            Label titleLabel = new Label("Exam: " + examTitle);
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

            Label scoreLabel = new Label("Score: " + (score != null ? score + "%" : "N/A"));

            Label timeSubmittedLabel = new Label(submittedTime);

            scoreLabel.setStyle("-fx-font-size: 13;");

            attemptBox.getChildren().addAll(titleLabel, timeSubmittedLabel, scoreLabel);

            attemptsVbox.getChildren().add(attemptBox);
        }
    }

    public String convertTimestampToString(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();

        return localDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
}
