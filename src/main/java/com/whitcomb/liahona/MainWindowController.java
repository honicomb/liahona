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
    @FXML
    private Button valuesTopNavBtn;
    @FXML
    private AnchorPane contentArea;
    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        valuesTopNavBtn.setOnAction(event -> {
           changeContentWindow("values/values.fxml");
        });
    }

    public void changeContentWindow(String fxmlpath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlpath));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}