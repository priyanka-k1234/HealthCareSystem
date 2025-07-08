package com.example.healthcare.repository.mongo;

import com.example.healthcare.model.mongo.PatientMedicalHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PatientMedicalHistoryRepository extends MongoRepository<PatientMedicalHistory, String> {
    Optional<PatientMedicalHistory> findByPatientId(Long patientId);
}