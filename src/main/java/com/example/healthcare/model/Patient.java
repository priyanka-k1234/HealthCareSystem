package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter @Setter @NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(nullable=false)
    private String firstName;

    @Column(nullable=false)
    private String lastName;

    @Column(nullable=false)
    private LocalDate dateOfBirth;

    @Column(nullable=false)
    private String gender;

    @Column(nullable=false)
    private String bloodType;

    private String phone;
    private String email;
    private String address;
}