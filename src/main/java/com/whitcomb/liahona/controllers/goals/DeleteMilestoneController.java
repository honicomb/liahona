package com.whitcomb.liahona.controllers.goals;

import com.whitcomb.liahona.models.goals.DBHandlerGoals;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DeleteMilestoneController implements Initializable {

    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private ComboBox<String> milestoneDeleteComboBox;

    private Stage dialogStage;
    private String selectedMilestoneID;
    private boolean deleteConfirmed;
    private String selectedGoalID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        deleteButton.setOnAction(event -> {
            selectedMilestoneID = milestoneDeleteComboBox.getSelectionModel().getSelectedItem();
            deleteMilestone(selectedMilestoneID);
            deleteConfirmed = true;
            dialogStage.close();
        });

        cancelButton.setOnAction(e -> {
            deleteConfirmed = false;
            dialogStage.close();
        });

    }

    // This method is called by the caller to inject the dialog instance
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getSelectedMilestoneID() {
        return selectedMilestoneID;
    }

    public boolean isDeleteConfirmed() {
        return deleteConfirmed;
    }

    public void setSelectedGoalID(String selectedGoalID) {
        this.selectedGoalID = selectedGoalID;
    }

    public void loadMilestonesFromDatabase() {

        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement("SELECT milestone_id FROM milestone WHERE goal_id = ? ORDER by due_date");

            pre.setString(1, selectedGoalID);
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                milestoneDeleteComboBox.getItems().add(rs.getString("milestone_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMilestone(String milestoneToDelete) {
        try {
            DBHandlerGoals dbh = new DBHandlerGoals();
            Connection conn = dbh.getConnection();

            PreparedStatement pre = conn.prepareStatement("DELETE FROM milestone where milestone_id = ?");
            pre.setString(1, milestoneToDelete);
            pre.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            showDeleteFailedMessage();
        }
    }

    public void showDeleteFailedMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Failure");
        alert.setContentText("Milestone failed to delete from database. Please try again.");
        alert.show();
    }
}
