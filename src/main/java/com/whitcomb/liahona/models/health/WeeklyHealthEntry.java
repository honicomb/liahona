package com.whitcomb.liahona.models.health;

import java.time.LocalDate;
import javafx.beans.property.*;

public class WeeklyHealthEntry {
    private final ObjectProperty<LocalDate> beginDate;
    private final IntegerProperty glucoseAvg;
    private final IntegerProperty medsCnt;
    private final IntegerProperty stepsAvg;
    private final DoubleProperty weightAvg;

    public WeeklyHealthEntry(LocalDate beginDate, Integer glucoseAvg, Integer medsCnt, Integer stepsAvg, Double weightAvg) {
        this.beginDate = new SimpleObjectProperty<>(beginDate);
        this.glucoseAvg = new SimpleIntegerProperty(glucoseAvg);
        this.medsCnt = new SimpleIntegerProperty(medsCnt);
        this.stepsAvg = new SimpleIntegerProperty(stepsAvg);
        this.weightAvg = new SimpleDoubleProperty(weightAvg);
    }

    // weekStart
    public LocalDate getBeginDate() {
        return beginDate.get();
    }

    public ObjectProperty<LocalDate> beginDateProperty() {
        return beginDate;
    }


    // glucoseAvg
    public Integer getGlucoseAvg() {
        return glucoseAvg.get();
    }

    public IntegerProperty glucoseAvgProperty() {
        return glucoseAvg;
    }


    // medsCnt
    public Integer getMedsCnt() {
        return medsCnt.get();
    }

    public IntegerProperty medsCntProperty() {
        return medsCnt;
    }


    // stepsAvg
    public Integer getStepsAvg() {
        return stepsAvg.get();
    }

    public IntegerProperty stepsAvgProperty() {
        return stepsAvg;
    }


    //  weightAvg
    public Double getWeightAvg() {
        return weightAvg.get();
    }

    public DoubleProperty weightAvgProperty() {
        return weightAvg;
    }
}
