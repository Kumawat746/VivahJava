package com.vivah.vivah.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Otpclass")
@Data
public class Otpclass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int otpid;

    private String otp;

    // Constructors, getters, setters, etc.
}

