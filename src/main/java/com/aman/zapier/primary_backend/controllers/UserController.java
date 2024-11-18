package com.aman.zapier.primary_backend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aman.zapier.primary_backend.entities.User;
import com.aman.zapier.primary_backend.services.UserService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path="/api/v1/users")
@CrossOrigin(origins="*")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping(path="/signup")
	public ResponseEntity<String> Signup(@RequestBody Map<String,Object> requestBody) {
		String name=(String)requestBody.get("name");
		String email=(String)requestBody.get("email");
		String password=(String)requestBody.get("password");
		
		try {
			String Jwttoken=userService.RegiterUser(name, email, password);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(Jwttoken);
		}
		catch(RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
	}
	
	@PostMapping(path="/signin")
	public ResponseEntity<String> Signin(@RequestBody Map<String,Object> requestBody){
		String email=(String)requestBody.get("email");
		String password=(String)requestBody.get("password");
		
		try {
			String JwtToken=userService.LoginUser(email, password);
			return ResponseEntity.status(HttpStatus.OK).body(JwtToken);
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	}
	
	@GetMapping(path="/user")
	public ResponseEntity<Map<String, String>> returnUser(HttpServletRequest request) {
		User foundUser=userService.returnUser(request);
		
		Map<String,String> responseMap=new HashMap<>();
		responseMap.put("email", foundUser.getEmail());
		responseMap.put("name", foundUser.getName());
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMap);
	}
	
	
}
