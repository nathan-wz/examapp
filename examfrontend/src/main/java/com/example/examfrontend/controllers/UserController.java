package com.example.examfrontend.controllers;

import com.example.examfrontend.dtos.Role;
import com.example.examfrontend.dtos.User;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private User selectedUser;
    private List<User> users;
    private final String viewsPath = "/com/example/examfrontend/views/";

    @FXML
    private VBox usersVbox;


    // Add user fields
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField idNumberTextField;
    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    // Update user fields
    @FXML
    private TextField updatedUsernameTextField;
    @FXML
    private TextField updatedPasswordTextField;
    @FXML
    private TextField updatedFirstNameTextField;
    @FXML
    private TextField updatedLastNameTextField;
    @FXML
    private TextField updatedIdNumberTextField;
    @FXML
    private ChoiceBox<Role> updatedRoleChoiceBox;

    // Http Requests

    public void addUser(User newUser) {
        try {
            // Send Post to backend
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(newUser);
            System.out.println(json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200 || response.statusCode() == 201) {
                // can be used later
                User createdUser = mapper.readValue(response.body(), User.class);
            } else {
                System.err.println("Server returned error: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while sending request: " + e.getMessage());
        }
    }

    private void deleteUser(User user) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users/" + selectedUser.getId()))
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

    public void updateUser(User updatedUser) {

        // Send the updated user to backend
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(updatedUser);
            System.out.println("Json: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users/" + updatedUser.getId()))
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

    public List<User> loadUsers() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());


            List<User> loadedUsers = new ArrayList<>();
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                loadedUsers = mapper.readValue(
                        response.body(),
                        new TypeReference<List<User>>() {
                        }
                );
            }
            return loadedUsers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Associated buttons

    public void addUserBtn(ActionEvent event) throws IOException {
        // store user values
        User newUser = new User();
        newUser.setUsername(usernameTextField.getText());
        newUser.setPassword(passwordTextField.getText());
        newUser.setFirstName(firstNameTextField.getText());
        newUser.setLastName(lastNameTextField.getText());
        newUser.setIdNumber(idNumberTextField.getText());
        newUser.setRole(roleChoiceBox.getValue());

        // add the new user
        addUser(newUser);

        // Load updated Users.fxml view
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        Parent view = FXMLLoader.load(getClass().getResource(viewsPath + "Users.fxml"));
        contentPane.getChildren().setAll(view);
    }

    public void deleteUserBtn(ActionEvent event) {
        deleteUser(selectedUser);
    }

    public void updateUserBtn(ActionEvent event) throws IOException {
        // Update the selected user
        User updatedUser = selectedUser;
        updatedUser.setUsername(updatedUsernameTextField.getText());
        updatedUser.setPassword(updatedPasswordTextField.getText());
        updatedUser.setFirstName(updatedFirstNameTextField.getText());
        updatedUser.setLastName(updatedLastNameTextField.getText());
        updatedUser.setIdNumber(updatedIdNumberTextField.getText());
        updatedUser.setRole(updatedRoleChoiceBox.getValue());

        // update the user
        updateUser(updatedUser);

        // switch back to users view
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        Parent view = FXMLLoader.load(getClass().getResource(viewsPath + "Users.fxml"));
        contentPane.getChildren().setAll(view);
    }

    public void loadUsersBtn(ActionEvent event) throws IOException {
        // clear the display list
        usersVbox.getChildren().clear();

        // load the users list
        users = loadUsers();

        for (User user : users) {
            Label usernameLabel = new Label(user.getUsername());
            Label roleLabel = new Label(user.getRole().getName());

            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Delete");

            // Action for update
            updateButton.setOnAction(e -> {
                // set the selected user to update
                selectedUser = user;

                System.out.println("Update clicked for: " + selectedUser.getUsername());
                try {
                    switchToUpdateUserBtn(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            });

            // Action for delete
            deleteButton.setOnAction(e -> {
                // set the selected user to delete
                selectedUser = user;

                System.out.println("Delete clicked for: " + selectedUser.getUsername());
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Deleting role...");
                    alert.setHeaderText("You're about to delete this user!");
                    alert.setContentText("Are you sure you want to delete?");

                    if (alert.showAndWait().get() == ButtonType.OK) {
                        deleteUser(user);
                        usersVbox.getChildren().removeIf(node -> node.getUserData() == user);
                        System.out.println(selectedUser.getUsername() + " user has been deleted successfully");
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });

            // Spacer Region
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox roleBox = new HBox(10, usernameLabel, roleLabel, spacer, updateButton, deleteButton);
            roleBox.setPadding(new Insets(5, 20, 5, 20));
            roleBox.setAlignment(Pos.CENTER_LEFT);
            roleBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
            
            roleBox.setUserData(user);

            usersVbox.getChildren().add(roleBox);
        }

    }

    public void switchToAddUserBtn(javafx.event.ActionEvent event) throws IOException {
        // load all roles
        List<Role> allRoles = loadAllRoles();

        // create the fxml loader
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "AddUser.fxml"));
        Parent view = loader.load();

        // get a reference to user controller
        UserController userController = loader.getController();
        userController.populateRoleChoiceBox(allRoles);

        contentPane.getChildren().setAll(view);
    }

    public void switchToUpdateUserBtn(ActionEvent event) throws IOException {
        // load all roles
        List<Role> allRoles = loadAllRoles();

        Node source = (Node) event.getSource();
        selectedUser = (User) source.getParent().getUserData(); // this gets the user data of the button's container

        System.out.println("Current selected user: " + selectedUser.getUsername());

        Node root = source.getScene().getRoot();
        Pane contentPane = (Pane) root.lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "UpdateUser.fxml"));
        Parent view = loader.load();

        // Get a reference to the loaded user controller
        UserController controller = loader.getController();
        controller.setSelectedUser(selectedUser);

        // prepopulate fields
        controller.populateUpdateUserFields();
        controller.populateUpdatedRoleChoiceBox(allRoles);

        contentPane.getChildren().setAll(view);
    }

    // Controller methods

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setRoleChoiceBox(ChoiceBox<Role> roleChoiceBox) {
        this.roleChoiceBox = roleChoiceBox;
    }

    public void populateUpdateUserFields() {
        if (selectedUser != null) {
            updatedFirstNameTextField.setText(selectedUser.getFirstName());
            updatedLastNameTextField.setText(selectedUser.getLastName());
            updatedUsernameTextField.setText(selectedUser.getUsername());
            updatedPasswordTextField.setText(selectedUser.getPassword());
            updatedIdNumberTextField.setText(selectedUser.getIdNumber());
        }
    }

    public void populateUpdatedRoleChoiceBox(List<Role> roles) {
        updatedRoleChoiceBox.getItems().clear();
        updatedRoleChoiceBox.getItems().addAll(roles);

        updatedRoleChoiceBox.setConverter(new StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return role != null ? role.getName() : "";
            }

            @Override
            public Role fromString(String string) {
                return null; // Not used unless editable
            }
        });
        updatedRoleChoiceBox.setValue(selectedUser.getRole());
    }

    public void populateRoleChoiceBox(List<Role> roles) {
        roleChoiceBox.getItems().clear();
        roleChoiceBox.getItems().addAll(roles);

        roleChoiceBox.setConverter(new StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return role != null ? role.getName() : "";
            }

            @Override
            public Role fromString(String string) {
                return null; // Not used unless editable
            }
        });
    }


    public List<Role> loadAllRoles() {
        List<Role> allRoles = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/roles"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                allRoles = mapper.readValue(
                        response.body(),
                        new TypeReference<List<Role>>() {
                        }
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allRoles;
    }

}
