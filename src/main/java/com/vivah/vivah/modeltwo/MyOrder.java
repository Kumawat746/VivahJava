package com.vivah.vivah.modeltwo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "My_Order")
@Data
public class MyOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long MyOrderId;
	private String orderId;
	private String amount;
	private String receipt;
	private String status;
	
	@ManyToOne
	private User user;
	private String paymentId;
	
	
	

}
