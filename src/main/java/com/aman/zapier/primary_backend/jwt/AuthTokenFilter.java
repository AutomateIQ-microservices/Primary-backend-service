package com.aman.zapier.primary_backend.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/v1/users/signin") || path.equals("/api/v1/users/signup");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            String token = extractTokenFromRequest(request);
            if (token != null && jwtUtil.validateJwtToken(token)) {
                Long userId = jwtUtil.getUserId(token);

                if (userId != null) {
                    request.setAttribute("id", userId);

                    // Manually create an authentication token
                    User principal = new User(userId.toString(), "", AuthorityUtils.createAuthorityList("ROLE_USER"));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, AuthorityUtils.createAuthorityList("ROLE_USER"));

                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authentication successfully set in SecurityContext for userId: {}", userId);
                } else {
                    logger.error("userId is null, cannot set attribute");
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            logger.debug("Authorization header is missing for URI: {}", request.getRequestURI());
        } else {
            logger.debug("Authorization token: {}", token);
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.debug("Extracted Token: {}", token);
            return token;
        }
        return null;
    }
}
