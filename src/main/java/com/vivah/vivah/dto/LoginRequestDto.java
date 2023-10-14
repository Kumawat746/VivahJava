package com.vivah.vivah.dto;

public class LoginRequestDto {
	
	public LoginRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginRequestDto(String phoneNumber, String userName, String otp) {
		super();
		this.phoneNumber = phoneNumber;
		this.userName = userName;
		this.otp = otp;
	}

	private String phoneNumber;
	
	private String userName;
	
	private String otp;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "LoginRequestDto [phoneNumber=" + phoneNumber + ", userName=" + userName + ", otp=" + otp + "]";
	}
	
	

}
