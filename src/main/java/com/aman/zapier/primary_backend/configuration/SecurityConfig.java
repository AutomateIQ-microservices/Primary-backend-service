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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.aman.zapier.primary_backend.jwt.AuthEntryPoint;
import com.aman.zapier.primary_backend.jwt.AuthTokenFilter;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    // Bean for AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // CORS configuration bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("http://localhost:3000"); // Adjust this if needed
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", corsConfig); // Adjust path as necessary
        
        return source;
    }
    
    
    // Define the SecurityFilterChain with necessary configurations
    @Bean
    public SecurityFilterChain mySecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/api/v1/users/signup").permitAll()
                    .requestMatchers("/api/v1/users/signin").permitAll()
                    .requestMatchers("/error").permitAll()
                    .anyRequest().authenticated())
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)  // Add AuthTokenFilter before UsernamePasswordAuthenticationFilter
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // Password Encoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
