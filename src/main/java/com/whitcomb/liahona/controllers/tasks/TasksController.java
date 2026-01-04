package com.whitcomb.liahona.controllers.tasks;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class TasksController implements Initializable {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private TableColumn<?, ?> actualTimeTableCol;
    @FXML private ImageView addTaskButton;
    @FXML private ImageView cancelNotesEditButton;
    @FXML private TableColumn<?, ?> categoryTableCol;
    @FXML private ImageView dailyTaskViewButton;
    @FXML private Label dateLabel;
    @FXML private ImageView dateLeftButton;
    @FXML private ImageView dateRightButton;
    @FXML private TableColumn<?, ?> descTableCol;
    @FXML private TableColumn<?, ?> dueDateTableCol;
    @FXML private ImageView editNotesButton;
    @FXML private TableColumn<?, ?> estTimeTableCol;
    @FXML private TableColumn<?, ?> goalIDTableCol;
    @FXML private Button mtlButton;
    @FXML private TextArea notesTextArea;
    @FXML private TableColumn<?, ?> orderTableCol;
    @FXML private ImageView printButton;
    @FXML private TableColumn<?, ?> priorityTableCol;
    @FXML private TableColumn<?, ?> recurringTableCol;
    @FXML private ImageView saveNotesButton;
    @FXML private TableColumn<?, ?> startDateTableCol;
    @FXML private TableColumn<?, ?> statusTableCol;
    @FXML private TableColumn<?, ?> taskIDTableCol;
    @FXML private TableView<?> tasksTableView;
    @FXML private ImageView weeklyTaskViewButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

