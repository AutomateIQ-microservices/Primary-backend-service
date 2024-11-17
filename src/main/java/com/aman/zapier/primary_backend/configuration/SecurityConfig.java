package com.aman.zapier.primary_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aman.zapier.primary_backend.jwt.AuthEntryPoint;
import com.aman.zapier.primary_backend.jwt.AuthTokenFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;


@Configuration //tells the spring application that this class provides configuration
@EnableWebSecurity //enable this web security class in the application
public class SecurityConfig {
	
	@Autowired
	private AuthTokenFilter authTokenFilter;
	
	@Autowired 
	private AuthEntryPoint authEntryPoint;
	
	//to use in service
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	   return authConfig.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain mySecurityFilterChain(HttpSecurity http) throws Exception{
		
		http
			.csrf(csrf->csrf.disable())
			
			// Handle unauthorized exceptions
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            
            // Stateless session management Configured as SessionCreationPolicy.STATELESS to ensure no session is created or used by Spring Security.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(authorizeRequests ->
            			authorizeRequests.requestMatchers("/api/v1/users/signup").permitAll()
            			.requestMatchers("/api/v1/users/signin").permitAll()
            			.anyRequest().authenticated())
            
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

		
		return http.build();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
