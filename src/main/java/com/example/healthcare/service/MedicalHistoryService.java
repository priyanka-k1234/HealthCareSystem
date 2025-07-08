package com.example.healthcare.service;

import com.example.healthcare.model.mongo.PatientMedicalHistory;
import com.example.healthcare.repository.mongo.PatientMedicalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalHistoryService {
    @Autowired
    private PatientMedicalHistoryRepository repository;

    public Optional<PatientMedicalHistory> findByPatientId(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    public long count() {
        return repository.count();
    }
}