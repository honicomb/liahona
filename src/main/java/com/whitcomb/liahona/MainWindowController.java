package com.whitcomb.liahona;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML private BorderPane borderPane;
    @FXML private AnchorPane contentArea;
    @FXML private Button goalsTopNavBtn;
    @FXML private Button tasksTopNavBtn;
    @FXML private Button healthTopNavBtn;
    @FXML private Button trackingTopNavBtn;
    @FXML private Button valuesTopNavBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goalsTopNavBtn.setOnAction(event -> {
            changeContentWindow("goals/fxml/goals.fxml");
        });
        tasksTopNavBtn.setOnAction(event -> {
            changeContentWindow("tasks/fxml/tasks.fxml");
        });
        healthTopNavBtn.setOnAction(event -> {
            changeContentWindow("health/fxml/vitals.fxml");
        });
        trackingTopNavBtn.setOnAction(event -> {
            changeContentWindow("tracking/fxml/carMaintenance.fxml");
        });
        valuesTopNavBtn.setOnAction(event -> {
           changeContentWindow("values/fxml/values.fxml");
        });
    }

    public void changeContentWindow(String fxmlpath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
            Node node = loader.load();

            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}