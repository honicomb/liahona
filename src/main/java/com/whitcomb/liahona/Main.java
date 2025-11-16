package com.whitcomb.liahona;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("FXML URL: " + getClass().getResource("/com/whitcomb/liahona/MainWindow.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/whitcomb/liahona/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Liahona");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}