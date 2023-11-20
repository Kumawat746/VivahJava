package com.vivah.vivah.servicetwo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vivah.vivah.Repository.RegistrationRepository;
import com.vivah.vivah.Repository.UserMappingRepository;
import com.vivah.vivah.model.UserMapping;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class RegistrationService {

	@Autowired
	private UserMappingRepository userMappingRepository;

	public boolean sendEmail(String subject, String message, String to) {
		String from = "anju20122001@gmail.com";

		boolean f = false;
		// Variables for email
		String host = "smtp.gmail.com";

		// Get the system properties
		Properties properties = System.getProperties();

		// Setting important information to properties object

		// Host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Step 1: Get the session object
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("anju20122001@gmail.com", "jiaufxftnpbnmwky");
			}
		});

		session.setDebug(true);

		// Compose the message
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(new InternetAddress(from));
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
//            mimeMessage.setText(message);
			mimeMessage.setContent(message, "text/html");

			// Send the message
			Transport.send(mimeMessage);
			System.out.println("Email sent successfully.");

			f = true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return f;
	}

}
