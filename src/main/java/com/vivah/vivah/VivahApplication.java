package com.vivah.vivah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.twilio.Twilio;
import com.vivah.vivah.config.TwilioConfig;

import jakarta.annotation.PostConstruct;

@SpringBootApplication

public class VivahApplication {
	@Autowired
	private TwilioConfig twilioConfig;

	@PostConstruct
	public void initTwilio() {
		Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

	}

	public static void main(String[] args) {
		SpringApplication.run(VivahApplication.class, args);
	}
}
