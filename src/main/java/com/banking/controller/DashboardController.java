package com.banking.controller;

import com.banking.model.Customer;
import com.banking.model.User;
import com.banking.model.UserRole;
import com.banking.service.AccountService;
import com.banking.service.AuditService;
import com.banking.service.CustomerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private StackPane contentArea;

    private final AccountService accountService = new AccountService();
    private final CustomerService customerService = new CustomerService();
    private final AuditService auditService = new AuditService();

    private User currentUser;
    private Customer currentCustomer;

    public void initialize(User user, Customer customer) throws Exception {
        this.currentUser = user;
        this.currentCustomer = customer;
        this.welcomeLabel.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        if (user.getRole() == UserRole.ADMIN) {
            loadAdmin();
        } else {
            loadCustomer();
        }
    }

    private void loadAdmin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/view/admin-dashboard.fxml"));
        Node view = loader.load();
        AdminController controller = loader.getController();
        controller.init(accountService, customerService, auditService, currentUser);
        contentArea.getChildren().setAll(view);
    }

    private void loadCustomer() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/view/customer-dashboard.fxml"));
        Node view = loader.load();
        CustomerController controller = loader.getController();
        controller.init(accountService, customerService, currentCustomer, currentUser);
        contentArea.getChildren().setAll(view);
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/banking/view/main-login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("WorldBank Login");
        } catch (Exception e) {
            throw new IllegalStateException("Unable to return to login", e);
        }
    }
}
