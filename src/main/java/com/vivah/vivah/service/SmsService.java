package com.vivah.vivah.service;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vivah.vivah.model.SmsPojo;
import com.vivah.vivah.model.StoreOtp;
@Service
public class SmsService extends StoreOtp {
	 StoreOtp store = new StoreOtp();
    private final String ACCOUNT_SID = "AC3b197eff4c2741a8dad0e49a22995bde";
    private final String AUTH_TOKEN = "9d55e10d87f251791c9d38b3e8001ca7";
    private final String FROM_NUMBER="+15734923751";
    public void sentOtp(SmsPojo sms) {
        // Initialize Twilio with your credentials
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
 
        // Generate a random OTP
        int min = 100000;
        int max = 999999;
        int number = (int) (Math.random() * (max - min + 1) + min);

        // Compose the OTP message
        String otpMsg = "Dear customer, your bank account reset password OTP is " + number;

        
     // Send the OTP message using Twilio
        @SuppressWarnings("unused")
		Message message = Message.creator(
            new PhoneNumber((String)sms.getPhoneNumber()), // Recipient's phone number
            new PhoneNumber(FROM_NUMBER), // Your Twilio phone number
            otpMsg // OTP message
        ).create();
            
        
            StoreOtp.setOtp(number);
        
        

    }
    
  public boolean   verifyOtp(String userOtp ){
	 int userOtp2= Integer.parseInt(userOtp);
	  
	  boolean  message;
	  
	int  storeOtp =StoreOtp.getOtp();
	
	System.out.println(storeOtp);
	  if (userOtp2==storeOtp) {
		  
		 message=true;
	  }
	  else {
	 message=false; 
		  
		  
	  }
	return message;
    	
    	
    	
    	
    	
    	
    }
            
        
        
      
    
    public void Received(MultiValueMap<String, String> smsCallback) {
 	   
 	   
   	 System.out.println("No SMS callback data received.");
	   
	   
	   
	   
	   
  }
}
