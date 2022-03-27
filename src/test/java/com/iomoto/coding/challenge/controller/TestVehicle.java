package com.iomoto.coding.challenge.controller;

import com.iomoto.coding.challenge.model.Vehicle;
import org.springframework.stereotype.Component;

import java.util.List;
import static java.util.Arrays.asList;

@Component
public class TestVehicle {

    Vehicle getBMW() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlateNumber("BMW-1234");
        vehicle.setName("BMW");
        vehicle.setProperties("{color:blue,price:10000}");
        vehicle.setVin("BMW4321");
        return vehicle;
    }

    Vehicle getAudi() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlateNumber("Audi-S3");
        vehicle.setName("Audi");
        vehicle.setProperties("<color>gray</color><price>1021202</price>");
        vehicle.setVin("Audi4321");
        return vehicle;
    }

    List<Vehicle> getVehicleList() {
        return asList(getBMW(), getAudi());
    }
}
