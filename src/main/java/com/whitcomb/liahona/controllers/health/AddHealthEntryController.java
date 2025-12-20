package com.whitcomb.liahona.controllers.health;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.whitcomb.liahona.models.health.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddHealthEntryController implements Initializable {

    @FXML private TextField bloodSugarTextField;
    @FXML private Button cancelButton;
    @FXML private Label dialogTitle;
    @FXML private DatePicker healthDatePicker;
    @FXML private ComboBox<String> medsComboBox;
    @FXML private Button saveButton;
    @FXML private TextField stepsTextField;
    @FXML private TextField weightTextField;

    private ResultSet rs;

    private Stage dialogStage;
    ObservableList<String> yesNoList = FXCollections.observableArrayList("Y","N");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveButton.setOnAction(event -> {
            if (checkForReqdFields())
                if (checkForDupDate()) {
                    insertIntoDatabase();
                    dialogStage.close();
                }
            }
        );

        cancelButton.setOnAction(e -> {
            dialogStage.close();
        });
    }

    // This method is called by the caller to inject the dialog instance
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setYesNoComboBox(ObservableList<String> yesNoList) {
        this.yesNoList = yesNoList;
        medsComboBox.setItems(yesNoList);
    }

    public void insertIntoDatabase() {
        StringBuilder sql = new StringBuilder("INSERT INTO vitals (date");
        StringBuilder values = new StringBuilder(" VALUES (?");

        List<Object> params = new ArrayList<>();

        params.add(java.sql.Date.valueOf(healthDatePicker.getValue()));

        // Optional field: weight
        if (weightTextField.getText() != null && !weightTextField.getText().trim().isEmpty()) {
            sql.append(", weight");
            values.append(", ?");
            params.add(Double.parseDouble(weightTextField.getText()));
        }

        // Optional field: glucose
        if (bloodSugarTextField.getText() != null && !bloodSugarTextField.getText().trim().isEmpty()) {
            sql.append(", glucose");
            values.append(", ?");
            params.add(Integer.parseInt(bloodSugarTextField.getText()));
        }

        // Optional field: steps
        if (stepsTextField.getText() != null && !stepsTextField.getText().trim().isEmpty()) {
            sql.append(", steps");
            values.append(", ?");
            params.add(Integer.parseInt(stepsTextField.getText()));
        }

        // Optional field: meds
        if (medsComboBox.getValue() != null) {
            sql.append(", meds");
            values.append(", ?");
            params.add(medsComboBox.getValue());
        }

        sql.append(")");
        values.append(")");

        String finalSql = sql.toString() + values.toString();

        try {
            DBHandlerHealth dbh = new DBHandlerHealth();
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

    public boolean checkForReqdFields() {
        if (healthDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Missing info");
            alert.setContentText("Date is a required field");
            alert.show();
            return false;
        }
        return true;
    }

    public boolean checkForDupDate() {
        try {
            DBHandlerHealth dbh = new DBHandlerHealth();
            Connection conn = dbh.getConnection();
            PreparedStatement pre = conn.prepareStatement("SELECT * FROM vitals WHERE date = ?");
            pre.setString(1, String.valueOf(Date.valueOf(healthDatePicker.getValue())));

            rs = pre.executeQuery();

            if (rs.next()) {
                showDupEntryAlert();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void showDupEntryAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("You have entered a duplicate date value.");
        alert.show();
    }
}
