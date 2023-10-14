package com.vivah.vivah.model;
//LoginRequest.java
public class LoginRequest {
public LoginRequest() {
	
		// TODO Auto-generated constructor stub
	}
public LoginRequest(String phoneNumber, String gmail) {   
		super();
		this.phoneNumber = phoneNumber;
		this.gmail = gmail;
	}
private String phoneNumber;
private String gmail;
public String getPhoneNumber() {
	return phoneNumber;
}
public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}
public String getGmail() {
	return gmail;
}
public void setGmail(String gmail) {
	this.gmail = gmail;
}
@Override
public String toString() {
	return "LoginRequest [phoneNumber=" + phoneNumber + ", gmail=" + gmail + "]";
}

// Getters and setters

// Constructor

// Other properties, getters, and setters
}

