package com.vivah.vivah.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix ="twilio")
public class TwilioConfig {
  private String accountSid;
  private String authToken;
  private String trialNumber;

  // Getters and setters for accountSid, authToken, and trialNumber

  public String getAccountSid() {
    return accountSid;
  }

  public void setAccountSid(String accountSid) {
    this.accountSid = accountSid;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public String gettrialNumber() {
    return trialNumber;
  }

  public void settrialNumber(String trialNumber) {
    this.trialNumber = trialNumber;
  }
}
