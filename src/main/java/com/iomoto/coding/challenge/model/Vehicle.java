package com.iomoto.coding.challenge.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Vehicle {

    private ObjectId Id;
    private String name;
    private String vin;
    private String licensePlateNumber;
    private String properties;

    public Vehicle() {

    }

    public ObjectId getId() {
        return Id;
    }

    public void setId(ObjectId id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Id.equals(vehicle.Id) &&
                name.equals(vehicle.name) &&
                vin.equals(vehicle.vin) &&
                licensePlateNumber.equals(vehicle.licensePlateNumber) &&
                properties.equals(vehicle.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name, vin, licensePlateNumber, properties);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", vin='" + vin + '\'' +
                ", license_plate_number='" + licensePlateNumber + '\'' +
                ", properties='" + properties + '\'' +
                '}';
    }
}
