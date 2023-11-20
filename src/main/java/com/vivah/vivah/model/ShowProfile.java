package com.vivah.vivah.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "show_profile")

@Data
public class ShowProfile {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private String userId;

    private String userName;

    private boolean profileVisible;

    private LocalDate hiddenUntil;

    private LocalDateTime lastHideDate;

    private LocalDateTime lastUnhideDate;



}
