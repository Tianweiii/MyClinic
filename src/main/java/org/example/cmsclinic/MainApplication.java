package org.example.cmsclinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Filing.FileIO;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("adminMain.fxml"));
//        FXMLLoader parent = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
//        Scene scene = new Scene(parent.load());
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        stage.setTitle("Clinic Management System");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}