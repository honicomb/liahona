package com.whitcomb.liahona.models.tasks;

import javafx.beans.property.*;
import java.time.LocalDate;

public class TaskDaily {

    private final IntegerProperty actualTime;
    private final StringProperty category;
    private final StringProperty categoryID;
    private final StringProperty description;
    private final ObjectProperty<LocalDate> dueDate;
    private final IntegerProperty estimatedTime;
    private final StringProperty goalID;
    private final StringProperty notes;
    private final IntegerProperty order;
    private final StringProperty priority;
    private final StringProperty recurringFlag;
    private final ObjectProperty<LocalDate> startDate;
    private final StringProperty status;
    private final StringProperty statusID;
    private final StringProperty taskID;

    public TaskDaily(Integer actualTime,
                     String category,
                     String categoryID,
                     String description,
                     LocalDate dueDate,
                     Integer estimatedTime,
                     String goalID,
                     String notes,
                     Integer order,
                     String priority,
                     String recurringFlag,
                     LocalDate startDate,
                     String status,
                     String statusID,
                     String taskID) {
        this.actualTime = new SimpleIntegerProperty(actualTime);
        this.category = new SimpleStringProperty(category);
        this.categoryID = new SimpleStringProperty(categoryID);
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.estimatedTime = new SimpleIntegerProperty(estimatedTime);
        this.goalID = new SimpleStringProperty(goalID);
        this.notes = new SimpleStringProperty(notes);
        this.order = new SimpleIntegerProperty(order);
        this.priority = new SimpleStringProperty(priority);
        this.recurringFlag = new SimpleStringProperty(recurringFlag);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.status = new SimpleStringProperty(status);
        this.statusID = new SimpleStringProperty(statusID);
        this.taskID = new SimpleStringProperty(taskID);
    }

    /* Actual Time */
    public int getActualTime() {
        return actualTime.get();
    }

    public void setActualTime(int actualTime) {
        this.actualTime.set(actualTime);
    }

    public IntegerProperty actualTimeProperty() {
        return actualTime;
    }


    /* Category */
    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }


    /* Category ID */
    public String getCategoryID() {
        return categoryID.get();
    }

    public void setCategoryID(String categoryID) {
        this.categoryID.set(categoryID);
    }

    public StringProperty categoryIDProperty() {
        return categoryID;
    }


    /* Description */
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }


    /* Due Date */
    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }


    /* Estimated Time */
    public int getEstimatedTime() {
        return estimatedTime.get();
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime.set(estimatedTime);
    }

    public IntegerProperty estimatedTimeProperty() {
        return estimatedTime;
    }


    /* Goal ID */
    public String getGoalID() {
        return goalID.get();
    }

    public void setGoalID(String goalID) {
        this.goalID.set(goalID);
    }

    public StringProperty goalIDProperty() {
        return goalID;
    }


    /* Notes */
    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public StringProperty notesProperty() {
        return notes;
    }


    /* Order */
    public int getOrder() {
        return order.get();
    }

    public void setOrder(int order) {
        this.order.set(order);
    }

    public IntegerProperty orderProperty() {
        return order;
    }


    /* Priority */
    public String getPriority() {
        return priority.get();
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public StringProperty priorityProperty() {
        return priority;
    }


    /* Recurring flag */
    public String getRecurringFlag() {
        return recurringFlag.get();
    }

    public void setRecurringFlag(String recurringFlag) {
        this.recurringFlag.set(recurringFlag);
    }

    public StringProperty recurringFlagProperty() {
        return recurringFlag;
    }


    /* Start date */
    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }


    /* Status */
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }


    /* Status ID */
    public String getStatusID() {
        return statusID.get();
    }

    public void setStatusID(String statusID) {
        this.statusID.set(statusID);
    }

    public StringProperty statusIDProperty() {
        return statusID;
    }


    /* Task ID */
    public String getTaskID() {
        return taskID.get();
    }

    public void setTaskID(String taskID) {
        this.taskID.set(taskID);
    }

    public StringProperty taskIDProperty() {
        return taskID;
    }
}
