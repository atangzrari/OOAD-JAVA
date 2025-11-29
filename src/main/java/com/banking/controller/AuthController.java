package com.banking.controller;

import com.banking.service.AuthService;
import com.banking.service.AuthService.AuthenticatedUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            errorLabel.setVisible(false);

            AuthenticatedUser auth = authService.authenticate(
                    usernameField.getText().trim(),
                    passwordField.getText());

            // Indicate successful authentication and which dashboard will open
            System.out.println("Login successful for user '" + auth.user().getUsername()
                    + "' with role " + auth.user().getRole());

            loadDashboard(auth);
        } catch (Exception ex) {
            // Authentication failed – show a clear, user-friendly message
            ex.printStackTrace();
            errorLabel.setText("Invalid username or password");
            errorLabel.setVisible(true);
        }
    }

    private void loadDashboard(AuthenticatedUser auth) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/view/dashboard.fxml"));
        Parent root = loader.load();
        DashboardController controller = loader.getController();
        controller.initialize(auth.user(), auth.customer());

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("WorldBank Dashboard");
    }
}
