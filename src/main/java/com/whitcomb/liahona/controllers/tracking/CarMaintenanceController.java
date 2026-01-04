package com.whitcomb.liahona.controllers.tracking;

import com.whitcomb.liahona.models.tracking.*;
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
import javafx.util.converter.IntegerStringConverter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ResourceBundle;

public class CarMaintenanceController implements Initializable {

    @FXML private Button addMaintenanceButton;
    @FXML private DatePicker beginDatePicker;
    @FXML private TableView<CarMaintenanceEntry> carMaintenanceTableView;
    @FXML private Button clearFiltersButton;
    @FXML private TableColumn<CarMaintenanceEntry, Double> costTableCol;
    @FXML private TableColumn<CarMaintenanceEntry, LocalDate> dateTableCol;
    @FXML private DatePicker endDatePicker;
    @FXML private TableColumn<CarMaintenanceEntry, Integer> mileageTableCol;
    @FXML private TableColumn<CarMaintenanceEntry, String> providerTableCol;
    @FXML private TableColumn<CarMaintenanceEntry, String> serviceTableCol;
    @FXML private TableColumn<CarMaintenanceEntry, CarMaintenanceType > typeTableCol;
    @FXML private ComboBox<String> vehicleComboBox;
    @FXML private TableColumn<CarMaintenanceEntry, String> vehicleTableCol;

    private FilteredList<CarMaintenanceEntry> filteredEntries;
    private CarMaintenanceEntry editingEntry = null;
    private CarMaintenanceEntry originalEntry = null;

    ObservableList<String> allVehicles = FXCollections.observableArrayList();
    ObservableList<String> allServices = FXCollections.observableArrayList();
    ObservableList<String> allProviders = FXCollections.observableArrayList();
    ObservableList<CarMaintenanceEntry> serviceEntries = FXCollections.observableArrayList();
    ObservableList<CarMaintenanceType> typeValues = FXCollections.observableArrayList(CarMaintenanceType.values());

    PreparedStatement pre;
    ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAllServices();
        getAllProviders();

        //*** Don't want these buttons displayed until vehicle is selected or changes are being made to entry ***//
        addMaintenanceButton.setVisible(false);

        vehicleComboBox.setItems(getAllVehicleValues());
        vehicleComboBox.setOnAction(event -> {
           clearServiceEntriesInTable();
           String vehicleChosen = vehicleComboBox.getSelectionModel().getSelectedItem();
           getMaintenanceEntries(vehicleChosen);
           applyFilters();
           addMaintenanceButton.setVisible(true);
        });

        populateTableWithCarMaintenanceEntries();

        // Centralize edit start handling
        carMaintenanceTableView.editingCellProperty().addListener((observable, oldCell, newCell) -> {
            if (newCell != null) {
                CarMaintenanceEntry row = carMaintenanceTableView.getItems().get(newCell.getRow());
                startRowEdit(row);
            }
        });

        addMaintenanceButton.setOnAction(event -> {
            showAddMaintenanceDialog();
        });


        // Listeners for date filtering
        beginDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        clearFiltersButton.visibleProperty().bind(
                beginDatePicker.valueProperty().isNotNull()
                        .or(endDatePicker.valueProperty().isNotNull())
        );

        clearFiltersButton.disableProperty().bind(
                beginDatePicker.valueProperty().isNull()
                    .and(endDatePicker.valueProperty().isNull())
        );

        clearFiltersButton.setOnAction(e -> clearFilters());
    }

    // ******************** Getting data to populate in combo boxes and tableview *********//
    public ObservableList<String> getAllVehicleValues() {
        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement("SELECT model from vehicle_dim ORDER BY model");
            rs = pre.executeQuery();

            while (rs.next()) {
                allVehicles.add(rs.getString("model"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allVehicles;
    };

    public void getAllServices() {
        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement("SELECT service_desc FROM service_dim ORDER BY service_desc");
            rs = pre.executeQuery();

            while (rs.next()) {
                allServices.add(rs.getString("service_desc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllProviders() {
        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement("SELECT provider_name FROM service_provider_dim ORDER BY provider_name");
            rs = pre.executeQuery();

            while (rs.next()) {
                allProviders.add(rs.getString("provider_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getMaintenanceEntries(String vehicleModelChosen) {
        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            pre = conn.prepareStatement(
                    "SELECT c.service_entry_id, " +
                            "c.cost, " +
                            "c.mileage, " +
                            "c.service_date, " +
                            "c.service_id, " +
                            "s.service_desc, " +
                            "c.service_provider_id, " +
                            "p.provider_name, " +
                            "c.type, " +
                            "c.vehicle_id, " +
                            "v.model " +
                            "FROM car_maintenance_log c " +
                            "LEFT JOIN service_dim s ON c.service_id = s.service_id " +
                            "LEFT JOIN service_provider_dim p ON c.service_provider_id = p.service_provider_id " +
                            "LEFT JOIN vehicle_dim v ON c.vehicle_id = v.vehicle_id " +
                            "WHERE v.model = ?" +
                            "ORDER BY service_date" );
            pre.setString(1, vehicleModelChosen);
            rs = pre.executeQuery();

            while (rs.next()) {
                // Handle service date if null so no error when calling toLocalDate;
                Date sqlDate = rs.getDate("service_date" );
                LocalDate serviceDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                // Put type from database and put into CarMaintenanceType for model
                String typeString = rs.getString("type");
                CarMaintenanceType type = CarMaintenanceType.valueOf(typeString);

                CarMaintenanceEntry cme = new CarMaintenanceEntry(
                        rs.getDouble("cost" ),
                        rs.getInt("mileage" ),
                        serviceDate,
                        rs.getString("service_desc" ),
                        rs.getInt("service_entry_id"),
                        rs.getString("service_id" ),
                        rs.getInt("service_provider_id" ),
                        rs.getString("provider_name" ),
                        type,
                        rs.getString("vehicle_id" ),
                        rs.getString("model" )
                );
                serviceEntries.add(cme);
            }
        } catch (SQLException e) {
                e.printStackTrace();
            }
    }


    // ******************** Populating tableview *******************//
    public void populateTableWithCarMaintenanceEntries() {
        carMaintenanceTableView.setEditable(true);

        populateDateColumn();
        populateVehicleColumn();
        populateServiceColumn();
        populateMileageColumn();
        populateTypeColumn();
        populateCostColumn();
        populatePerformedByColumn();

        // Wrap list in FilteredList
        filteredEntries = new FilteredList<>(serviceEntries, p -> true);

        // Bind the FilteredList to the table
        carMaintenanceTableView.setItems(filteredEntries);
    }

    public void populateDateColumn() {
        dateTableCol.setEditable(true);
        dateTableCol.setCellValueFactory(cellData -> cellData.getValue().serviceDateProperty());
        dateTableCol.setCellFactory(column -> new TableCell<CarMaintenanceEntry, LocalDate>() {
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
                CarMaintenanceEntry rowItem = getTableRow() != null ? getTableRow().getItem() : null;
                if (rowItem == null) return;

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
                CarMaintenanceEntry entry = getTableRow().getItem();
                if (entry != null) {
                    entry.setServiceDate(newValue);
                    updateDatabase(entry);
                }
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

    public void populateVehicleColumn() {
        vehicleTableCol.setEditable(false);
        vehicleTableCol.setCellValueFactory(cellData -> cellData.getValue().vehicleModelProperty());
    }

    public void populateServiceColumn() {
        serviceTableCol.setEditable(true);
        serviceTableCol.setCellValueFactory(cellData -> cellData.getValue().serviceDescProperty());
        serviceTableCol.setCellFactory(ComboBoxTableCell.forTableColumn(allServices));
        serviceTableCol.setOnEditCommit(e -> {
            CarMaintenanceEntry entry = e.getRowValue();
            entry.setServiceDesc(e.getNewValue());
            entry.setServiceID(getServiceIdByDesc(entry.getServiceDesc()));
            updateDatabase(entry);
        });
     }

    public void populateMileageColumn() {
        mileageTableCol.setEditable(true);
        mileageTableCol.setCellValueFactory(cellData -> cellData.getValue().mileageProperty().asObject());

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();

        // Creating custom so that changes are committed when focus is lost - now just when Enter is pressed
        mileageTableCol.setCellFactory(column -> new TextFieldTableCell<CarMaintenanceEntry, Integer>(new IntegerStringConverter()) {
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

                textField.setAlignment(Pos.CENTER_RIGHT);

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
                    setAlignment(Pos.CENTER_RIGHT);
                }
            }

            @Override
            public void commitEdit(Integer newValue) {
                super.commitEdit(newValue);
                CarMaintenanceEntry entry = getTableRow().getItem();
                if (entry != null) {
                    entry.setMileage(newValue);
                    updateDatabase(entry);
                }
            }
        });
    }

    public void populateTypeColumn() {
        typeTableCol.setEditable(true);
        typeTableCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        typeTableCol.setCellFactory(column -> {
            ComboBoxTableCell<CarMaintenanceEntry, CarMaintenanceType> cell = new ComboBoxTableCell<>(typeValues);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        typeTableCol.setOnEditCommit(e -> {
            CarMaintenanceEntry entry = e.getRowValue();
            if (entry != null) {
                entry.setType(e.getNewValue());
                updateDatabase(entry);
            }
        });
    }

    public void populateCostColumn() {
        costTableCol.setEditable(true);
        costTableCol.setCellValueFactory(celldata -> celldata.getValue().costProperty().asObject());
        costTableCol.setCellFactory(column -> new TextFieldTableCell<CarMaintenanceEntry, Double>() {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();
                if (textField == null) {
                    createTextField();
                }
                setText(null);
                setGraphic(textField);
                textField.setText(getItem() != null ? String.format("$%.2f", getItem()) : "");
                textField.setAlignment(Pos.CENTER_RIGHT);
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? String.format("$%.2f", getItem()) : "");
                textField.setAlignment(Pos.CENTER_RIGHT);
                setGraphic(null);
            }

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField == null) {
                            createTextField();
                        }
                        textField.setText(item != null ? String.format("$%.2f", item) : "");
                        setText(null);
                        setGraphic(textField);
                        textField.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        setText(item != null ? String.format("$%.2f", item) : "");
                        setAlignment(Pos.CENTER_RIGHT);
                        setGraphic(null);
                    }
                }
            }

            private void createTextField() {
                textField = new TextField(getItem() != null ? String.format("%.2f", getItem()) : "");

                // Commit on <Enter>
                textField.setOnAction(event -> {
                    commitIfValid();
                });

                // Commit on focus loss
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitIfValid();
                    }
                });
            }

            public void commitIfValid() {
                try {
                    double parsedValue = Double.parseDouble(textField.getText());
                    double roundedValue = Math.round(parsedValue * 100.0) / 100.0;
                    commitEdit(roundedValue);
                } catch (NumberFormatException e) {
                    cancelEdit(); // Or show a validation error
                    }
                }

            public void commitEdit(Double newValue) {
                super.commitEdit(newValue);
                CarMaintenanceEntry entry = getTableRow().getItem();
                if (entry != null) {
                    entry.setCost(newValue);
                    updateDatabase(entry);
                }
            }
        });
    }

    public void populatePerformedByColumn() {
        providerTableCol.setEditable(true);
        providerTableCol.setCellValueFactory(cellData -> cellData.getValue().serviceProviderNameProperty());

        providerTableCol.setCellFactory(column -> {
            ComboBoxTableCell<CarMaintenanceEntry, String> cell = new ComboBoxTableCell<>(allProviders);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        providerTableCol.setOnEditCommit(e -> {
            CarMaintenanceEntry entry = e.getRowValue();
            if (entry != null) {
                entry.setServiceProviderName(e.getNewValue());
                entry.setServiceProviderID(getProviderIdByName(entry.getServiceProviderName()));
                updateDatabase(entry);
            }
        });

    }


    //******************** Editing, adding, and filtering functions ************//
    public void updateDatabase(CarMaintenanceEntry entry) {
        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement(
                    "UPDATE car_maintenance_log SET "   +
                            "cost = ?, "                +
                            "mileage = ?, "             +
                            "service_date = ?, "        +
                            "service_id = ?, "          +
                            "service_provider_id = ?, " +
                            "type = ? "                 +
                            "WHERE service_entry_id = ?"
            );

            // New updated values
            pre.setDouble(1, entry.getCost());
            pre.setInt(2, entry.getMileage());
            pre.setDate(3, entry.getServiceDate() != null ? Date.valueOf(entry.getServiceDate()) : null);
            pre.setString(4, entry.getServiceID());
            pre.setInt(5, entry.getServiceProviderID());
            pre.setString(6, entry.getType().name());
            pre.setInt(7, entry.getServiceEntryID());

            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startRowEdit(CarMaintenanceEntry row) {
        if (editingEntry == null) {
            editingEntry = row;
            originalEntry = new CarMaintenanceEntry(
                    row.getCost(),
                    row.getMileage(),
                    row.getServiceDate(),
                    row.getServiceDesc(),
                    row.getServiceEntryID(),
                    row.getServiceID(),
                    row.getServiceProviderID(),
                    row.getServiceProviderName(),
                    row.getType(),
                    row.getVehicleID(),
                    row.getVehicleModel()
            );
            return;
        }

        // Already editing another row then force to stay on same row
        if (editingEntry != row) {
            carMaintenanceTableView.getSelectionModel().select(editingEntry);
        }
    }

    private String getServiceIdByDesc(String desc) {
        try {
            Connection conn = new DBHandlerTracking().getConnection();
            PreparedStatement pre = conn.prepareStatement("SELECT service_id FROM service_dim WHERE service_desc = ?");
            pre.setString(1, desc);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) return rs.getString("service_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getProviderIdByName(String name) {
        try {
            Connection conn = new DBHandlerTracking().getConnection();
            PreparedStatement pre = conn.prepareStatement("SELECT service_provider_id FROM service_provider_dim WHERE provider_name = ?");
            pre.setString(1, name);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void clearServiceEntriesInTable() {
        serviceEntries.clear();
    }

    public void showAddMaintenanceDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/whitcomb/liahona/tracking/fxml/addMaintenance.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Maintenance");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(root));

            // Pass dialog reference to controller so it can close it
            AddMaintenanceController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Pass in the vehicle name of selected vehicle
            if (vehicleComboBox.getValue() != null) {
                controller.setVehicleSelected(vehicleComboBox.getValue());
                controller.setAllServicesList(allServices);
                controller.setPerformedByList(allProviders);
                controller.setTypeList(typeValues);
            }

            dialogStage.showAndWait();

            //Refresh the list to reflect database changes
            clearServiceEntriesInTable();
            getMaintenanceEntries(vehicleComboBox.getValue());
            carMaintenanceTableView.refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        LocalDate start = beginDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        filteredEntries.setPredicate(entry -> {
            LocalDate date = entry.getServiceDate();

            // If no filters are selected, show everything (including null dates)
            if (start == null && end == null) {
                return true;
            }

            // If the entry has no date, exclude it
            if (date == null) return false;

            if (start != null && date.isBefore(start)) return false;
            if (end != null && date.isAfter(end)) return false;

            return true;
        });
    }

    private void clearFilters() {
        beginDatePicker.setValue(null);
        endDatePicker.setValue(null);
        applyFilters();

        filteredEntries.setPredicate(e -> true);
    }
}

