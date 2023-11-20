package com.vivah.vivah.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_mapping")
@Data
public class UserMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_mappingId;
	
	private String userId;
	private String accepted_mem ;
	private String declined_mem;
	private String shortlisted_mem;
	private String send_mem;
	private String received_mem;
	private String blocked_mem;
	
	
	

}
