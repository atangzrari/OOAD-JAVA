package com.banking.controller;

import com.banking.model.Account;
import com.banking.model.AuditLog;
import com.banking.model.Customer;
import com.banking.model.User;
import com.banking.service.AccountService;
import com.banking.service.AuditService;
import com.banking.service.CustomerService;
import com.banking.service.CustomerService.CustomerRegistrationRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class AdminController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private CheckBox employedCheckBox;
    @FXML
    private TextField employerNameField;
    @FXML
    private TextField employerAddressField;
    @FXML
    private Label feedbackLabel;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> firstNameColumn;
    @FXML
    private TableColumn<Customer, String> lastNameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableView<AuditLog> auditTable;
    @FXML
    private TableColumn<AuditLog, String> actionColumn;
    @FXML
    private TableColumn<AuditLog, String> timestampColumn;
    @FXML
    private AccountController allAccountsController;

    private AccountService accountService;
    private CustomerService customerService;
    private AuditService auditService;
    private User currentUser;

    public void init(AccountService accountService,
            CustomerService customerService,
            AuditService auditService,
            User currentUser) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.auditService = auditService;
        this.currentUser = currentUser;
        configureTables();
        refreshData();
    }

    private void configureTables() {
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        addressColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
        actionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAction()));
        timestampColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp()));
    }

    @FXML
    private void handleRegisterCustomer() {
        try {
            CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                    usernameField.getText().trim(),
                    passwordField.getText(),
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    addressField.getText().trim(),
                    employedCheckBox.isSelected(),
                    employerNameField.getText().trim(),
                    employerAddressField.getText().trim());
            Customer created = customerService.registerCustomer(request);
            auditService.record(currentUser.getId(),
                    "Registered customer %s %s".formatted(created.getFirstName(), created.getLastName()));
            feedbackLabel.setText("Customer registered successfully");
            clearForm();
            refreshData();
        } catch (Exception ex) {
            feedbackLabel.setText(ex.getMessage());
        }
    }

    private void refreshData() {
        List<Customer> customers = customerService.listCustomers();
        customerTable.setItems(FXCollections.observableArrayList(customers));
        List<Account> accounts = accountService.getAllAccounts();
        if (allAccountsController != null) {
            allAccountsController.setAccounts(accounts);
        } else {
            System.err.println("ERROR: allAccountsController is null!");
        }
        List<AuditLog> logs = auditService.recentLogs(50);
        auditTable.setItems(FXCollections.observableArrayList(logs));
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        employedCheckBox.setSelected(false);
        employerNameField.clear();
        employerAddressField.clear();
    }
}
