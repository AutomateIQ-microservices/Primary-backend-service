package com.aman.zapier.primary_backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aman.zapier.primary_backend.JPArepositories.UserRepository;
import com.aman.zapier.primary_backend.entities.User;
import com.aman.zapier.primary_backend.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	//create new user
	public String RegiterUser(String name,String email,String password) {
		Optional<User> existingUser=userRepo.findByEmail(email);
		if(existingUser.isPresent()) {
			throw new RuntimeException("User already exists");
		}
		
		User user=new User();
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setName(name);
			
		User createdUser=userRepo.save(user);
			
		String token=jwtUtil.generateTheToken(createdUser.getName(),createdUser.getId().longValue());
		return token;
	}
	
	//verify existing user for login
	public String LoginUser(String email,String password) {
		User user=userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
		
		//the .match() converts the raw password to the hashed one and compares with the user.getPassword() 
		if(passwordEncoder.matches(password, user.getPassword())) {
			return jwtUtil.generateTheToken(user.getName(), user.getId().longValue());
		}
		else {
			throw new RuntimeException("Invalid User credential");
		}	
	}
	
	//return user email and name based on id
	public User returnUser(HttpServletRequest request) {
		Integer userId=(Integer)request.getAttribute("id");
		User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
		
		return user;
	}
}
