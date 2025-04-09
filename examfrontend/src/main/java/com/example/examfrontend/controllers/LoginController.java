package com.example.examfrontend.controllers;

import com.example.examfrontend.dtos.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class LoginController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label incorrectUsernameOrPasswordLabel;

    private final String viewsPath = "/com/example/examfrontend/views/";

    @FXML
    public void verifyCredentials(ActionEvent event) throws IOException {

        User user = new User();
        user.setUsername(usernameTextField.getText());
        user.setPassword(usernameTextField.getText());

        User currentUser = getUser(user);
        if (currentUser != null
                && currentUser.getUsername().equals(user.getUsername())
                && currentUser.getPassword().equals(user.getPassword())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "Main.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = loader.load();
            MainController mainController = loader.getController();
            mainController.setCurrentUser(currentUser);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            incorrectUsernameOrPasswordLabel.setText("Incorrect username or password");
        }
    }

    public User getUser(User user) {
        User foundUser = new User();
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(user);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users/find"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                foundUser = mapper.readValue(response.body(), User.class);
            } else if (response.statusCode() == 404) {
                foundUser = null; // User not found
            } else {
                throw new RuntimeException("Request failed: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundUser;
    }

}
