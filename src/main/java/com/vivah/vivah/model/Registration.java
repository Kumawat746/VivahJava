package com.vivah.vivah.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//search commit

@Entity 
@Table(name = "registration")
@Data
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int  id;
	
	private String  userId;
	private String username;

	private String email;
	private String phoneNumber;
	private String password;
	private String confirmPassword;
	private String createProfileFor;
	private String gender;
	private String dateOfBirth;
	
    private boolean profileVisible;
    private Integer hidePhoneNumber = 0;
    private Integer albumPrivacy = 0;
    private Integer profileVisibility = 0;
    private LocalDate joinDate;

	
	
}