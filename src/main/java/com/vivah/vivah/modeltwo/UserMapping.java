package com.vivah.vivah.modeltwo;

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
	
	private Long userId;
	private Long accepted_mem ;
	private Long declined_mem;
	private Long shortlisted_mem;
	private Long send_mem;
	private Long received_mem;
	private Long blocked_mem;
	
	
	

}
