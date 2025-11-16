package com.whitcomb.liahona.controllers.tracking;

import com.whitcomb.liahona.models.tracking.CarMaintenanceType;
import com.whitcomb.liahona.models.tracking.DBHandlerTracking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMaintenanceController implements Initializable {

    @FXML private ComboBox<String> serviceSelectComboBox;
    @FXML private ComboBox<CarMaintenanceType> typeSelectComboBox;
    @FXML private ComboBox<String> performedBySelectComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TextField mileageTextField;
    @FXML private TextField costTextField;
    @FXML private DatePicker serviceDatePicker;
    @FXML private Label dialogTitle;

    private Stage dialogStage;
    private String selectedVehicle;
    ObservableList<String> allServicesList = FXCollections.observableArrayList();
    ObservableList<String> allProvidersList = FXCollections.observableArrayList();
    ObservableList<CarMaintenanceType> allTypeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveButton.setOnAction(event -> {
            if (validateInput()) {
                insertIntoDatabase();
                dialogStage.close();
            };
        });

        cancelButton.setOnAction(e -> {
            dialogStage.close();
        });

    }

    // This method is called by the caller to inject the dialog instance
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setVehicleSelected(String selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
        dialogTitle.setText("Add Maintenance - " + selectedVehicle);
    }

    public void setAllServicesList(ObservableList<String> allServicesList) {
        this.allServicesList = allServicesList;
        serviceSelectComboBox.setItems(allServicesList);
    }

    public void setTypeList(ObservableList<CarMaintenanceType> allTypeList) {
        this.allTypeList = allTypeList;
        typeSelectComboBox.setItems(allTypeList);
    }

    public void setPerformedByList(ObservableList<String> allProvidersList) {
        this.allProvidersList = allProvidersList;
        performedBySelectComboBox.setItems(allProvidersList);
    }

    public boolean validateInput() {
        boolean missingData =
                serviceSelectComboBox.getValue() == null ||
                typeSelectComboBox.getSelectionModel().isEmpty() ||
                mileageTextField.getText() == null || mileageTextField.getText().trim().isEmpty() ||
                performedBySelectComboBox.getValue() == null;

            if (missingData) {
                showMissingRequiredDataWarning();
                return false;
            } else {
                return true;
            }
        }

    public void showMissingRequiredDataWarning() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Additional info required");
        alert.setContentText("Service, Type, Mileage, and Performed by are all required fields.");
        alert.show();
    }

    public void insertIntoDatabase() {
        StringBuilder sql = new StringBuilder("INSERT INTO car_maintenance_log (service_id, vehicle_id, type, mileage, service_provider_id");
        StringBuilder values = new StringBuilder(" VALUES (?, ?, ?, ?, ?");

        List<Object> params = new ArrayList<>();

        params.add(getServiceId(serviceSelectComboBox.getValue()));
        params.add(getVehicleId(selectedVehicle));
        params.add(typeSelectComboBox.getValue().toString());
        params.add(Integer.parseInt(mileageTextField.getText().replace(",", "").trim()));
        params.add(getProviderId(performedBySelectComboBox.getValue()));

        // Optional field: cost
        if (costTextField.getText() != null && !costTextField.getText().trim().isEmpty()) {
            sql.append(", cost");
            values.append(", ?");
            params.add(Double.parseDouble(costTextField.getText()));
        }

        // Optional field: service date
        if (serviceDatePicker.getValue() != null) {
            sql.append(", service_date");
            values.append(", ?");
            params.add(java.sql.Date.valueOf(serviceDatePicker.getValue()));
        }

        sql.append(")");
        values.append(")");

        String finalSql = sql.toString() + values.toString();

        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();
            PreparedStatement pre = conn.prepareStatement(finalSql);

            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            pre.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getVehicleId(String selectedVehicle) {
        String vehicleID = null;

        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement("SELECT vehicle_id FROM vehicle_dim WHERE model = ?");
            pre.setString(1, selectedVehicle);

            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                vehicleID = rs.getString("vehicle_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleID;
    }

    public String getServiceId(String serviceName) {
        String serviceID = null;

        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement("SELECT service_id FROM service_dim WHERE service_desc = ?");
            pre.setString(1, serviceName);

            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                serviceID = rs.getString("service_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceID;
    }

    public String getProviderId(String providerName) {
        String providerID = null;

        try {
            DBHandlerTracking dbh = new DBHandlerTracking();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement("SELECT service_provider_id FROM service_provider_dim WHERE provider_name = ?");
            pre.setString(1, providerName);

            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                providerID = rs.getString("service_provider_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return providerID;
    }

}
