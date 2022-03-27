package com.iomoto.coding.challenge.repository;

import com.iomoto.coding.challenge.model.Vehicle;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.WriteModel;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.eq;


@Repository
public class VehicleRepositoryImpl implements VehicleRepository{

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    private final MongoClient client;

    private MongoCollection<Vehicle> vehicleMongoCollection;

    public VehicleRepositoryImpl(MongoClient client) {
            this.client = client;
    }

    @PostConstruct
    void init() {
        vehicleMongoCollection = client.getDatabase("vehicle").getCollection("vehicle",Vehicle.class);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        vehicleMongoCollection.insertOne(vehicle);
        return vehicle;
    }

    @Override
    public List<Vehicle> saveAll(List<Vehicle> vehicles) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                vehicleMongoCollection.insertMany(clientSession, vehicles);
                return vehicles;
            }, txnOptions);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleMongoCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<Vehicle> findAll(List<String> Ids) {
        return vehicleMongoCollection.find(in("_id", mapToObjectIds(Ids))).into(new ArrayList<>());
    }

    @Override
    public Vehicle findOne(String id) {
        return vehicleMongoCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long count() {
        return vehicleMongoCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return vehicleMongoCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> vehicleMongoCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> vehicleMongoCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        return vehicleMongoCollection.findOneAndReplace(eq("_id", vehicle.getId()), vehicle, options);
    }



    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }
}
