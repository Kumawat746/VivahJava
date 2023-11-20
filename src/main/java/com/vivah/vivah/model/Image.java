package com.vivah.vivah.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Lob
    private byte[] data;

    // getters and setters
}
