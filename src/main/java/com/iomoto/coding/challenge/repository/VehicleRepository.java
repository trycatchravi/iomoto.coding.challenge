package com.iomoto.coding.challenge.repository;

import com.iomoto.coding.challenge.model.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    List<Vehicle> saveAll(List<Vehicle> vehicles);

    List<Vehicle> findAll();

    List<Vehicle> findAll(List<String> Ids);

    Vehicle findOne(String Id);

    long count();

    long delete(String Id);

    long delete(List<String> Id);

    long deleteAll();

    Vehicle update(Vehicle vehicle);

}
