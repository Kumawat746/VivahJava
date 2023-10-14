package com.vivah.vivah.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;
@Configuration
public class TwilioRouterConfig {
	

	@Autowired
	private TwilioOtpHandler handler;
@Bean
	public RouterFunction<ServerResponse> handleSms() {
	    return RouterFunctions.route()
	            .POST("/router/sendOtp", handler::sentOtp)
	            .POST("/router/validateOtp", handler::ValidateOtp)
	            .build();
	}

	}
	
	
	 


