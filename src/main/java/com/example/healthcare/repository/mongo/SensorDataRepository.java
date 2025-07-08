package com.example.healthcare.repository.mongo;

import com.example.healthcare.model.mongo.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SensorDataRepository extends MongoRepository<SensorData, String> {
    List<SensorData> findByPatientId(Long patientId);
}