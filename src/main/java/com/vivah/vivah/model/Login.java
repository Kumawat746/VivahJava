package com.vivah.vivah.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity(name = "Login")
@Data
@Table(name = "login")
public class Login{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id ;

	
	private String email;
	private String phoneNumber;
	private String password;
	private String confirmPassword;
	
	
	
	
}
