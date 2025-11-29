package com.banking.controller;

import com.banking.model.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class TransactionController {

    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, String> amountColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    @FXML
    private TableColumn<Transaction, String> timestampColumn;

    @FXML
    private void initialize() {
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        amountColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAmount().toPlainString()));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDescription() == null ? "" : data.getValue().getDescription()));
        timestampColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp()));
    }

    public void setTransactions(List<Transaction> transactions) {
        transactionTable.setItems(FXCollections.observableArrayList(transactions));
    }
}

