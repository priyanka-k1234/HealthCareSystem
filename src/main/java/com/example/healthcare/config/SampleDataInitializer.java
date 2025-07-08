package com.example.healthcare.config;

import com.example.healthcare.model.*;
import com.example.healthcare.model.mongo.*;
import com.example.healthcare.repository.*;
import com.example.healthcare.repository.mongo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class SampleDataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            PatientMedicalHistoryRepository medicalHistoryRepository,
            SensorDataRepository sensorDataRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // --- USERS ---
            // --- USERS ---
            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .passwordHash(passwordEncoder.encode("adminpass"))
                        .role("ADMIN")
                        .firstName("System")
                        .lastName("Administrator")
                        .build();
                User doctor1 = User.builder()
                        .username("drsmith")
                        .email("drsmith@example.com")
                        .passwordHash(passwordEncoder.encode("docpass"))
                        .role("DOCTOR")
                        .firstName("John")
                        .lastName("Smith")
                        .build();
                User doctor2 = User.builder()
                        .username("drjones")
                        .email("drjones@example.com")
                        .passwordHash(passwordEncoder.encode("docpass2"))
                        .role("DOCTOR")
                        .firstName("Emily")
                        .lastName("Jones")
                        .build();
                User patientUser1 = User.builder()
                        .username("johndoe")
                        .email("johndoe@example.com")
                        .passwordHash(passwordEncoder.encode("patientpass"))
                        .role("PATIENT")
                        .firstName("John")
                        .lastName("Doe")
                        .build();
                User patientUser2 = User.builder()
                        .username("janeroe")
                        .email("janeroe@example.com")
                        .passwordHash(passwordEncoder.encode("patientpass2"))
                        .role("PATIENT")
                        .firstName("Jane")
                        .lastName("Roe")
                        .build();
                User patientUser3 = User.builder()
                        .username("bobsmith")
                        .email("bobsmith@example.com")
                        .passwordHash(passwordEncoder.encode("patientpass3"))
                        .role("PATIENT")
                        .firstName("Bob")
                        .lastName("Smith")
                        .build();
                userRepository.saveAll(Arrays.asList(admin, doctor1, doctor2, patientUser1, patientUser2, patientUser3));
            }

            // --- PATIENTS ---
            if (patientRepository.count() == 0) {
                Patient patient1 = new Patient();
                patient1.setFirstName("John");
                patient1.setLastName("Doe");
                patient1.setDateOfBirth(LocalDate.of(1990, 1, 1));
                patient1.setGender("Male");
                patient1.setBloodType("O+");
                patient1.setPhone("555-1234");
                patient1.setEmail("john.doe@example.com");
                patient1.setAddress("123 Main St");

                Patient patient2 = new Patient();
                patient2.setFirstName("Jane");
                patient2.setLastName("Roe");
                patient2.setDateOfBirth(LocalDate.of(1985, 5, 15));
                patient2.setGender("Female");
                patient2.setBloodType("A-");
                patient2.setPhone("555-5678");
                patient2.setEmail("jane.roe@example.com");
                patient2.setAddress("456 Oak Ave");

                Patient patient3 = new Patient();
                patient3.setFirstName("Bob");
                patient3.setLastName("Smith");
                patient3.setDateOfBirth(LocalDate.of(1975, 8, 20));
                patient3.setGender("Male");
                patient3.setBloodType("B+");
                patient3.setPhone("555-9012");
                patient3.setEmail("bob.smith@example.com");
                patient3.setAddress("789 Pine Rd");

                Patient patient4 = new Patient();
                patient4.setFirstName("Alice");
                patient4.setLastName("Johnson");
                patient4.setDateOfBirth(LocalDate.of(2000, 3, 10));
                patient4.setGender("Female");
                patient4.setBloodType("AB+");
                patient4.setPhone("555-3456");
                patient4.setEmail("alice.johnson@example.com");
                patient4.setAddress("321 Maple St");

                patientRepository.saveAll(Arrays.asList(patient1, patient2, patient3, patient4));
            }

            // --- APPOINTMENTS ---
            if (appointmentRepository.count() == 0) {
                List<Patient> patients = patientRepository.findAll();
                List<User> users = userRepository.findAll();
                Long doctorId1 = users.stream().filter(u -> "DOCTOR".equals(u.getRole())).findFirst().map(User::getId).orElse(null);
                Long doctorId2 = users.stream().filter(u -> "DOCTOR".equals(u.getRole()) && !"drsmith".equals(u.getUsername())).findFirst().map(User::getId).orElse(null);

                Appointment appt1 = new Appointment();
                appt1.setPatientId(patients.get(0).getPatientId());
                appt1.setDoctorId(doctorId1);
                appt1.setDateTime(LocalDateTime.now().plusDays(1));
                appt1.setReason("General Checkup");
                appt1.setStatus("Scheduled");

                Appointment appt2 = new Appointment();
                appt2.setPatientId(patients.get(1).getPatientId());
                appt2.setDoctorId(doctorId1);
                appt2.setDateTime(LocalDateTime.now().plusDays(2));
                appt2.setReason("Consultation");
                appt2.setStatus("Scheduled");

                Appointment appt3 = new Appointment();
                appt3.setPatientId(patients.get(2).getPatientId());
                appt3.setDoctorId(doctorId2);
                appt3.setDateTime(LocalDateTime.now().plusDays(3));
                appt3.setReason("Follow-up");
                appt3.setStatus("Scheduled");

                Appointment appt4 = new Appointment();
                appt4.setPatientId(patients.get(3).getPatientId());
                appt4.setDoctorId(doctorId2);
                appt4.setDateTime(LocalDateTime.now().plusDays(4));
                appt4.setReason("Blood Test");
                appt4.setStatus("Scheduled");

                appointmentRepository.saveAll(Arrays.asList(appt1, appt2, appt3, appt4));
            }

            // --- MONGODB: Patient Medical History ---
            if (medicalHistoryRepository.count() == 0) {
                List<Patient> patients = patientRepository.findAll();

                PatientMedicalHistory history1 = new PatientMedicalHistory();
                history1.setPatientId(patients.get(0).getPatientId());
                history1.setAllergies(Arrays.asList("Peanuts", "Penicillin"));
                history1.setConditions(Arrays.asList("Hypertension"));
                PatientMedicalHistory.Medication med1 = new PatientMedicalHistory.Medication();
                med1.setName("Lisinopril");
                med1.setDosage("10mg");
                med1.setFrequency("Once daily");
                med1.setPrescribedBy("drsmith");
                med1.setStartDate(new Date());
                med1.setEndDate(null);
                history1.setMedications(Collections.singletonList(med1));
                history1.setSurgeries(new ArrayList<>());
                history1.setLastUpdated(new Date());

                PatientMedicalHistory history2 = new PatientMedicalHistory();
                history2.setPatientId(patients.get(1).getPatientId());
                history2.setAllergies(Collections.singletonList("Latex"));
                history2.setConditions(Arrays.asList("Asthma", "Diabetes"));
                PatientMedicalHistory.Medication med2 = new PatientMedicalHistory.Medication();
                med2.setName("Albuterol");
                med2.setDosage("2 puffs");
                med2.setFrequency("As needed");
                med2.setPrescribedBy("drjones");
                med2.setStartDate(new Date());
                med2.setEndDate(null);
                history2.setMedications(Collections.singletonList(med2));
                PatientMedicalHistory.Surgery surg1 = new PatientMedicalHistory.Surgery();
                surg1.setName("Appendectomy");
                surg1.setDate(new Date());
                surg1.setHospital("City Hospital");
                surg1.setNotes("No complications");
                history2.setSurgeries(Collections.singletonList(surg1));
                history2.setLastUpdated(new Date());

                medicalHistoryRepository.saveAll(Arrays.asList(history1, history2));
            }

            // --- MONGODB: Sensor Data ---
            if (sensorDataRepository.count() == 0) {
                List<Patient> patients = patientRepository.findAll();

                // Patient 1: HeartRateMonitor with two readings
                SensorData data1 = new SensorData();
                data1.setPatientId(patients.get(0).getPatientId());
                data1.setDeviceType("HeartRateMonitor");
                data1.setDeviceId("HRM-001");
                data1.setRecordedDate(new Date());
                List<SensorData.Metric> metrics1 = new ArrayList<>();
                SensorData.Metric hrRest = new SensorData.Metric();
                hrRest.setType("HeartRate");
                hrRest.setValue(72);
                hrRest.setUnit("bpm");
                hrRest.setTimestamp(new Date(System.currentTimeMillis() - 60 * 60 * 1000)); // 1 hour ago
                hrRest.setContext("resting");
                metrics1.add(hrRest);

                SensorData.Metric hrActive = new SensorData.Metric();
                hrActive.setType("HeartRate");
                hrActive.setValue(88);
                hrActive.setUnit("bpm");
                hrActive.setTimestamp(new Date(System.currentTimeMillis() - 30 * 60 * 1000)); // 30 min ago
                hrActive.setContext("after walking");
                metrics1.add(hrActive);

                SensorData.Metric spo2 = new SensorData.Metric();
                spo2.setType("SpO2");
                spo2.setValue(98);
                spo2.setUnit("%");
                spo2.setTimestamp(new Date(System.currentTimeMillis() - 30 * 60 * 1000));
                spo2.setContext("after walking");
                metrics1.add(spo2);

                data1.setMetrics(metrics1);

                // Patient 2: BloodPressureMonitor with two readings
                SensorData data2 = new SensorData();
                data2.setPatientId(patients.get(1).getPatientId());
                data2.setDeviceType("BloodPressureMonitor");
                data2.setDeviceId("BPM-002");
                data2.setRecordedDate(new Date());
                List<SensorData.Metric> metrics2 = new ArrayList<>();
                SensorData.Metric bpMorning = new SensorData.Metric();
                bpMorning.setType("BloodPressure");
                bpMorning.setValue("120/80");
                bpMorning.setUnit("mmHg");
                bpMorning.setTimestamp(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000)); // 2 hours ago
                bpMorning.setContext("morning");
                metrics2.add(bpMorning);

                SensorData.Metric bpEvening = new SensorData.Metric();
                bpEvening.setType("BloodPressure");
                bpEvening.setValue("130/85");
                bpEvening.setUnit("mmHg");
                bpEvening.setTimestamp(new Date(System.currentTimeMillis() - 30 * 60 * 1000)); // 30 min ago
                bpEvening.setContext("evening");
                metrics2.add(bpEvening);

                SensorData.Metric pulse = new SensorData.Metric();
                pulse.setType("Pulse");
                pulse.setValue(76);
                pulse.setUnit("bpm");
                pulse.setTimestamp(new Date(System.currentTimeMillis() - 30 * 60 * 1000));
                pulse.setContext("evening");
                metrics2.add(pulse);

                data2.setMetrics(metrics2);

                // Patient 3: Thermometer with two readings
                SensorData data3 = new SensorData();
                data3.setPatientId(patients.get(2).getPatientId());
                data3.setDeviceType("Thermometer");
                data3.setDeviceId("THERMO-003");
                data3.setRecordedDate(new Date());
                List<SensorData.Metric> metrics3 = new ArrayList<>();
                SensorData.Metric tempMorning = new SensorData.Metric();
                tempMorning.setType("Temperature");
                tempMorning.setValue(98.6);
                tempMorning.setUnit("F");
                tempMorning.setTimestamp(new Date(System.currentTimeMillis() - 3 * 60 * 60 * 1000)); // 3 hours ago
                tempMorning.setContext("morning");
                metrics3.add(tempMorning);

                SensorData.Metric tempEvening = new SensorData.Metric();
                tempEvening.setType("Temperature");
                tempEvening.setValue(99.2);
                tempEvening.setUnit("F");
                tempEvening.setTimestamp(new Date(System.currentTimeMillis() - 30 * 60 * 1000)); // 30 min ago
                tempEvening.setContext("evening");
                metrics3.add(tempEvening);

                data3.setMetrics(metrics3);

                sensorDataRepository.saveAll(Arrays.asList(data1, data2, data3));
            }
        };
    }
}