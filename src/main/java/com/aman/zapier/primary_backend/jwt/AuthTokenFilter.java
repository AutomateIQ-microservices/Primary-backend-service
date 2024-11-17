package com.aman.zapier.primary_backend.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) 
	throws ServletException,IOException
	{
		logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
		try {
			String token=extractTokenFromRequest(request);
			if(token!=null && jwtUtil.validateJwtToken(token)) {
				Long userId=jwtUtil.getUserId(token);
				if(userId!=null) {
					request.setAttribute("id", userId);
				}
			}
		}catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
		
		filterChain.doFilter(request, response);
		
		
		
	}
	
	private String extractTokenFromRequest(HttpServletRequest request) {
		String token=request.getHeader("Authorization");
		if(token!=null && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}
}
