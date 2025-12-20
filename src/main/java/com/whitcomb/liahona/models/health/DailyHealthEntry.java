package com.whitcomb.liahona.models.health;

import javafx.beans.property.*;
import java.time.LocalDate;

public class DailyHealthEntry {

    private final ObjectProperty<LocalDate> beginDate;
    private final IntegerProperty glucose;
    private final SimpleStringProperty meds;
    private final IntegerProperty steps;
    private final DoubleProperty weight;

    public DailyHealthEntry(LocalDate beginDate, Integer glucose, String meds, Integer steps, Double weight) {
        this.beginDate = new SimpleObjectProperty<>(beginDate);
        this.glucose = new SimpleIntegerProperty(glucose);
        this.meds = new SimpleStringProperty(meds);
        this.steps = new SimpleIntegerProperty(steps);
        this.weight = new SimpleDoubleProperty(weight);
    }


    // entry date
    public LocalDate getBeginDate() {
        return beginDate.get();
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate.set(beginDate);
    }

    public ObjectProperty<LocalDate> beginDateProperty() {
        return beginDate;
    }


    // glucose
    public Integer getGlucose() {
        return glucose.get();
    }

    public void setGlucose(Integer glucose) {
        this.glucose.set(glucose);
    }

    public IntegerProperty glucoseProperty() {
        return glucose;
    }


    // meds
    public String getMeds() {
        return meds.get();
    }

    public void setMeds(String meds) {
        this.meds.set(meds);
    }

    public StringProperty medsProperty() {
        return meds;
    }


    // steps
    public Integer getSteps() {
        return steps.get();
    }

    public void setSteps(Integer steps) {
        this.steps.set(steps);
    }

    public IntegerProperty stepsProperty() {
        return steps;
    }


    // weight
    public Double getWeight() {
        return weight.get();
    }

    public void setWeight(Double weight) {
        this.weight.set(weight);
    }

    public DoubleProperty weightProperty() {
        return weight;
    }
}
