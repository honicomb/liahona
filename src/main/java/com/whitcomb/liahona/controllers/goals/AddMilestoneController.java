package com.whitcomb.liahona.controllers.goals;

import com.whitcomb.liahona.models.goals.DBHandlerGoals;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class AddMilestoneController implements Initializable {

    @FXML private TextField milestoneDescText;
    @FXML private DatePicker dueDatePicker;
    @FXML private Label milestoneIDLabel;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Stage dialogStage;
    private String selectedGoalID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        milestoneIDLabel.setText(getNextMilestoneID());
        saveButton.setOnAction(e -> {
            if (!milestoneDescText.getText().isEmpty()) {
                saveMilestoneToDatabase();
            }
            dialogStage.close();
        });
        cancelButton.setOnAction(e -> {
            dialogStage.close();
        });
    }

    // This method is called by the caller to inject the dialog instance
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSelectedGoalID(String selectedGoalID) {
        this.selectedGoalID = selectedGoalID;
    }

    public String getNextMilestoneID() {
        String newMilestoneID = null;

        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement("SELECT MAX(CAST(SUBSTRING(milestone_id,4) AS UNSIGNED) + 1) as new_mls_number FROM milestone");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                newMilestoneID =  ("MLS" + rs.getString("new_mls_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newMilestoneID;
    }

    public void saveMilestoneToDatabase() {
        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement("INSERT INTO milestone (milestone_id, goal_id, status_id, due_date, milestone_desc, status_date) VALUES (?, ?, ?, ?, ?, ?)");
            pre.setString(1, milestoneIDLabel.getText());
            pre.setString(2, selectedGoalID);
            pre.setString(3, "OP");

            if (dueDatePicker.getValue() != null) {
                pre.setString(4, dueDatePicker.getValue().toString());
            } else {
                pre.setNull(4, java.sql.Types.DATE);
            }

            pre.setString(5, milestoneDescText.getText());
            pre.setString(6, String.valueOf(Date.valueOf(LocalDate.now())));

            pre.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            showAddFailedMessage();
        }
    }

    public void showAddFailedMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Failure");
        alert.setContentText("Milestone failed to save to database. Please try again.");
        alert.show();
    }
}
