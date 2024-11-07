package com.example.DepartmentProj.scheduleJobs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.DepartmentProj.depServices.emailServices;




@Component
public class scheduleClass {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(scheduleClass.class);

	@Autowired
	private emailServices emailServ;
	
	@Scheduled (cron = "0 16 12 * * * ")
	public void scheduler() throws InterruptedException{
		
		LocalDateTime current = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		
		String formattedDateTime = current.format(format);
	
		logger.info("Scheduler time " + formattedDateTime);
		
//		Thread.sleep(60000);
		
		
		//combine message with the formatted date-time
		String message = ("This is a scheduled email sent at: "+ formattedDateTime);
		
//		call emailServ         to address                 subject           message or body
		emailServ.sendEmail("saisandeep9866@gmail.com", "Scheduled Email", message);
		logger.info("Email sent at " + LocalDateTime.now().format(format));	
	
	
	
	}
}
