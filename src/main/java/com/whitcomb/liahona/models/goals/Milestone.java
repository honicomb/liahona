package com.whitcomb.liahona.models.goals;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Milestone {

    private final ObjectProperty<LocalDate> dueDate;
    private final StringProperty goalID;
    private final StringProperty milestoneID;
    private final StringProperty milestoneDesc;
    private final StringProperty status;


    public Milestone(LocalDate dueDate, String goalID, String milestoneID, String milestoneDesc, String status) {
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.goalID = new SimpleStringProperty(goalID);
        this.milestoneID = new SimpleStringProperty(milestoneID);
        this.milestoneDesc = new SimpleStringProperty(milestoneDesc);
        this.status = new SimpleStringProperty(status);
    }


    // dueDate
    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }


    // goalID
    public String getGoalID() {
        return goalID.get();
    }

    public void setGoalID(String goalID) {
        this.goalID.set(goalID);
    }

    public StringProperty goalIDProperty() {
        return goalID;
    }


    // milestoneID
    public String getMilestoneID() {
        return milestoneID.get();
    }

    public void setMilestoneID(String milestoneID) {
        this.milestoneID.set(milestoneID);
    }

    public StringProperty milestoneIDProperty() {
        return milestoneID;
    }


    // milestoneDesc
    public String getMilestoneDesc() {
        return milestoneDesc.get();
    }

    public void setMilestoneDesc(String milestoneDesc) {
        this.milestoneDesc.set(milestoneDesc);
    }

    public StringProperty milestoneDescProperty() {
        return milestoneDesc;
    }

    //status
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Milestone that = (Milestone) o;
        return milestoneID.equals(that.milestoneID);
    }

    @Override
    public int hashCode() {
        return milestoneID.hashCode();
    }
}
