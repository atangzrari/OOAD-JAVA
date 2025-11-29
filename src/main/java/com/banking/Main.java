package com.banking;

import com.banking.dao.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Connect to existing SQLite database
        DatabaseConnection.initializeDatabase();

        // Load main login screen
        Parent root = FXMLLoader.load(getClass().getResource("/com/banking/view/main-login.fxml"));
        primaryStage.setTitle("WorldBank");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}