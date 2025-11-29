module com.banking {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.banking to javafx.fxml;
    opens com.banking.controller to javafx.fxml;
    opens com.banking.model to javafx.fxml;
    exports com.banking;
    exports com.banking.controller;
    exports com.banking.model;
    exports com.banking.service;
    exports com.banking.dao;
}