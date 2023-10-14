package com.vivah.vivah.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vivah.vivah.dto.LoginRequestDto;
import com.vivah.vivah.service.TwilioService;

import reactor.core.publisher.Mono;


import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.http.HttpStatus;
@Component
public class TwilioOtpHandler {
	@Autowired
	private TwilioService twilioService;
		
	public Mono<ServerResponse> sentOtp(ServerRequest serverRequest) {
		
		
		return serverRequest.bodyToMono(LoginRequestDto.class)
			    .flatMap(dto -> twilioService.SendOtpForlogin(dto))
			    .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
			        .body(BodyInserters.fromValue(dto)));

		
		
		
	}
	
	public Mono<ServerResponse> ValidateOtp(ServerRequest serverRequest) {
		
		
		
		return serverRequest.bodyToMono(LoginRequestDto.class)
			    .flatMap(dto -> twilioService.ValidateOtp(dto.getOtp(),dto.getUserName()))
			    .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
			        .bodyValue(dto));


		
		
	}
		
	}
	
	
	


