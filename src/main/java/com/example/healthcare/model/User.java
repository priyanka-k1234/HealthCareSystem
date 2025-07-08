package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 256)
    private String passwordHash;

    @Column(nullable = false)
    private String role; // "ADMIN", "DOCTOR", "PATIENT"

    private String mfaSecret;

    // --- Added fields ---
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
}