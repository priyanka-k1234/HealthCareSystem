package com.example.healthcare.model.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "SensorData")
@Getter @Setter @NoArgsConstructor
public class SensorData {
    @Id
    private String id;
    private Long patientId;
    private String deviceType;
    private String deviceId;
    private Date recordedDate;
    private List<Metric> metrics;

    @Getter @Setter @NoArgsConstructor
    public static class Metric {
        private String type;
        private Object value;
        private String unit;
        private Date timestamp;
        private String context;
    }
}