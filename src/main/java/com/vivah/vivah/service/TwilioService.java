package com.vivah.vivah.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vivah.vivah.config.TwilioConfig;
import com.vivah.vivah.dto.*;
import reactor.core.publisher.Mono;

@Service
public class TwilioService {
	Map<String, String> otpmap = new HashMap<>();
	@Autowired
	TwilioConfig twilioconfig;

	private String GenerateOtp() {

		return new DecimalFormat("000000").format(new Random().nextInt(999999));

	}

	public Mono<OtpResponseDto> SendOtpForlogin(LoginRequestDto loginRequestDto) {
		OtpResponseDto OtpResponseDto = null;
		try {
			String otp = GenerateOtp();
			String optMessageString = "dear customer your bank acound reset password otp is  " + otp;
			PhoneNumber from = new PhoneNumber(twilioconfig.gettrialNumber());
			PhoneNumber to = new PhoneNumber(loginRequestDto.getPhoneNumber());

			@SuppressWarnings("unused")
			Message message = Message.creator(to, from, optMessageString).create();
			otpmap.put(loginRequestDto.getUserName(), otp);
			OtpResponseDto = new OtpResponseDto(OtpStatus.Delivered, optMessageString);

		} catch (Exception exception) {

			OtpResponseDto = new OtpResponseDto(OtpStatus.failed, exception.getMessage());

		}
		return Mono.just(OtpResponseDto);
	}

	public Mono<String> ValidateOtp(String userInputOtp, String userName) {

		if (userInputOtp.equals(otpmap.get(userName))) {

			return Mono.just("valid");

		} else {

			return Mono.error(new IllegalArgumentException("invalid"));
		}

	}
}
