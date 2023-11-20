package com.vivah.vivah.model;

import java.time.Instant;
import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "my_Order")
@Data
public class MyOrder {

	
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String orderId;
	    private String paymentId;
	    private String receipt;
	    private String status;
	    private String amount;
	    private String userId;
	    private String email;
	    
	    private ZonedDateTime startDate;
	    private ZonedDateTime endDate;
	

	

}
