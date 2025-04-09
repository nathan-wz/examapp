package com.example.examfrontend.controllers;

import com.example.examfrontend.dtos.Answer;
import com.example.examfrontend.dtos.Exam;
import com.example.examfrontend.dtos.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ExamCreatorController {
    private Exam selectedExam;
    private Question selectedQuestion;
    private List<Exam> exams;
    private List<Question> questions;
    private final String viewsPath = "/com/example/examfrontend/views/";

    @FXML
    private VBox examsVbox;

    @FXML
    private VBox questionsVbox;

    // Add Exam Fields
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField durationTextField;
    @FXML
    private TextField endTimeTextField;


    // Update Exam Fields
    @FXML
    private TextField updatedTitleTextField;
    @FXML
    private TextField updatedDateTextField;
    @FXML
    private TextField updatedStartTimeTextField;
    @FXML
    private TextField updatedEndTimeTextField;
    @FXML
    private TextField updatedDurationTextField;

    // Add Question Fields
    @FXML
    private TextField questionTextField;
    @FXML
    private VBox answersVbox;


    // Update Question Fields
    @FXML
    private TextField updatedQuestionTextField;
    @FXML
    private VBox updatedAnswersVbox;


    private ToggleGroup correctAnswerGroup = new ToggleGroup();


    // Warnings Add Exam
    @FXML
    private Label formatDateLabel;
    @FXML
    private Label formatStartTimeLabel;
    @FXML
    private Label formatEndTimeLabel;

    // Warnings Update Exam
    @FXML
    private Label updatedFormatDateLabel;
    @FXML
    private Label updatedFormatStartTimeLabel;
    @FXML
    private Label updatedFormatEndTimeLabel;

    // Http requests

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


    // Associated buttons

    public void addExamBtn(ActionEvent event) throws IOException {
        // create new exam
        Exam newExam = new Exam();

        String startDateTimeString = dateTextField.getText() + " " + startTimeTextField.getText();
        String endDateTimeString = dateTextField.getText() + " " + endTimeTextField.getText();

        // trigger date time warnings if any
        boolean isValidDate = isValidDateFormat(dateTextField.getText());
        if (!isValidDate) formatDateLabel.setText("Incorrect date format");

        boolean isValidStartTime = isValidTimeFormat(startTimeTextField.getText());
        if (!isValidStartTime) formatStartTimeLabel.setText("Incorrect time format");

        boolean isValidEndTime = isValidTimeFormat(endTimeTextField.getText());
        if (!isValidEndTime) formatEndTimeLabel.setText("Incorrect time format");


        boolean isValidStartDateTime = isValidDateTimeFormat(startDateTimeString);
        boolean isValidEndDateTime = isValidDateTimeFormat(endDateTimeString);

        if (isValidStartDateTime && isValidEndDateTime) {
            Timestamp startDateTime = convertDateTimeToTimestamp(startDateTimeString);
            Timestamp endDateTime = convertDateTimeToTimestamp(endDateTimeString);
            Integer duration = convertStringToInteger(durationTextField.getText());

            // add exam values
            newExam.setTitle(titleTextField.getText());
            newExam.setStartDateTime(startDateTime);
            newExam.setEndDateTime(endDateTime);
            newExam.setDurationInMinutes(duration);

            // add new exam
            addExam(newExam);

            // Load update ExamsToDo.fxml view
            Node source = (Node) event.getSource();
            Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
            Parent view = FXMLLoader.load(getClass().getResource(viewsPath + "ExamCreator.fxml"));
            contentPane.getChildren().setAll(view);
        }


    }

    public void deleteExamBtn(ActionEvent event) throws IOException {
        deleteExam(selectedExam);
    }

    public void updateExamBtn(ActionEvent event) throws IOException {
        // store exam values
        Exam updatedExam = selectedExam;

        String startDateTimeString = updatedDateTextField.getText() + " " + updatedStartTimeTextField.getText();
        String endDateTimeString = updatedDateTextField.getText() + " " + updatedEndTimeTextField.getText();

        // trigger date time warnings if any
        boolean isValidDate = isValidDateFormat(updatedDateTextField.getText());
        if (!isValidDate) updatedFormatDateLabel.setText("Incorrect date format");

        boolean isValidStartTime = isValidTimeFormat(updatedStartTimeTextField.getText());
        if (!isValidStartTime) updatedFormatStartTimeLabel.setText("Incorrect time format");

        boolean isValidEndTime = isValidTimeFormat(updatedEndTimeTextField.getText());
        if (!isValidEndTime) updatedFormatEndTimeLabel.setText("Incorrect time format");


        boolean isValidStartDateTime = isValidDateTimeFormat(startDateTimeString);
        boolean isValidEndDateTime = isValidDateTimeFormat(endDateTimeString);

        if (isValidStartDateTime && isValidEndDateTime) {
            Timestamp startDateTime = convertDateTimeToTimestamp(startDateTimeString);
            Timestamp endDateTime = convertDateTimeToTimestamp(endDateTimeString);
            Integer duration = convertStringToInteger(updatedDurationTextField.getText());

            // add exam values
            updatedExam.setTitle(updatedTitleTextField.getText());
            updatedExam.setStartDateTime(startDateTime);
            updatedExam.setEndDateTime(endDateTime);
            updatedExam.setDurationInMinutes(duration);

            // update the exam
            updateExam(updatedExam);

            // Load update ExamsToDo.fxml view
            Node source = (Node) event.getSource();
            Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
            Parent view = FXMLLoader.load(getClass().getResource(viewsPath + "ExamCreator.fxml"));
            contentPane.getChildren().setAll(view);
        }

    }

    public void addQuestionBtn(ActionEvent event) throws IOException {
        // Get question text field from FXML
        String questionText = questionTextField.getText();

        if (questionText == null || questionText.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Question text cannot be empty.");
            alert.show();
            return;
        }

        // Build answers from VBox
        ArrayList<Answer> answers = new ArrayList<>();
        for (Node node : answersVbox.getChildren()) {
            if (node instanceof HBox hBox && hBox.getChildren().size() >= 2) {
                TextField answerField = (TextField) hBox.getChildren().get(0);
                RadioButton correctRadio = (RadioButton) hBox.getChildren().get(1);

                String answerText = answerField.getText();
                if (answerText == null || answerText.isBlank()) continue;

                Answer answer = new Answer();
                answer.setAnswerText(answerText);
                answer.setCorrect(correctRadio.isSelected());

                answers.add(answer);
            }
        }

        if (answers.size() < 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must provide at least two answers.");
            alert.show();
            return;
        }

        // Build Question DTO
        Question newQuestion = new Question();
        newQuestion.setQuestion(questionText);
        newQuestion.setAnswerChoices(answers);
        newQuestion.setExam(selectedExam);

        // Update exam questions locally
        selectedExam.getQuestions().add(newQuestion);

        updateExam(selectedExam);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Question added successfully.");
        alert.show();

        // Reload Question List
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "Questions.fxml"));
        Parent view = loader.load();

        // set selected exam back to controller
        ExamCreatorController controller = loader.getController();
        controller.setSelectedExam(selectedExam);

        contentPane.getChildren().setAll(view);
    }

    public void updateQuestionBtn(ActionEvent event) throws IOException {
        // Get question text field from FXML
//        TextField questionTextField = (TextField) answersVbox.getScene().lookup("#questionTextField");

        String questionText = updatedQuestionTextField.getText();

        if (questionText == null || questionText.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Question text cannot be empty.");
            alert.show();
            return;
        }

        // Build answers from VBox
        List<Answer> updatedAnswers = new ArrayList<>();
        for (Node node : updatedAnswersVbox.getChildren()) {
            if (node instanceof HBox hBox && hBox.getChildren().size() >= 2) {
                TextField answerField = (TextField) hBox.getChildren().get(0);
                RadioButton correctRadio = (RadioButton) hBox.getChildren().get(1);

                String answerText = answerField.getText();
                if (answerText == null || answerText.isBlank()) continue;

                Answer answer = new Answer();
                answer.setAnswerText(answerText);
                answer.setCorrect(correctRadio.isSelected());

                updatedAnswers.add(answer);
            }
        }

        if (updatedAnswers.size() < 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must provide at least two answers.");
            alert.show();
            return;
        }

        // Update selectedQuestion object
        selectedQuestion.setQuestion(questionText);
        selectedQuestion.setAnswerChoices(updatedAnswers);

        // Persist changes in selectedExam
        updateExam(selectedExam);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Question updated successfully.");
        alert.show();

        // Reload Question List
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "Questions.fxml"));
        Parent view = loader.load();

        ExamCreatorController controller = loader.getController();
        controller.setSelectedExam(selectedExam);

        contentPane.getChildren().setAll(view);
    }

    public void loadExamsBtn(ActionEvent event) throws IOException {
        // clear the display list
        examsVbox.getChildren().clear();

        // load the exams list
        exams = loadExams();

        for (Exam exam : exams) {
            String examTitle = exam.getTitle();
            String examDate = convertTimestampToStringDate(exam.getStartDateTime());
            String startTime = convertTimestampToStringTime(exam.getStartDateTime());
            String endTime = convertTimestampToStringTime(exam.getEndDateTime());

            Label examTitleLabel = new Label(examTitle);
            Label examDateLabel = new Label(examDate);
            Label examStartTimeLabel = new Label(startTime);
            Label examEndTimeLabel = new Label(endTime);

            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Delete");
            Button editButton = new Button("Edit");

            // Action for update
            updateButton.setOnAction(e -> {
                // set the selected exam to update
                selectedExam = exam;
                try {
                    switchToUpdateExamBtn(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });

            // Action for delete
            deleteButton.setOnAction(e -> {
                // set the selected exam to update
                selectedExam = exam;

                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Deleting role...");
                    alert.setHeaderText("You're about to delete this user!");
                    alert.setContentText("Are you sure you want to delete?");

                    if (alert.showAndWait().get() == ButtonType.OK) {
                        deleteExam(exam);
                        examsVbox.getChildren().removeIf(node -> node.getUserData() == exam);
                        System.out.println(selectedExam.getTitle() + " exam has been deleted successfully");
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });

            // Action for edit
            editButton.setOnAction(e -> {
                // set the selected exam to edit
                selectedExam = exam;
                try {
                    switchToQuestionList(e);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            });

            // Spacer Region
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox examBox = new HBox(10, examTitleLabel, spacer, editButton, updateButton, deleteButton);
            examBox.setPadding(new Insets(5, 20, 5, 20));
            examBox.setAlignment(Pos.CENTER_LEFT);
            examBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
            examBox.setUserData(exam);

            examsVbox.getChildren().add(examBox);

        }

    }

    public void loadQuestionsBtn(ActionEvent event) throws IOException {
        // clear the display list
        questionsVbox.getChildren().clear();


        // get questions from selected exam
        questions = selectedExam.getQuestions();

        for (Question question : questions) {
            Label questionNameLabel = new Label(question.getQuestion());

            ArrayList<Label> answerLabels;

            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Delete");

            // Action for update
            updateButton.setOnAction(e -> {
                // set the selected question to update
                selectedQuestion = question;
                try {
                    switchToUpdateQuestionBtn(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });

            // Action for delete
            deleteButton.setOnAction(e -> {
                // set the selected exam to update
                selectedQuestion = question;

                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Deleting question...");
                    alert.setHeaderText("You're about to delete this question!");
                    alert.setContentText("Are you sure you want to delete?");

                    if (alert.showAndWait().get() == ButtonType.OK) {
                        selectedQuestion = null;
                        updateExam(selectedExam);
                        questionsVbox.getChildren().removeIf(node -> node.getUserData() == question);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });

            // Spacer Region
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox questionBox = new HBox(10, questionNameLabel, spacer, updateButton, deleteButton);
            questionBox.setPadding(new Insets(5, 20, 5, 20));
            questionBox.setAlignment(Pos.CENTER_LEFT);
            questionBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

            questionBox.setUserData(question);

            questionsVbox.getChildren().add(questionBox);

        }
    }


    public void switchToAddExamBtn(ActionEvent event) throws IOException {
        // create the fxml Loader
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "AddExam.fxml"));
        Parent view = loader.load();

        contentPane.getChildren().setAll(view);
    }

    public void switchToUpdateExamBtn(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        selectedExam = (Exam) source.getParent().getUserData(); // this gets the user data of the button's container

        Node root = source.getScene().getRoot();
        Pane contentPane = (Pane) root.lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "UpdateExam.fxml"));
        Parent view = loader.load();

        // Get a reference to the loaded exam creator controller
        ExamCreatorController examCreatorController = loader.getController();
        examCreatorController.setSelectedExam(selectedExam);

        contentPane.getChildren().setAll(view);
    }

    public void switchToQuestionList(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        selectedExam = (Exam) source.getParent().getUserData(); // this gets the user data of the button's container

        Node root = source.getScene().getRoot();
        Pane contentPane = (Pane) root.lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "Questions.fxml"));
        Parent view = loader.load();

        // Get a reference to the loaded exam creator controller
        ExamCreatorController examCreatorController = loader.getController();
        examCreatorController.setSelectedExam(selectedExam);

        contentPane.getChildren().setAll(view);
    }

    public void switchToAddQuestionBtn(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource(); // this gets the user data of the button's container

        Node root = source.getScene().getRoot();
        Pane contentPane = (Pane) root.lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "AddQuestion.fxml"));
        Parent view = loader.load();

        // Get a reference to the loaded exam creator controller
        ExamCreatorController examCreatorController = loader.getController();
        examCreatorController.setSelectedExam(selectedExam);

        contentPane.getChildren().setAll(view);
    }

    public void switchToUpdateQuestionBtn(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        selectedQuestion = (Question) source.getParent().getUserData(); // this gets the user data of the button's container

        Node root = source.getScene().getRoot();
        Pane contentPane = (Pane) root.lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "UpdateQuestion.fxml"));
        Parent view = loader.load();

        // Get a reference to the loaded exam creator controller
        ExamCreatorController examCreatorController = loader.getController();
        examCreatorController.setSelectedExam(selectedExam);
        examCreatorController.setSelectedQuestion(selectedQuestion);

        examCreatorController.getUpdatedQuestionTextField().setText(selectedQuestion.getQuestion());

        contentPane.getChildren().setAll(view);
    }

    public void addAnswerRowBtn() {
        HBox answerRow = new HBox(10);
        answerRow.setAlignment(Pos.CENTER_LEFT);

        TextField answerField = new TextField();
        answerField.setPromptText("Enter answer");
        answerField.setPrefWidth(300);

        RadioButton correctRadio = new RadioButton("Correct");
        correctRadio.setToggleGroup(correctAnswerGroup);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> answersVbox.getChildren().remove(answerRow));

        answerRow.getChildren().addAll(answerField, correctRadio, deleteButton);
        answersVbox.getChildren().add(answerRow);
    }

    public void updateAnswerChoiceRowBtn() {
        HBox answerRow = new HBox(10);
        answerRow.setAlignment(Pos.CENTER_LEFT);

        TextField answerField = new TextField();
        answerField.setPromptText("Enter answer");
        answerField.setPrefWidth(300);

        RadioButton correctRadio = new RadioButton("Correct");
        correctRadio.setToggleGroup(correctAnswerGroup);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> updatedAnswersVbox.getChildren().remove(answerRow));

        answerRow.getChildren().addAll(answerField, correctRadio, deleteButton);
        updatedAnswersVbox.getChildren().add(answerRow);
    }


    // controller methods

    public void setSelectedExam(Exam selectedExam) {
        this.selectedExam = selectedExam;
    }

    public void setSelectedQuestion(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }


    // Validate Date format (yyyy-MM-dd)
    public boolean isValidDateFormat(String dateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(dateStr, dateFormatter);
            return true; // No exception means valid
        } catch (DateTimeParseException e) {
            return false; // Exception means invalid
        }
    }

    // Validate Time format (HH:mm:ss)
    public boolean isValidTimeFormat(String timeStr) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime.parse(timeStr, timeFormatter);
            return true; // No exception means valid
        } catch (DateTimeParseException e) {
            return false; // Exception means invalid
        }
    }

    // Validate DateTime format (yyyy-MM-dd HH:mm:ss)
    public boolean isValidDateTimeFormat(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
            return true; // No exception means valid
        } catch (DateTimeParseException e) {
            return false; // Exception means invalid
        }
    }

    public Timestamp convertDateTimeToTimestamp(String dateTimeString) {
        // Define the DateTime format (assuming the format is "yyyy-MM-dd HH:mm")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            // Parse the string to a LocalDateTime object
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

            // Convert LocalDateTime to Timestamp
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            System.out.println("Invalid date format: " + e.getMessage());
            return null; // Or throw an exception if needed
        }
    }

    public String convertTimestampToStringDate(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();

        return localDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public String convertTimestampToStringTime(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();

        return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public Integer convertStringToInteger(String str) {
        try {
            Integer num = Integer.valueOf(str);
            return num;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setUpdatedQuestionTextField(TextField updatedQuestionTextField) {
        this.updatedQuestionTextField = updatedQuestionTextField;
    }

    public TextField getUpdatedQuestionTextField() {
        return updatedQuestionTextField;
    }

}
