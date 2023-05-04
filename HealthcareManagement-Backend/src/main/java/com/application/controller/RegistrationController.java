package com.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.Doctor;
import com.application.model.EmailDetails;
import com.application.model.Slots;
import com.application.model.User;
import com.application.repository.EmailService;
import com.application.service.DoctorRegistrationService;
import com.application.service.UserRegistrationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
public class RegistrationController {
	@Autowired
	private UserRegistrationService userRegisterService;

	@Autowired
	private DoctorRegistrationService doctorRegisterService;

	@Autowired
	private EmailService emailService;

	@Value("${spring.mail.username}")
	private String sender;

	@Autowired
	private JavaMailSender javaMailSender;

	@PostMapping("/registeruser")
	@CrossOrigin(origins = "http://localhost:4200")
	public User registerUser(@RequestBody User user, EmailDetails details) throws Exception {

		String name = user.getUsername();
		String ph = user.getMobile();

		String currEmail = user.getEmail();
		if (currEmail != null || !"".equals(currEmail)) {
			User userObj = userRegisterService.fetchUserByEmail(currEmail);
			if (userObj != null) {
				throw new Exception("User with " + currEmail + " already exists !!!");
			}
		}
		SimpleMailMessage mailmessage = new SimpleMailMessage();
		mailmessage.setFrom(sender);
		mailmessage.setTo(user.getEmail());
		mailmessage.setText("Dear " + name + "," + " \n"
				+ "Thank you for Creating your account with us you can login by clicking the below link\n"
				+ "http://localhost:4200 \n" + "Thanks \n" + "EMR Technologies");
		mailmessage.setSubject("EMR-Account Created Successfully");
		System.out.println("here");

		javaMailSender.send(mailmessage);
		Twilio.init("ACa8c5d8dc91577707bc8069fcb46c27d9", "f9e0cca63a8c06408f3078932a8d6fba");
		Message.creator(new PhoneNumber(ph), new PhoneNumber("5074362609"), "Dear " + name + ","
				+ "\nThank your for creating your account with us your account is activated now and can access using the below link\n"
				+ "\nhttp://localhost:4200").create();
		System.out.println("1");
		User userObj = null;
		userObj = userRegisterService.saveUser(user);
		return userObj;
	}

	@PostMapping("/registerdoctor")
	@CrossOrigin(origins = "http://localhost:4200")
	public Doctor registerDoctor(@RequestBody Doctor doctor) throws Exception {
		String currEmail = doctor.getEmail();
		if (currEmail != null || !"".equals(currEmail)) {
			Doctor doctorObj = doctorRegisterService.fetchDoctorByEmail(currEmail);
			if (doctorObj != null) {
				throw new Exception("Doctor with " + currEmail + " already exists !!!");
			}
		}
		SimpleMailMessage mailmessage = new SimpleMailMessage();
		mailmessage.setFrom(sender);
		mailmessage.setTo(doctor.getEmail());
		String name=doctor.getDoctorname();
		mailmessage.setText("Dear " + name + "," + " \n"
				+ "Thank you for Creating your Doctor account with us,Please give us some time to verify.Once your account,is verified you will be informed so that you can login by clicking the below link\n"
				+ "http://localhost:4200 \n" + "Thanks \n" + "EMR Technologies");
		mailmessage.setSubject("EMR-Account Created Successfully");
		javaMailSender.send(mailmessage);
		System.out.println("here");
		Doctor doctorObj = null;
		doctorObj = doctorRegisterService.saveDoctor(doctor);
		return doctorObj;
	}

	@PostMapping("/addDoctor")
	@CrossOrigin(origins = "http://localhost:4200")
	public Doctor addNewDoctor(@RequestBody Doctor doctor) throws Exception {
		Doctor doctorObj = null;
		doctorObj = doctorRegisterService.saveDoctor(doctor);
		SimpleMailMessage mailmessage = new SimpleMailMessage();
		mailmessage.setFrom(sender);
		mailmessage.setTo(doctor.getEmail());
		String name=doctor.getDoctorname();
		mailmessage.setText("Dear " + name + "," + " \n"
				+ "Thank you for Creating your Doctor account with us,Please give us some time to verify.Once your account,is verified you will be informed so that you can login by clicking the below link\n"
				+ "http://localhost:4200 \n" + "Thanks \n" + "EMR Technologies");
		mailmessage.setSubject("EMR-Account Created Successfully");
		javaMailSender.send(mailmessage);
		return doctorObj;
		
	}

	@GetMapping("/gettotalusers")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Integer>> getTotalSlots() throws Exception {
		List<User> users = userRegisterService.getAllUsers();
		List<Integer> al = new ArrayList<>();
		al.add(users.size());
		return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
	}

	@PostMapping("/sendMail")
	public String sendMail(@RequestBody EmailDetails details) {
		String status = emailService.sendSimpleMail(details);

		return status;
	}

}
