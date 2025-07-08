package com.example.healthcare.service;

import com.example.healthcare.model.Patient;
import com.example.healthcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public Map<String, Long> getGenderDistribution() {
        List<Patient> all = patientRepository.findAll();
        return all.stream().collect(Collectors.groupingBy(Patient::getGender, Collectors.counting()));
    }

    public List<Patient> getRecentPatients(int limit) {
        return patientRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "patientId"))).getContent();
    }

    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    // Add this method:
    public String getPatientNameById(Long id) {
        Patient patient = getPatientById(id);
        return patient.getFirstName() + " " + patient.getLastName();
    }
}