package com.whitcomb.liahona.controllers.goals;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.whitcomb.liahona.models.goals.DBHandlerGoals;
import com.whitcomb.liahona.models.goals.Milestone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GoalsController implements Initializable {

    @FXML private Button addButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private TableColumn<Milestone, LocalDate> dueDateTableCol;
    @FXML private TextField goalDescTextField;
    @FXML private ChoiceBox<String> goalSelectorComboBox;
    @FXML private TextField howTextField;
    @FXML private TableColumn<Milestone, String> milestoneIDTableCol;
    @FXML private TableColumn<Milestone, String> milestoneTableCol;
    @FXML private TableView<Milestone> milestoneTableView;
    @FXML private TableColumn<Milestone, String> statusTableCol;
    @FXML private Button saveButton;
    @FXML private TextField valueTextField;
    @FXML private ResourceBundle resources;
    @FXML private URL location;

    private String goalIDofSelectedGoal;
    private Milestone editingMilestone = null;
    private Milestone originalMilestoneCopy = null;

    ObservableList<Milestone> milestones = FXCollections.observableArrayList();
    ObservableList<String> allPossibleStatuses = FXCollections.observableArrayList();

    PreparedStatement pre;
    ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goalSelectorComboBox.setItems(getGoalDropDownChoices());
        allPossibleStatuses = getAllPossibleStatusValues();

        goalSelectorComboBox.setOnAction(event -> {
            String goalChosen = goalSelectorComboBox.getValue();
            getValueHowAndDescAssociatedToGoal(goalChosen);
            clearMilestonesInTable();
            getMilestones(goalChosen);
        });

        populateTableWithMilestones();
        setupEditingBehavior();

        saveButton.setOnAction(e -> {
            if (editingMilestone != null) {
                saveMilestoneChangesToDatabase(editingMilestone);
                resetUI();
            }
        });

        cancelButton.setOnAction(e -> {
            if (editingMilestone != null && originalMilestoneCopy != null) {
                editingMilestone.setDueDate(originalMilestoneCopy.getDueDate());
                editingMilestone.setMilestoneDesc(originalMilestoneCopy.getMilestoneDesc());
                editingMilestone.setStatus(originalMilestoneCopy.getStatus());
            }
            resetUI();
        });

        addButton.setOnAction(e -> {
            showMilestoneAddDialog();
            resetUI();
        });

        deleteButton.setOnAction(e -> {
            showMilestoneDeleteDialog();
            resetUI();
        });

        //Centralize edit start handling
        milestoneTableCol.setOnEditStart(e -> beginEditing(e.getRowValue()));
        dueDateTableCol.setOnEditStart(e -> beginEditing(e.getRowValue()));
        statusTableCol.setOnEditStart(e -> beginEditing(e.getRowValue()));

        // Hide Add, Save, Delete, and Cancel buttons on initial load of page
        addButton.setVisible(false);
        saveButton.setVisible(false);
        deleteButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    public ObservableList<String> getGoalDropDownChoices() {
        ObservableList<String> goalsDropDownChoices = FXCollections.observableArrayList();
        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement("SELECT short_desc FROM goal ORDER BY short_desc");
            rs = pre.executeQuery();

            while (rs.next()) {
                goalsDropDownChoices.add(rs.getString("short_desc"));
            }
            return goalsDropDownChoices;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<String> getAllPossibleStatusValues() {
        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement("SELECT status_name from status_dim ORDER BY status_name");
            rs = pre.executeQuery();

            while (rs.next()) {
                allPossibleStatuses.add(rs.getString("status_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allPossibleStatuses;
    };

    public void getValueHowAndDescAssociatedToGoal(String goalChosen) {
        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement(
                        "SELECT  g.value_id, "   +
                                "v.value_name, " +
                                "h.how_desc, "   +
                                "g.goal_desc, "  +
                                "g.goal_id "     +
                        "FROM goal g " +
                        "JOIN value v ON g.value_id = v.value_id " +
                        "JOIN how h ON g.how_id = h.how_id " +
                        "WHERE g.short_desc = ?");
            pre.setString(1, goalChosen);
            rs = pre.executeQuery();

            if (rs.next()) {
                goalIDofSelectedGoal = rs.getString("goal_id");
                valueTextField.setText(rs.getString("value_name"));
                howTextField.setText(rs.getString("how_desc"));
                goalDescTextField.setText(rs.getString("goal_desc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearMilestonesInTable() {
        milestones.clear();
    }

    public void getMilestones(String goalChosen) {
        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement(
                    "SELECT  m.due_date, "       +
                            "m.goal_id, "        +
                            "m.milestone_desc, " +
                            "m.milestone_id, "    +
                            "s.status_name "     +
                            "FROM milestone m "  +
                            "JOIN status_dim s ON m.status_id = s.status_id " +
                            "JOIN goal g on g.goal_id = m.goal_id " +
                            "WHERE g.short_desc = ?" +
                            "ORDER BY m.due_date");
            pre.setString(1, goalChosen);
            rs = pre.executeQuery();
            while (rs.next()) {
                // Handle due date if null so no error when calling toLocalDate()
                Date sqlDate = rs.getDate("due_date");
                LocalDate dueDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                Milestone ms = new Milestone(
                        dueDate,
                        rs.getString("goal_id"),
                        rs.getString("milestone_id"),
                        rs.getString("milestone_desc"),
                        rs.getString("status_name")
                );
                milestones.add(ms);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Saving Goal ID of selected goal for use in populating milestone table w/updates
        if (!milestones.isEmpty()) {
            deleteButton.setVisible(true);
            deleteButton.setDisable(false);       // Enable if milestones exist
        } else {
            deleteButton.setVisible(false);       // Hide if no milestones
        }

        // Add button: always visible/enabled once goal is selected
        addButton.setVisible(true);
        addButton.setDisable(false);
    }

    public void populateTableWithMilestones() {
        milestoneTableView.setEditable(true);
        populateMilestoneIDColumn();
        populateMilestoneDescriptionColumn();
        populateDueDateColumn();
        populateStatusColumn();
        milestoneTableView.setItems(milestones);
    }

    public void populateMilestoneIDColumn() {
        milestoneIDTableCol.setEditable(false);
        milestoneIDTableCol.setCellValueFactory(cellData -> cellData.getValue().milestoneIDProperty());
    }

    public void populateMilestoneDescriptionColumn() {
        // --- Milestone Description (editable TextField)
        milestoneTableCol.setEditable(true);
        milestoneTableCol.setCellValueFactory(cellData -> cellData.getValue().milestoneDescProperty());

        // Creating custom so that changes are committed when focus is lost - not just when Enter is pressed
        milestoneTableCol.setCellFactory(column -> new TextFieldTableCell<Milestone, String>() {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();
                if (textField == null) {
                    createTextField();
                }
                setText(null);
                setGraphic(textField);
                textField.setText(getItem());
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField == null) {
                            createTextField();
                        }
                        textField.setText(getItem());
                        setText(null);
                        setGraphic(textField);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            }

            private void createTextField() {
                textField = new TextField(getItem());

                //Commit on <Enter>
                textField.setOnAction(event -> {
                    commitEdit(textField.getText());
                });

                //Commit on focus loss
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitEdit(textField.getText());
                    }
                });
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                if (getTableRow() != null && getTableRow().getItem() != null) {
                    getTableRow().getItem().setMilestoneDesc(newValue);
                }
            }
        });
    }

    public void populateDueDateColumn() {
        // --- Due Date (editable DatePicker)
        dueDateTableCol.setEditable(true);
        dueDateTableCol.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
        dueDateTableCol.setCellFactory(column -> new TableCell<Milestone, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(e -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                    commitEdit(datePicker.getValue());
                    }
                });

                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        commitEdit(datePicker.getValue());
                    }
                });
            }

            @Override
            public void startEdit() {
                Milestone rowItem = getTableRow() != null ? getTableRow().getItem() : null;

                if (rowItem == null) return;

                if (editingMilestone != null && !editingMilestone.equals(rowItem)) {
                    milestoneTableView.edit(-1, null);
                    return;
                }

                super.startEdit();
                datePicker.setValue(getItem());
                setGraphic(datePicker);
                setText(null);
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? getItem().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy")) : "");
                setGraphic(null);
            }

            public void commitEdit(LocalDate newValue) {
                super.commitEdit(newValue);
                getTableView().getItems().get(getIndex()).setDueDate(newValue);
                cancelEdit();
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        datePicker.setValue(getItem());
                        setGraphic(datePicker);
                        setText(null);
                    } else {
                        setText(item.format(java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy")));
                        setGraphic(null);
                    }
                }
            }
        });
    }

    public void populateStatusColumn() {
        statusTableCol.setEditable(true);
        statusTableCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusTableCol.setCellFactory(ComboBoxTableCell.forTableColumn(allPossibleStatuses));
        statusTableCol.setOnEditCommit(e -> e.getRowValue().setStatus(e.getNewValue()));
    }

    private void setupEditingBehavior() {
        // Handle edit start: any column
        milestoneTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            public void updateItem(Milestone item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && editingMilestone != null && item != editingMilestone) {
                    setDisable(true);
                    setStyle("-fx-opacity: 0.5;");
                } else {
                    setDisable(false);
                    setStyle("");
                }
            }
        });
    }

    private void beginEditing(Milestone row) {

        if (editingMilestone == null) {
            editingMilestone = row;
            originalMilestoneCopy = new Milestone(
                    row.getDueDate(),
                    row.getGoalID(),
                    row.getMilestoneID(),
                    row.getMilestoneDesc(),
                    row.getStatus()
            );

            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            addButton.setDisable(true);
            deleteButton.setDisable(true);
            milestoneTableView.refresh();
        } else if (!editingMilestone.equals(row)) {
            milestoneTableView.getSelectionModel().clearSelection();
            milestoneTableView.edit(-1, null);
        }
    }

    public void saveMilestoneChangesToDatabase(Milestone m) {
        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement(
                    "UPDATE milestone m "           +
                        "JOIN status_dim s on s.status_name = ? " +
                        "SET m.status_id = s.status_id, " +
                            "m.due_date = ?, "      +
                            "m.milestone_desc = ?, " +
                            "m.status_date = ?" +
                            "WHERE m.milestone_id = ?");
            pre.setString(1, m.getStatus());
            pre.setString(2, String.valueOf(Date.valueOf(m.getDueDate())));
            pre.setString(3, m.getMilestoneDesc());
            pre.setString(4, String.valueOf(Date.valueOf(LocalDate.now())));
            pre.setString(5, m.getMilestoneID());
            pre.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void resetUI() {
        editingMilestone = null;
        originalMilestoneCopy = null;
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        addButton.setDisable(false);
        if (!milestones.isEmpty()) deleteButton.setDisable(false);
        milestoneTableView.edit(-1, null);
        setupEditingBehavior();
        milestoneTableView.refresh();
    }

    public void showMilestoneDeleteDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/whitcomb/liahona/goals/fxml/deleteMilestone.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Delete Milestone");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(root));

            // Pass dialog reference to controller so it can close it
            DeleteMilestoneController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Pass in the Goal ID of selected goal
            controller.setSelectedGoalID(goalIDofSelectedGoal);
            controller.loadMilestonesFromDatabase();

            dialogStage.showAndWait();

            if (controller.isDeleteConfirmed()) {
                String milestoneIdToDelete = controller.getSelectedMilestoneID();

                //Refresh the list to reflect database changes
                clearMilestonesInTable();
                getMilestones(goalSelectorComboBox.getValue());
                milestoneTableView.refresh();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMilestoneAddDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/whitcomb/liahona/goals/fxml/addMilestone.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Milestone");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(root));

            // Pass dialog reference to controller so it can close it
            AddMilestoneController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Pass in the Goal ID of selected goal
            controller.setSelectedGoalID(goalIDofSelectedGoal);

            dialogStage.showAndWait();

            //Refresh the list to reflect database changes
            clearMilestonesInTable();
            getMilestones(goalSelectorComboBox.getValue());
            milestoneTableView.refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

