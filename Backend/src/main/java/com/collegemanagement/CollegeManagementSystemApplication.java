package com.collegemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CollegeManagementSystemApplication
{
	public static void main(String[] args)
	{
	   PasswordEncoder encoder = new BCryptPasswordEncoder();

	    String pass  = "admin1234";
	    System.out.println(encoder.encode(pass));
		SpringApplication.run(CollegeManagementSystemApplication.class, args);
	}
}
