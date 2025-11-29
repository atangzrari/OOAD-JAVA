package com.banking.controller;

import com.banking.model.Account;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class AccountController {

    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, String> numberColumn;
    @FXML
    private TableColumn<Account, String> typeColumn;
    @FXML
    private TableColumn<Account, String> balanceColumn;
    @FXML
    private TableColumn<Account, String> branchColumn;

    @FXML
    private void initialize() {
        numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccountNumber()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType().name()));
        balanceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBalance().toPlainString()));
        branchColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getBranch() == null ? "" : data.getValue().getBranch()));
    }

    public void setAccounts(List<Account> accounts) {
        accountTable.setItems(FXCollections.observableArrayList(accounts));
    }
}
