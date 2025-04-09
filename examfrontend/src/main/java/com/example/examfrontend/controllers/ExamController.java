package com.example.examfrontend.controllers;

import com.example.examfrontend.dtos.Answer;
import com.example.examfrontend.dtos.Attempt;
import com.example.examfrontend.dtos.Exam;
import com.example.examfrontend.dtos.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExamController {
    private Exam selectedExam;
    private List<Exam> exams;
    private Attempt currentAttempt;
    private final String viewsPath = "/com/example/examfrontend/views/";
    private Timeline countdownTimeline;

    private List<ToggleGroup> answerGroups = new ArrayList<>();

    @FXML
    private VBox examsToDoVbox;

    @FXML
    private VBox currentExamVbox;

    @FXML
    private VBox timerVbox;


    // http requests
    public void addAttempt(Attempt newAttempt) {
        try {
            // Send Post to backend
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(newAttempt);
            System.out.println(json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/attempts"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200 || response.statusCode() == 201) {
                // can be used later
                Attempt createdAttempt = mapper.readValue(response.body(), Attempt.class);
            } else {
                System.err.println("Server returned error: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while sending request: " + e.getMessage());
        }
    }

    public void addExam(Exam newExam) {
        try {
            // Send Post to backend
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(newExam);
            System.out.println(json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/exams"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200 || response.statusCode() == 201) {
                // can be used later
                Exam createdExam = mapper.readValue(response.body(), Exam.class);
            } else {
                System.err.println("Server returned error: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while sending request: " + e.getMessage());
        }
    }

    public void deleteExam(Exam exam) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/exams/" + exam.getId()))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                System.out.println("User deleted successfully");
            } else {
                System.out.println("Failed to delete user. Status code: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQuestion(Question question) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/exams/questions/" + question.getId()))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                System.out.println("question deleted successfully");
            } else {
                System.out.println("Failed to delete question. Status code: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateExam(Exam updatedExam) {
        // Send the updated exam to backend
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(updatedExam);
            System.out.println("Json: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/exams/" + updatedExam.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());

            if (response.statusCode() == 200) {
                System.out.println("User updated successfully");
            } else {
                System.out.println("Failed to update user. Status Code: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Exam> loadExams() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/exams"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());


            List<Exam> loadedExams = new ArrayList<>();
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                loadedExams = mapper.readValue(
                        response.body(),
                        new TypeReference<List<Exam>>() {
                        }
                );
            }
            return loadedExams;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // associated buttons

    public void loadExamsToDoBtn(ActionEvent event) throws IOException {
        // clear the display list
        examsToDoVbox.getChildren().clear();

        // load the exams list
        exams = loadExams();

        for (Exam exam : exams) {
            LocalDateTime startDateTime = exam.getStartDateTime().toLocalDateTime();
            LocalDateTime endDateTime = exam.getEndDateTime().toLocalDateTime();

            String date = startDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));

            String startTime = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            String endTime = endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));


            Label examTitleLabel = new Label(exam.getTitle());
            Label examDateLabel = new Label(date);
            Label examStartTimeLabel = new Label(startTime);
            Label examEndTimeLabel = new Label(endTime);
            Label examDurationLabel = new Label(exam.getDurationInMinutes().toString());

            Button startButton = new Button("Start");

            // Action for start
            startButton.setOnAction(e -> {
                // set the selected exam to start
                selectedExam = exam;
                try {
                    Node source = (Node) e.getSource();
                    Node root = source.getScene().getRoot();
                    Pane contentPane = (Pane) root.lookup("#contentPane");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "CurrentExam.fxml"));
                    Parent view = loader.load();

                    // Get a reference to the loaded exam controller
                    ExamController examController = loader.getController();
                    examController.setSelectedExam(selectedExam);
                    examController.setCurrentAttempt(new Attempt());


                    // switch scene
                    Stage stage = (Stage) source.getScene().getWindow();
                    Scene selectedExamScene = new Scene(view);
                    stage.setScene(selectedExamScene);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });

            // Spacer Region
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox examBox = new HBox(10, examTitleLabel, examDateLabel, examDurationLabel, examStartTimeLabel, examEndTimeLabel, spacer, startButton);
            examBox.setPadding(new Insets(5, 20, 5, 20));
            examBox.setAlignment(Pos.CENTER_LEFT);
            examBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

            examBox.setUserData(exam);

            examsToDoVbox.getChildren().add(examBox);
        }
    }

    public void loadCurrentExamQuestionsBtn(ActionEvent event) throws IOException {
        currentExamVbox.getChildren().clear();
        answerGroups.clear(); // clear any previous state

        List<Question> questions = selectedExam.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            VBox questionBox = new VBox(5);
            questionBox.setPadding(new Insets(10));
            questionBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;");

            Label questionLabel = new Label((i + 1) + ". " + question.getQuestion());
            questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            questionBox.getChildren().add(questionLabel);

            ToggleGroup group = new ToggleGroup();

            for (Answer answer : question.getAnswerChoices()) {
                RadioButton answerBtn = new RadioButton(answer.getAnswerText());
                answerBtn.setToggleGroup(group);
                answerBtn.setUserData(answer); // store Answer object in the radio button
                questionBox.getChildren().add(answerBtn);
            }

            answerGroups.add(group); // just store the group
            currentExamVbox.getChildren().add(questionBox);
        }

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        setWindowInLockdownMode(stage);

        currentAttempt.setStartTime(Timestamp.from(Instant.now()));

        startCountdownTimer(selectedExam.getDurationInMinutes() * 60, timerVbox);
    }

    public void submitCurrentAttemptBtn(ActionEvent event) throws IOException {
        submitAttempt();

        stopCountdownTimer();

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        undoWindowLockdownMode(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "Main.fxml"));
        Parent view = loader.load();

        Scene scene = new Scene(view);
        stage.setScene(scene);

    }


    public List<Answer> getSelectedAnswers() {
        List<Answer> selectedAnswers = new ArrayList<>();

        for (ToggleGroup group : answerGroups) {
            Toggle selectedToggle = group.getSelectedToggle();
            if (selectedToggle != null) {
                Answer answer = (Answer) selectedToggle.getUserData();
                selectedAnswers.add(answer);
            }
        }

        return selectedAnswers;
    }

    public void setSelectedExam(Exam selectedExam) {
        this.selectedExam = selectedExam;
    }

    public void setCurrentAttempt(Attempt currentAttempt) {
        this.currentAttempt = currentAttempt;
    }

    public Attempt getCurrentAttempt() {
        return currentAttempt;
    }

    public void startCountdownTimer(int totalTime, VBox targetContainer) throws IOException {
        // UI components
        ProgressBar timerBar = new ProgressBar(1);
        timerBar.setPrefWidth(300);


        Label timeLabel = new Label(totalTime + "s left");

        // Styling and layout
        VBox timerBox = new VBox(10, timerBar, timeLabel);
        timerBox.setPadding(new Insets(10));
        timerBox.setAlignment(Pos.CENTER);

        // Add timer box to target container
        targetContainer.getChildren().add(timerBox);

        // Timer logic
        IntegerProperty timeLeft = new SimpleIntegerProperty(totalTime);
        final Timeline[] timelineRef = new Timeline[1];

        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft.set(timeLeft.get() - 1);

            // Update visuals
            double progress = (double) timeLeft.get() / totalTime;
            timerBar.setProgress(progress);
            timeLabel.setText(timeLeft.get() + "s left");

            // Time up
            if (timeLeft.get() <= 0) {
                timelineRef[0].stop();

                submitAttempt();
                Stage stage = (Stage) timerBox.getScene().getWindow();
                undoWindowLockdownMode(stage);

                // Show alert
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Time's up!");
                    alert.showAndWait();
                });

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "Main.fxml"));
                    Parent view = loader.load();
                    Scene scene = new Scene(view);
                    stage.setScene(scene);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            }
        }));

        countdownTimeline.setCycleCount(totalTime);
        countdownTimeline.play();
        timelineRef[0] = countdownTimeline;
    }

    public void stopCountdownTimer() {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }
    }

    public VBox getTimerVbox() {
        return timerVbox;
    }

    public void submitAttempt() {
        List<Answer> selectedAnswers = getSelectedAnswers();
        Integer totalScore = 0;

        for (Answer answer : selectedAnswers) {
            if (answer.getCorrect()) {
                totalScore++;
            }
        }

        currentAttempt.setSelectedAnswers(selectedAnswers);
        currentAttempt.setTotalScore((totalScore / selectedAnswers.size()) * 100);
        currentAttempt.setSubmittedTime(Timestamp.from(Instant.now()));
        currentAttempt.setExam(selectedExam);

        addAttempt(currentAttempt);
    }

    public static void setWindowInLockdownMode(Stage stage) {
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (event.isAltDown()) {
                if (event.getCode() == KeyCode.F4) {
                    event.consume();
                }
                if (event.getCode() == KeyCode.TAB) {
                    event.consume();
                }
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }

            if (event.getCode() == KeyCode.F4) {
                event.consume();
            }
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
            }

        });

        stage.setOnCloseRequest(event -> {
            event.consume();
        });

        // Set the Stage to fullscreen
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setOpacity(1.0);

        // Lock the window from resizing, minimizing, or moving
        stage.setResizable(false);
        stage.setIconified(false);
        stage.setMaximized(true);

        // Keep the window on top of all other windows
        stage.setAlwaysOnTop(true);


    }

    public static void undoWindowLockdownMode(Stage stage) {
        // Exit fullscreen mode
        stage.setFullScreen(false);
        stage.setOpacity(1.0);

        // Allow window resizing, minimizing, and moving
        stage.setResizable(true);
        stage.setIconified(true);
        stage.setMaximized(false);

        stage.setHeight(800);
        stage.setWidth(600);

        // Remove always on top behavior
        stage.setAlwaysOnTop(false);

        // Remove the event filter (unblock the keys)
        stage.removeEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }
            if (event.getCode() == KeyCode.F4 && event.isAltDown()) {
                event.consume();
            }
            if (event.getCode() == KeyCode.TAB && event.isAltDown()) {
                event.consume();
            }
        });

        // Allow closing and minimize again
        stage.setOnCloseRequest(null);


    }
}
