package com.banking.controller;

import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.service.AccountService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import com.banking.service.CustomerService;

import java.math.BigDecimal;
import java.util.List;

public class CustomerController {

    @FXML
    private ComboBox<AccountType> accountTypeCombo;
    @FXML
    private TextField initialDepositField;
    @FXML
    private TextField branchField;
    @FXML
    private ComboBox<String> accountSelection;
    @FXML
    private TextField transactionAmountField;
    @FXML
    private Label feedbackLabel;
    @FXML
    private AccountController customerAccountsController;
    @FXML
    private TransactionController transactionsViewController;
    @FXML
    private VBox employmentDetailsBox;
    @FXML
    private TextField employerNameField;
    @FXML
    private TextField employerAddressField;

    private CustomerService customerService;

    private AccountService accountService;
    private Customer currentCustomer;
    private User currentUser;

    public void init(AccountService accountService, CustomerService customerService, Customer customer, User user) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.currentCustomer = customer;
        this.currentUser = user;
        accountTypeCombo.setItems(FXCollections.observableArrayList(AccountType.values()));
        accountTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateEmploymentFieldsVisibility(newVal);
        });
        accountTypeCombo.getSelectionModel().select(AccountType.SAVINGS);
        refreshAccounts();
    }

    private void updateEmploymentFieldsVisibility(AccountType type) {
        boolean show = type == AccountType.CHEQUE && !currentCustomer.isEmployed();
        employmentDetailsBox.setVisible(show);
        employmentDetailsBox.setManaged(show);
    }

    @FXML
    private void handleOpenAccount() {
        try {
            AccountType type = accountTypeCombo.getSelectionModel().getSelectedItem();
            if (type == null) {
                throw new IllegalArgumentException("Select account type");
            }

            if (type == AccountType.CHEQUE && !currentCustomer.isEmployed()) {
                String empName = employerNameField.getText().trim();
                String empAddr = employerAddressField.getText().trim();
                if (empName.isEmpty() || empAddr.isEmpty()) {
                    throw new IllegalArgumentException("Employment details required for Cheque account");
                }
                currentCustomer.setEmployed(true);
                currentCustomer.setEmployerName(empName);
                currentCustomer.setEmployerAddress(empAddr);
                customerService.updateCustomer(currentCustomer);
            }

            BigDecimal initialDeposit = parseAmount(initialDepositField.getText());
            Account account = accountService.openAccount(
                    currentCustomer.getId(),
                    type,
                    initialDeposit,
                    branchField.getText().trim(),
                    currentUser.getId());
            feedbackLabel.setText("Opened account " + account.getAccountNumber());
            refreshAccounts();
            initialDepositField.clear();
            branchField.clear();
        } catch (Exception ex) {
            feedbackLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleDeposit() {
        try {
            String accountNumber = requireAccountSelection();
            BigDecimal amount = parseAmount(transactionAmountField.getText());
            accountService.deposit(accountNumber, amount, currentUser.getId());
            feedbackLabel.setText("Deposit successful");
            refreshAccounts();
        } catch (Exception ex) {
            feedbackLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleWithdraw() {
        try {
            String accountNumber = requireAccountSelection();
            BigDecimal amount = parseAmount(transactionAmountField.getText());
            accountService.withdraw(accountNumber, amount, currentUser.getId());
            feedbackLabel.setText("Withdrawal successful");
            refreshAccounts();
        } catch (Exception ex) {
            feedbackLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleApplyInterest() {
        try {
            String accountNumber = requireAccountSelection();
            accountService.applyMonthlyInterest(accountNumber, currentUser.getId());
            feedbackLabel.setText("Interest applied");
            refreshAccounts();
        } catch (Exception ex) {
            feedbackLabel.setText(ex.getMessage());
        }
    }

    private void refreshAccounts() {
        List<Account> accounts = accountService.getAccountsForCustomer(currentCustomer.getId());
        if (customerAccountsController != null) {
            customerAccountsController.setAccounts(accounts);
        } else {
            System.err.println("ERROR: customerAccountsController is null!");
        }
        accountSelection.setItems(FXCollections.observableArrayList(
                accounts.stream().map(Account::getAccountNumber).toList()));
        if (!accountSelection.getItems().isEmpty()) {
            accountSelection.getSelectionModel().selectFirst();
        }
        List<Transaction> transactions = accountService.getTransactionsForCustomer(currentCustomer.getId());
        if (transactionsViewController != null) {
            transactionsViewController.setTransactions(transactions);
        } else {
            System.err.println("ERROR: transactionsViewController is null!");
        }
        transactionAmountField.clear();
    }

    private BigDecimal parseAmount(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Amount required");
        }
        BigDecimal amount = new BigDecimal(text);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return amount;
    }

    private String requireAccountSelection() {
        String accountNumber = accountSelection.getSelectionModel().getSelectedItem();
        if (accountNumber == null) {
            throw new IllegalArgumentException("Select an account");
        }
        return accountNumber;
    }
}
