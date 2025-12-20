package com.whitcomb.liahona.controllers.health;

import com.whitcomb.liahona.models.health.*;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class VitalsController implements Initializable {

    // FXML & Java attributes for entire page
    @FXML private Button addFoodButton;
    @FXML private Button mealPlansButton;
    @FXML private Button reportsButton;
    @FXML private Button vitalsButton;

    // FXML & Java attributes for all Vitals views
    @FXML private DatePicker beginDatePicker;
    @FXML private Button dailyViewButton;
    @FXML private Button printButton;
    @FXML private Label tableHeaderText;
    @FXML private Button weeklyViewButton;
    @FXML private ResourceBundle resources;
    @FXML private URL location;

    PreparedStatement pre;
    ResultSet rs;


    // FXML & Java attributes for Vitals - daily views
    @FXML private Button addButton;
    @FXML private TableColumn<DailyHealthEntry, Integer> bloodSugarTableCol;
    @FXML private TableColumn<DailyHealthEntry, LocalDate> dateTableCol;
    @FXML private TableColumn<DailyHealthEntry, String> medsTableCol;
    @FXML private TableColumn<DailyHealthEntry, Integer> stepsTableCol;
    @FXML private TableView<DailyHealthEntry> vitalsTableView;
    @FXML private TableColumn<DailyHealthEntry, Double> weightTableCol;

    private DailyHealthEntry editingEntry = null;
    private FilteredList<DailyHealthEntry> filteredDailyEntries;
    private ObservableList<DailyHealthEntry> healthDailyEntries = FXCollections.observableArrayList();
    private DailyHealthEntry originalEntry = null;
    private ObservableList<String> yesOrNo  = FXCollections.observableArrayList("Y","N");


    // FXML & Java attributes for Vitals - weekly views
    @FXML private TableColumn<WeeklyHealthEntry, Integer> bloodSugarAvgTableCol;
    private FilteredList<WeeklyHealthEntry> filteredWeeklyEntries;
    private ObservableList<WeeklyHealthEntry> healthWeeklyEntries = FXCollections.observableArrayList();
    @FXML private TableColumn<WeeklyHealthEntry, Integer> medsCntTableCol;
    @FXML private TableColumn<WeeklyHealthEntry, Integer> stepsAvgTableCol;
    @FXML private TableColumn<WeeklyHealthEntry, Double> weightAvgTableCol;
    @FXML private TableView<WeeklyHealthEntry> weeklyVitalsTableView;
    @FXML private TableColumn<WeeklyHealthEntry, LocalDate> weekStartTableCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Force default view
        weeklyVitalsTableView.setVisible(false);
        weeklyVitalsTableView.setManaged(false);

        vitalsTableView.setVisible(true);
        vitalsTableView.setManaged(true);

        tableHeaderText.setText("Vitals - Daily");

        getDailyHealthEntries();
        populateTableWithDailyHealthEntries();

        // Create filtered lists
        filteredDailyEntries = new FilteredList<>(healthDailyEntries, p -> true);
        vitalsTableView.setItems(filteredDailyEntries);

        filteredWeeklyEntries = new FilteredList<>(healthWeeklyEntries, p -> true);
        weeklyVitalsTableView.setItems(filteredWeeklyEntries);


        // Buttons in top right behaviors
        dailyViewButton.setOnAction(e -> {
            tableHeaderText.setText("Vitals - Daily");

            vitalsTableView.setVisible(true);
            vitalsTableView.setManaged(true);

            weeklyVitalsTableView.setVisible(false);
            weeklyVitalsTableView.setManaged(false);

            addButton.setVisible(true);
        });

        weeklyViewButton.setOnAction(e -> {
            // Clearing entries so we don't get a duplicate set of data added to the table
            tableHeaderText.setText("Vitals - Weekly");

            clearWeeklyHeathTableEntries();
            getWeeklyHealthEntries();

            applyFilters(filteredWeeklyEntries, WeeklyHealthEntry::getBeginDate);

            populateTableWithWeeklyHealthEntries();

            vitalsTableView.setVisible(false);
            vitalsTableView.setManaged(false);

            weeklyVitalsTableView.setVisible(true);
            weeklyVitalsTableView.setManaged(true);

            addButton.setVisible(false);
        });

        // Centralize edit start handling
        vitalsTableView.editingCellProperty().addListener((observable, oldCell, newCell) -> {
            if (newCell != null) {
                DailyHealthEntry row = vitalsTableView.getItems().get(newCell.getRow());
                startRowEdit(row);
            }
        }
    );

        addButton.setOnAction(e -> showAddHealthEntryDialog());

        // Listeners for date filtering
        beginDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                applyFilters(filteredDailyEntries, DailyHealthEntry::getBeginDate);
                applyFilters(filteredWeeklyEntries, WeeklyHealthEntry::getBeginDate);
        });
    }

    //******************** Getting data to populate in combo boxes and tableview (Daily view) *********//
    public void getDailyHealthEntries() {
        try {
            DBHandlerHealth dbh = new DBHandlerHealth();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement("SELECT * FROM vitals");
            rs = pre.executeQuery();

            while (rs.next()) {
                // Handle service date if null so no error when calling toLocalDate;
                Date sqlDate = rs.getDate("date" );
                LocalDate beginDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                DailyHealthEntry dhe = new DailyHealthEntry(
                        beginDate,
                        rs.getInt("glucose" ),
                        rs.getString("meds"),
                        rs.getInt("steps" ),
                        rs.getDouble("weight" )
                );
                healthDailyEntries.add(dhe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //******************** Populating tableview (Daily View) **********//
    public void populateTableWithDailyHealthEntries() {
        vitalsTableView.setEditable(true);

        populateDateColumn();
        populateWeightColumn();
        populateBloodSugarColumn();
        populateStepsColumn();
        populateMedsColumn();
    }

    public void populateDateColumn() {
        dateTableCol.setEditable(true);
        dateTableCol.setCellValueFactory(cellData -> cellData.getValue().beginDateProperty());
        dateTableCol.setCellFactory(column -> new TableCell<DailyHealthEntry, LocalDate>() {
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
                DailyHealthEntry rowItem = getTableRow() != null ? getTableRow().getItem() : null;

                if (rowItem == null) return;

                if (editingEntry != null && !editingEntry.equals(rowItem)) {
                    vitalsTableView.edit(-1, null);
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
                getTableView().getItems().get(getIndex()).setBeginDate(newValue);
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

    public void populateWeightColumn() {
        weightTableCol.setEditable(true);
        weightTableCol.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());

        NumberFormat numberFormat = new DecimalFormat("#0.0");

        // Creating custom so that changes are committed when focus is lost - now just when Enter is pressed
        weightTableCol.setCellFactory(column -> new TextFieldTableCell<DailyHealthEntry, Double>(new DoubleStringConverter()) {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();

                if (textField == null) {
                    textField = new TextField();
                }

                // Commit on enter
                textField.setOnAction(e -> commitIfValid());

                // Commit on focus lost
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitIfValid();
                    }
                });

                textField.setAlignment(Pos.CENTER);

                if (getItem() != null) {
                    textField.setText(numberFormat.format(getItem()));
                } else {
                    textField.setText("");
                }

                setText(null);
                setGraphic(textField);
                textField.requestFocus();
                textField.selectAll();
            }

            private void commitIfValid() {
                try {
                    Double value = Double.parseDouble(textField.getText());
                    commitEdit(value);
                } catch (NumberFormatException e) {
                    cancelEdit();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? numberFormat.format(getItem()) : "");
                setGraphic(null);
            }

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    if (textField != null) {
                        textField.setText(item != null ? numberFormat.format(item) : "");
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(item != null ? numberFormat.format(item) : "");
                    setGraphic(null);
                    setAlignment(Pos.CENTER);
                }
            }

            @Override
            public void commitEdit(Double newValue) {
                super.commitEdit(newValue);
                DailyHealthEntry entry = getTableRow().getItem();
                if (getTableRow() != null && getTableRow().getItem() != null) {
                    getTableRow().getItem().setWeight(newValue);
                    updateDatabase(entry);
                }
            }
        });
    }

    public void populateBloodSugarColumn() {
        bloodSugarTableCol.setEditable(true);
        bloodSugarTableCol.setCellValueFactory(cellData -> cellData.getValue().glucoseProperty().asObject());

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();

        // Creating custom so that changes are committed when focus is lost - now just when Enter is pressed
        bloodSugarTableCol.setCellFactory(column -> new TextFieldTableCell<DailyHealthEntry, Integer>(new IntegerStringConverter()) {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();

                if (textField == null) {
                    textField = new TextField();
                }

                // Commit on enter
                textField.setOnAction(e -> commitIfValid());

                // Commit on focus lost
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitIfValid();
                    }
                });

                textField.setAlignment(Pos.CENTER);

                if (getItem() != null) {
                    textField.setText(getItem().toString());
                } else {
                    textField.setText("");
                }

                setText(null);
                setGraphic(textField);
                textField.requestFocus();
                textField.selectAll();
            }

            private void commitIfValid() {
                try {
                    int value = Integer.parseInt(textField.getText());
                    commitEdit(value);
                } catch (NumberFormatException e) {
                    cancelEdit();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? numberFormat.format(getItem()) : "");
                setGraphic(null);
            }

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    if (textField != null) {
                        textField.setText(item != null ? numberFormat.format(getItem()) : "");
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(item != null ? numberFormat.format(getItem()) : "");
                    setGraphic(null);
                    setAlignment(Pos.CENTER);
                }
            }

            @Override
            public void commitEdit(Integer newValue) {
                super.commitEdit(newValue);
                DailyHealthEntry entry = getTableRow().getItem();
                if (getTableRow() != null && getTableRow().getItem() != null) {
                    getTableRow().getItem().setGlucose(newValue);
                    updateDatabase(entry);
                }
            }
        });
    }

    public void populateStepsColumn() {
        stepsTableCol.setEditable(true);
        stepsTableCol.setCellValueFactory(cellData -> cellData.getValue().stepsProperty().asObject());

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();

        // Creating custom so that changes are committed when focus is lost - now just when Enter is pressed
        stepsTableCol.setCellFactory(column -> new TextFieldTableCell<DailyHealthEntry, Integer>(new IntegerStringConverter()) {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();

                if (textField == null) {
                    textField = new TextField();
                }

                // Commit on enter
                textField.setOnAction(e -> commitIfValid());

                // Commit on focus lost
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitIfValid();
                    }
                });

                textField.setAlignment(Pos.CENTER);

                if (getItem() != null) {
                    textField.setText(getItem().toString());
                } else {
                    textField.setText("");
                }

                setText(null);
                setGraphic(textField);
                textField.requestFocus();
                textField.selectAll();
            }

            private void commitIfValid() {
                try {
                    int value = Integer.parseInt(textField.getText());
                    commitEdit(value);
                } catch (NumberFormatException e) {
                    cancelEdit();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? numberFormat.format(getItem()) : "");
                setGraphic(null);
            }

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    if (textField != null) {
                        textField.setText(item != null ? numberFormat.format(getItem()) : "");
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(item != null ? numberFormat.format(getItem()) : "");
                    setGraphic(null);
                    setAlignment(Pos.CENTER);
                }
            }

            @Override
            public void commitEdit(Integer newValue) {
                super.commitEdit(newValue);
                DailyHealthEntry entry = getTableRow().getItem();
                if (getTableRow() != null && getTableRow().getItem() != null) {
                    getTableRow().getItem().setSteps(newValue);
                    updateDatabase(entry);
                }
            }
        });
    }

    public void populateMedsColumn() {
        medsTableCol.setEditable(true);
        medsTableCol.setCellValueFactory(cellData -> cellData.getValue().medsProperty());

        medsTableCol.setCellFactory(column -> {
            ComboBoxTableCell<DailyHealthEntry, String> cell = new ComboBoxTableCell<>(yesOrNo);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        medsTableCol.setOnEditCommit(e -> {
            DailyHealthEntry entry = e.getRowValue();
            if (entry != null) {
                entry.setMeds(e.getNewValue());
                updateDatabase(entry);
            }
        });
    }


    //******************** Editing and adding functions (Daily view only) **********//
    public void updateDatabase(DailyHealthEntry entry) {
        try {
            DBHandlerHealth dbh = new DBHandlerHealth();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement(
                    "UPDATE vitals SET "    +
                            "glucose = ?, " +
                            "meds = ?, "    +
                            "steps = ?, "   +
                            "weight = ? "   +
                            "WHERE date = ?"
            );

            // New updated values
            pre.setInt(1, entry.getGlucose());
            pre.setString(2,entry.getMeds());
            pre.setInt(3, entry.getSteps());
            pre.setDouble(4, entry.getWeight());
            pre.setDate(5, Date.valueOf(entry.getBeginDate()));

            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startRowEdit(DailyHealthEntry row) {
        if (editingEntry == null) {
            editingEntry = row;
            originalEntry = new DailyHealthEntry(
                    row.getBeginDate(),
                    row.getGlucose(),
                    row.getMeds(),
                    row.getSteps(),
                    row.getWeight()
            );
            return;
        }

        // Already editing another row then force to stay on same row
        if (editingEntry != row) {
            vitalsTableView.getSelectionModel().select(editingEntry);
        }
    }

    public void showAddHealthEntryDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/whitcomb/liahona/health/fxml/addHealthEntry.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Health Entry");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(root));

            // Pass dialog reference to controller so it can close it
            AddHealthEntryController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setYesNoComboBox(yesOrNo);

            dialogStage.showAndWait();

            //Refresh the list to reflect database changes
            clearHealthEntriesInTable();
            getDailyHealthEntries();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearHealthEntriesInTable() {
        healthDailyEntries.clear();
    }

    //******************** Getting data to populate in combo boxes and tableview (Weekly view) *********//
    public void getWeeklyHealthEntries() {
        try {
            DBHandlerHealth dbh = new DBHandlerHealth();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement(
                    "SELECT "                                                                      +
                            "DATE_SUB(DATE(`date`), INTERVAL WEEKDAY(`date`) DAY) AS week_start, " +
                            "ROUND(AVG(glucose)) AS glucose_avg, "                                 +
                            "ROUND(AVG(steps)) as steps_avg, "                                     +
                            "AVG(weight) as weight_avg, "                                          +
                            "SUM(CASE WHEN meds = 'Y' THEN 1 ELSE 0 END) as meds_cnt "             +
                            "FROM vitals "                                                         +
                            "GROUP BY week_start "                                                 +
                            "ORDER BY week_start");
            rs = pre.executeQuery();

            while (rs.next()) {
                WeeklyHealthEntry dhe = new WeeklyHealthEntry(
                        rs.getDate("week_start").toLocalDate(),
                        rs.getInt("glucose_avg" ),
                        rs.getInt("meds_cnt"),
                        rs.getInt("steps_avg" ),
                        rs.getDouble("weight_avg" )
                );
                healthWeeklyEntries.add(dhe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //******************** Populating tableview (Daily View) **********//
    public void populateTableWithWeeklyHealthEntries() {
        weeklyVitalsTableView.setEditable(false);

        populateWeekColumn();
        populateAvgWeightColumn();
        populateAvgGlucoseColumn();
        populateAvgStepsColumn();
        populateCntMedsColumn();
    }

    public void populateWeekColumn() {
        weekStartTableCol.setEditable(false);
        weekStartTableCol.setCellValueFactory(cellData -> cellData.getValue().beginDateProperty());

        weekStartTableCol.setCellFactory(col -> new TableCell<WeeklyHealthEntry, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormat.format(item));
                }
            }
        });
    }

    public void populateAvgWeightColumn() {
        weightAvgTableCol.setEditable(false);
        weightAvgTableCol.setCellValueFactory(cellData -> cellData.getValue().weightAvgProperty().asObject());

        DecimalFormat format = new DecimalFormat("#.#");

        weightAvgTableCol.setCellFactory(col -> new TableCell<WeeklyHealthEntry, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item.doubleValue()));
                }

                setAlignment(Pos.CENTER);
            }
        });
    }

    public void populateAvgGlucoseColumn() {
        bloodSugarAvgTableCol.setEditable(false);
        bloodSugarAvgTableCol.setCellValueFactory(cellData -> cellData.getValue().glucoseAvgProperty().asObject());
        bloodSugarAvgTableCol.setStyle("-fx-alignment: CENTER;");
    }

    public void populateAvgStepsColumn() {
        stepsAvgTableCol.setEditable(false);
        stepsAvgTableCol.setCellValueFactory(cellData -> cellData.getValue().stepsAvgProperty().asObject());

        DecimalFormat format = new DecimalFormat("#,##0");

        stepsAvgTableCol.setCellFactory(col -> new TableCell<WeeklyHealthEntry, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item.doubleValue()));
                }

                setAlignment(Pos.CENTER);
            }
        });

    }

    public void populateCntMedsColumn() {
        medsCntTableCol.setEditable(false);
        medsCntTableCol.setCellValueFactory(cellData -> cellData.getValue().medsCntProperty().asObject());
        medsCntTableCol.setStyle("-fx-alignment: CENTER;");
    }

    public void clearWeeklyHeathTableEntries() {
        healthWeeklyEntries.clear();
    }

    //******************** Filtering functions **********//

    public <T> void applyFilters(
            FilteredList<T> entries,
            Function<T, LocalDate> beginDateExtractor) {

        LocalDate start = beginDatePicker.getValue();

        entries.setPredicate(entry -> {
            LocalDate date = beginDateExtractor.apply(entry);

            if (start != null && date.isBefore(start)) return false;
            else return true;
        });
    }

}
