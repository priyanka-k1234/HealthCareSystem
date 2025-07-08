package com.example.healthcare.service;

import com.example.healthcare.model.mongo.SensorData;
import com.example.healthcare.repository.mongo.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository repository;

    public List<SensorData> findByPatientId(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    public long count() {
        return repository.count();
    }
}