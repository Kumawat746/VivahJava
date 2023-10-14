package com.vivah.vivah.modeltwo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "phonebook")
@Data
public class PhoneBook {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

		private Long id;
	private String userid;
		private String name;
		private String email;
		private String phoneNumber;


}
