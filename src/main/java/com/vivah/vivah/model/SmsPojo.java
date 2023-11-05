package com.vivah.vivah.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//search commit
@Entity
@Table(name = "smspojo")
@Data
public class SmsPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // You can use a suitable data type for your primary key

	private String phoneNumber;
	private String userName;

	private String profileType;
	private String email;
	private String newPassword;
	private String Otp;
	private String rememberMe;

}
