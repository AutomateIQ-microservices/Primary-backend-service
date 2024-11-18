package com.aman.zapier.primary_backend.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private static final Logger logger=LoggerFactory.getLogger(JwtUtil.class);
	
	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;
	
	private final long expirationTime=1000*60*60*10;
	
	//generate the token method
	public String generateTheToken(String name,Long userId) {
		return Jwts.builder().subject(name)
				.claim("userId",userId)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+expirationTime))
				.signWith(key())
				.compact();
	}
	
//	private Key key() {
//		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//	}
	private Key key() {
	    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));  // jwtSecret is your base64-encoded secret
	}

	
	//extract all claims from jwt
	public Claims extractAllClaims(String token) {
		try {
			Claims claims=Jwts.parser()
					.verifyWith((SecretKey) key())
					.build().parseSignedClaims(token)
					.getPayload();
			return claims;
		}
		catch(Exception e) {
			logger.error("Error parsing token: {}", e.getMessage());
	        throw e;
		}

	}
	
	//extract userId from the claims
	public Long getUserId(String token) {
		Object userId = extractAllClaims(token).get("userId");
		
		if (userId != null) {
	        logger.debug("Extracted userId: {} (type: {})", userId, userId.getClass());
	    } else {
	        logger.error("userId claim is missing in the token");
	    }
		
	    if (userId instanceof Integer) {
	        return ((Integer) userId).longValue(); // Convert Integer to Long
	    } else if (userId instanceof Long) {
	        return (Long) userId;
	    } else {
	        throw new RuntimeException("Invalid userId type in JWT token");
	    }
	}
	//extract subject (username)
	public String getSubject(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	//check expiration
	public Date getExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}
	public boolean isExpired(String token) {
		return getExpiration(token).before(new Date());
	}
	
	//validate the token
	public boolean validateJwtToken(String token) {
		try {
			Jwts.parser().verifyWith((SecretKey) key()).build()
			.parseSignedClaims(token);
			return true;
		}catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
		
		return false;
	}
}
