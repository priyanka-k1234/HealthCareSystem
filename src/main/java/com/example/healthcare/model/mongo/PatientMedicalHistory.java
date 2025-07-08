package com.example.healthcare.model.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "PatientMedicalHistory")
@Getter @Setter @NoArgsConstructor
public class PatientMedicalHistory {
    @Id
    private String id;
    private Long patientId;
    private List<String> allergies;
    private List<String> conditions;
    private List<Medication> medications;
    private List<Surgery> surgeries;
    private Date lastUpdated;

    @Getter @Setter @NoArgsConstructor
    public static class Medication {
        private String name;
        private String dosage;
        private String frequency;
        private String prescribedBy;
        private Date startDate;
        private Date endDate;
    }

    @Getter @Setter @NoArgsConstructor
    public static class Surgery {
        private String name;
        private Date date;
        private String hospital;
        private String notes;
    }
}