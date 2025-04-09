package com.example.examfrontend.controllers;

import com.example.examfrontend.dtos.Role;
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
import java.util.ArrayList;
import java.util.List;


public class RoleController {
    private Role selectedRole;
    private List<Role> roles;
    private final String viewsPath = "/com/example/examfrontend/views/";


    @FXML
    private VBox rolesVbox;

    // Add role field
    @FXML
    private TextField addRoleTextField;


    // Update role field
    @FXML
    private TextField updateRoleTextField;


    // Http Requests

    public void addRole(Role newRole) {
        try {
            // Send Post to backend
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(newRole);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/roles"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {

                // can be used later
                Role createdRole = mapper.readValue(response.body(), Role.class);

            } else {
                System.err.println("Server returned error: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while sending request: " + e.getMessage());
        }


    }

    public void deleteRole(Role role) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/roles/" + selectedRole.getId()))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                System.out.println("Role deleted successfully");
            } else {
                System.out.println("Failed to delete role. Status code: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRole(Role updatedRole) {


        // Send the Role to backend
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();


            String json = mapper.writeValueAsString(updatedRole);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/roles/" + updatedRole.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Role update successfully");
            } else {
                System.out.println("Failed to update role. Status Code: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Role> loadAllRoles() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/roles"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            List<Role> loadedRoles = new ArrayList<>();
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                loadedRoles = mapper.readValue(
                        response.body(),
                        new TypeReference<List<Role>>() {
                        }
                );
            }
            return loadedRoles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Associated Buttons

    public void addRoleBtn(ActionEvent event) throws IOException {
        Role newRole = new Role();
        newRole.setName(addRoleTextField.getText());

        // add role
        addRole(newRole);

        // Load updated Roles.fxml view
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        Parent view = FXMLLoader.load(getClass().getResource(viewsPath + "Roles.fxml"));
        contentPane.getChildren().setAll(view);
    }

    public void deleteRoleBtn(ActionEvent event) {
        deleteRole(selectedRole);
    }

    public void updateRoleBtn(ActionEvent event) throws IOException {
        // Update the selected role
        Role updatedRole = selectedRole;
        updatedRole.setName(updateRoleTextField.getText());

        // update the role
        updateRole(updatedRole);

        // switch back to the roles view
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        Parent view = FXMLLoader.load(getClass().getResource(viewsPath + "Roles.fxml"));
        contentPane.getChildren().setAll(view);

    }

    public void loadRolesBtn(ActionEvent event) {
        // clear the roles list
        rolesVbox.getChildren().clear();

        // load the roles list
        roles = loadAllRoles();

        for (Role role : roles) {
            Label nameLabel = new Label(role.getName());

            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Delete");

            // Action for update
            updateButton.setOnAction(e -> {
                // set the selected role to update
                selectedRole = role;

                System.out.println("Update clicked for: " + selectedRole.getName());
                try {
                    switchToUpdateRoleBtn(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            });

            // Action for delete
            deleteButton.setOnAction(e -> {
                selectedRole = role;
                System.out.println("Delete clicked for: " + selectedRole.getName());
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Deleting role...");
                    alert.setHeaderText("You're about to delete this role!");
                    alert.setContentText("Are you sure you want to delete?");

                    if (alert.showAndWait().get() == ButtonType.OK) {
                        deleteRole(selectedRole);
                        rolesVbox.getChildren().removeIf(node -> node.getUserData() == role);
                        System.out.println(selectedRole.getName() + " role has been deleted successfully");
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            });

            // Spacer Region
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox roleBox = new HBox(10, nameLabel, spacer, updateButton, deleteButton);

            roleBox.setPadding(new Insets(5, 20, 5, 20));
            roleBox.setAlignment(Pos.CENTER_LEFT);
            roleBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

            roleBox.setUserData(role);

            rolesVbox.getChildren().add(roleBox);
        }
    }


    // Controller methods

    public void switchToAddRoleBtn(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Pane contentPane = (Pane) source.getScene().getRoot().lookup("#contentPane");
        Parent view = FXMLLoader.load(getClass().getResource(viewsPath + "AddRole.fxml"));
        contentPane.getChildren().setAll(view);
    }

    public void switchToUpdateRoleBtn(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        selectedRole = (Role) source.getParent().getUserData(); // this gets the user data of the button's container
        System.out.println("Current selected role: " + selectedRole.getName());

        Node root = source.getScene().getRoot();
        Pane contentPane = (Pane) root.lookup("#contentPane");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewsPath + "UpdateRole.fxml"));

        Parent view = loader.load();
        RoleController controller = loader.getController();
        controller.setSelectedRole(selectedRole);
        controller.populateUpdateRoleField();

        contentPane.getChildren().setAll(view);
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
    }

    public void populateUpdateRoleField() {
        if (updateRoleTextField != null & selectedRole != null) {
            updateRoleTextField.setText(selectedRole.getName());
        }
    }

}
