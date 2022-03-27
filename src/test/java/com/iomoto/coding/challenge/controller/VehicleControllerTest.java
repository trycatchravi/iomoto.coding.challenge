package com.iomoto.coding.challenge.controller;

import com.iomoto.coding.challenge.model.Vehicle;
import com.iomoto.coding.challenge.repository.VehicleRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehicleControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private TestVehicle testVehicle;
    private String URL;
    @Mock
    private MongoClient mongoClient;

    @BeforeEach
    void setUp() {
        URL = "http://localhost:" + port + "/api";
        createVehicleCollectionIfNotPresent(mongoClient);
    }

    @AfterEach
    void tearDown() {
        vehicleRepository.deleteAll();
    }

    @DisplayName("POST /vehicle with 1 vehicle")
    @Test
    void postVehicle() {
        // GIVEN
        // WHEN
        ResponseEntity<Vehicle> result = rest.postForEntity(URL + "/vehicle", testVehicle.getBMW(), Vehicle.class);
        // THEN
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        Vehicle vehicleResult = result.getBody();
        assertNotNull(vehicleResult.getId());
    }

    @DisplayName("POST /vehicle with 2 vehicle")
    @ParameterizedTest
    void postVehicles() {
        // GIVEN
        // WHEN
        HttpEntity<List<Vehicle>> body = new HttpEntity<>(testVehicle.getVehicleList());
        ResponseEntity<List<Vehicle>> response = rest.exchange(URL + "/vehicles", HttpMethod.
                POST, body, new ParameterizedTypeReference<List<Vehicle>>() {
        });
        // THEN
        assertEquals(response.getStatusCode(),HttpStatus.CREATED);
    }

    @DisplayName("GET /vehicle with 2 vehicles")
    @Test
    void getVehicles() {
        // GIVEN
        List<Vehicle> vehiclesInserted = vehicleRepository.saveAll(testVehicle.getVehicleList());
        // WHEN
        ResponseEntity<List<Vehicle>> result = rest.exchange(URL + "/vehicles", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Vehicle>>() {
                });
        // THEN
        assertEquals(result.getStatusCode(),HttpStatus.OK);
    }

    @DisplayName("GET /vehicle/{id}")
    @Test
    void getVehicle() {
        // GIVEN
        Vehicle vehicleInserted = vehicleRepository.save(testVehicle.getBMW());
        ObjectId idInserted = vehicleInserted.getId();
        // WHEN
        ResponseEntity<Vehicle> result = rest.getForEntity(URL + "/vehicle/" + idInserted, Vehicle.class);
        // THEN
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertEquals(result.getBody(),vehicleInserted);
    }

    @DisplayName("GET /vehicles/{ids}")
    @Test
    void testGetVehicles() {
        // GIVEN
        List<Vehicle> vehiclesInserted = vehicleRepository.saveAll(testVehicle.getVehicleList());
        List<String> idsInserted = vehiclesInserted.stream().map(Vehicle::getId).map(ObjectId::toString).collect(toList());
        // WHEN
        String url = URL + "/vehicles/" + String.join(",", idsInserted);
        ResponseEntity<List<Vehicle>> result = rest.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Vehicle>>() {
                });
        // THEN
        assertEquals(result.getStatusCode(),HttpStatus.OK);
    }

    @DisplayName("GET /vehicles/count")
    @Test
    void getCount() {
        // GIVEN
        vehicleRepository.saveAll(testVehicle.getVehicleList());
        // WHEN
        ResponseEntity<Long> result = rest.getForEntity(URL + "/vehicles/count", Long.class);
        // THEN
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertEquals(result.getBody(),2L);
    }

    @DisplayName("DELETE /vehicle/{id}")
    @Test
    void deleteVehicle() {
        // GIVEN
        Vehicle vehicleInserted = vehicleRepository.save(testVehicle.getBMW());
        ObjectId idInserted = vehicleInserted.getId();
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/vehicle/" + idInserted.toString(), HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Long>() {
                });
        // THEN
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertEquals(result.getBody(),1L);
        assertEquals(vehicleRepository.count(),0L);
    }

    @DisplayName("DELETE /vehicles/{ids}")
    @Test
    void deleteVehicles() {
        // GIVEN
        List<Vehicle> vehiclesInserted = vehicleRepository.saveAll(testVehicle.getVehicleList());
        List<String> idsInserted = vehiclesInserted.stream().map(Vehicle::getId).map(ObjectId::toString).collect(toList());
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/vehicles/" + String.join(",", idsInserted), HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Long>() {
                });
        // THEN
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertEquals(result.getBody(),2L);
        assertEquals(vehicleRepository.count(),0L);
    }

    @DisplayName("DELETE /vehicles")
    @Test
    void testDeleteVehicles() {
        // GIVEN
        vehicleRepository.saveAll(testVehicle.getVehicleList());
        // WHEN
        ResponseEntity<Long> result = rest.exchange(URL + "/vehicles", HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Long>() {
                });
        // THEN
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertEquals(result.getBody(),2L);
        assertEquals(vehicleRepository.count(),0L);
    }

    @DisplayName("PUT /vehicle")
    @Test
    void updateVehicle() {
        // GIVEN
        Vehicle vehicleInserted = vehicleRepository.save(testVehicle.getBMW());
        // WHEN
        vehicleInserted.setVin("123BMW");
        vehicleInserted.setLicensePlateNumber("1313-BMW");
        HttpEntity<Vehicle> body = new HttpEntity<>(vehicleInserted);
        ResponseEntity<Vehicle> result = rest.exchange(URL + "/vehicle", HttpMethod.PUT, body,
                new ParameterizedTypeReference<Vehicle>() {
                });
        // THEN
        assertEquals(result.getBody(),HttpStatus.OK);
        assertEquals(result.getBody().getVin(),"123BMW");
        assertEquals(result.getBody().getLicensePlateNumber(),"1313-BMW");
        assertEquals(vehicleRepository.count(),1L);
    }

    private void createVehicleCollectionIfNotPresent(MongoClient mongoClient) {
        // This is required because it is not possible to create a new collection within a multi-documents transaction.
        // Some tests start by inserting 2 documents with a transaction.
        MongoDatabase db = mongoClient.getDatabase("vehicles");
        if (!db.listCollectionNames().into(new ArrayList<>()).contains("vehicle"))
            db.createCollection("vehicle");
    }
}