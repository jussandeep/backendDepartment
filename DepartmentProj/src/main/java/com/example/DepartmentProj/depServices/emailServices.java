package com.example.DepartmentProj.depServices;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class emailServices {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(emailServices.class);
	@Autowired
	private JavaMailSender javaMailSender;

	
public void sendEmail(String to, String subject, String message) {
		
		try {
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(to);
			mail.setSubject(subject);
			mail.setText(message);
			
			javaMailSender.send(mail);
			logger.info("--------mail sent---------------------",mail);
			
		}catch (Exception e) {
			logger.error("Exception sending", e );
			 System.out.println("Exception while send Email: " + e );
			
		}
}
}
