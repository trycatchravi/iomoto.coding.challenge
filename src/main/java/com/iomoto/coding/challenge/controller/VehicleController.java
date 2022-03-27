package com.iomoto.coding.challenge.controller;


import com.iomoto.coding.challenge.model.Vehicle;
import com.iomoto.coding.challenge.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static java.util.Arrays.asList;

@RestController
@RequestMapping("/api")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping("vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle postVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @PostMapping("vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Vehicle> postVehicles(@RequestBody List<Vehicle> vehicles) {
        return vehicleRepository.saveAll(vehicles);
    }

    @GetMapping("vehicles")
    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @GetMapping("vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable String id) {
        Vehicle vehicle = vehicleRepository.findOne(id);
        if (vehicle == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("vehicles/{ids}")
    public List<Vehicle> getVehicles(@PathVariable String ids) {
        List<String> listIds = asList(ids.split(","));
        return vehicleRepository.findAll(listIds);
    }

    @GetMapping("vehicles/count")
    public Long getCount() {
        return vehicleRepository.count();
    }

    @DeleteMapping("vehicle/{id}")
    public Long deleteVehicle(@PathVariable String id) {
        return vehicleRepository.delete(id);
    }

    @DeleteMapping("vehicles/{ids}")
    public Long deleteVehicles(@PathVariable String ids) {
        List<String> listIds = asList(ids.split(","));
        return vehicleRepository.delete(listIds);
    }

    @DeleteMapping("vehicles")
    public Long deleteVehicles() {
        return vehicleRepository.deleteAll();
    }

    @PutMapping("vehicle")
    public Vehicle updateVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.update(vehicle);
    }

}
