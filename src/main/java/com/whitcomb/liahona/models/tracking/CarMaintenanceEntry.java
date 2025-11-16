package com.whitcomb.liahona.models.tracking;

import javafx.beans.property.*;
import java.time.LocalDate;

public class CarMaintenanceEntry {

    private final DoubleProperty cost;
    private final IntegerProperty mileage;
    private final ObjectProperty<LocalDate> serviceDate;
    private final StringProperty serviceDesc;
    private final StringProperty serviceID;
    private final IntegerProperty serviceProviderID;
    private final StringProperty serviceProviderName;
    private final SimpleObjectProperty<CarMaintenanceType> type;
    private final StringProperty vehicleID;
    private final StringProperty vehicleModel;


    public CarMaintenanceEntry(Double cost, Integer mileage, LocalDate serviceDate, String serviceDesc, String serviceID, Integer serviceProviderID, String serviceProviderName, CarMaintenanceType type, String vehicleID, String vehicleModel) {
        this.cost = new SimpleDoubleProperty(cost);
        this.mileage = new SimpleIntegerProperty(mileage);
        this.serviceDate = new SimpleObjectProperty<>(serviceDate);
        this.serviceDesc = new SimpleStringProperty(serviceDesc);
        this.serviceID = new SimpleStringProperty(serviceID);
        this.serviceProviderID = new SimpleIntegerProperty(serviceProviderID);
        this.serviceProviderName = new SimpleStringProperty(serviceProviderName);
        this.type = new SimpleObjectProperty<>(type);
        this.vehicleID = new SimpleStringProperty(vehicleID);
        this.vehicleModel = new SimpleStringProperty(vehicleModel);
    }

    // Cost
    public double getCost() {
        return cost.get();
    }

    public void setCost(Double cost) {
        this.cost.set(cost);
    }

    public DoubleProperty costProperty() {
        return cost;
    }


    // Mileage
    public Integer getMileage() {
        return mileage.get();
    }

    public void setMileage(Integer mileage) {
        this.mileage.set(mileage);
    }

    public IntegerProperty mileageProperty() {
        return mileage;
    }


    // ServiceDate
    public LocalDate getServiceDate() {
        return serviceDate.get();
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate.set(serviceDate);
    }

    public ObjectProperty<LocalDate> serviceDateProperty() {
        return serviceDate;
    }


    // ServiceDesc
    public String getServiceDesc() {
        return serviceDesc.get();
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc.set(serviceDesc);
    }

    public StringProperty serviceDescProperty() {
        return serviceDesc;
    }


    // getServiceID
    public String getServiceID() {
        return serviceID.get();
    }

    public void setServiceID(String serviceID) {
        this.serviceID.set(serviceID);
    }

    public StringProperty serviceIDProperty() {
        return serviceID;
    }


    // ServiceProviderID
    public int getServiceProviderID() {
        return serviceProviderID.get();
    }

    public void setServiceProviderID(int serviceProviderID) {
        this.serviceProviderID.set(serviceProviderID);
    }

    public IntegerProperty serviceProviderIDProperty() {
        return serviceProviderID;
    }


    // ServiceProviderName
    public String getServiceProviderName() {
        return serviceProviderName.get();
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName.set(serviceProviderName);
    }

    public StringProperty serviceProviderNameProperty() {
        return serviceProviderName;
    }


    // type
    public CarMaintenanceType getType() {
        return type.get();
    }

    public void setType(CarMaintenanceType type) {
        this.type.set(type);
    }

    public ObjectProperty<CarMaintenanceType> typeProperty() {
        return type;
    }


    // VehicleID
    public String getVehicleID() {
        return vehicleID.get();
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID.set(vehicleID);
    }

    public StringProperty vehicleIDProperty() {
        return vehicleID;
    }


    // VehicleModel
    public String getVehicleModel() {
        return vehicleModel.get();
    }

    public void setVehicleModel(String vehicleName) {
        this.vehicleModel.set(vehicleName);
    }

    public StringProperty vehicleModelProperty() {
        return vehicleModel;
    }
}
