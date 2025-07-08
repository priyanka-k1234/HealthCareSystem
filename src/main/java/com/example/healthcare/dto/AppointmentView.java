package com.example.healthcare.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AppointmentView {
    private final LocalDateTime dateTime;
    private final String reason;
    private final String status;
    private final String patientName;
    private final String doctorName;

    public AppointmentView(LocalDateTime dateTime, String reason, String status, String patientName, String doctorName) {
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = status;
        this.patientName = patientName;
        this.doctorName = doctorName;
    }

    public LocalDateTime getDateTime() { return dateTime; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }

    // This is the key: convert LocalDateTime to java.util.Date for Thymeleaf
    public Date getDateTimeAsDate() {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}