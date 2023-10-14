package com.vivah.vivah.dto;

public class OtpResponseDto {
	
	public OtpResponseDto(OtpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public OtpResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	private OtpStatus status;
	private String message;
	public OtpStatus getStatus() {
		return status;
	}
	public void setStatus(OtpStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "OtpResponseDto [status=" + status + ", message=" + message + "]";
	}
	
	
	

}
