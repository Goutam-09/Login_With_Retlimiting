package com.erp.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.erp.exception.HandleAuthException;
import com.erp.exception.HandleLockedException;
import com.erp.models.ApiResponse;
import com.erp.service.IAuthService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RateLimiterService rateLimitService;

	@Override
	public ApiResponse loginUser(String username, String password) {

        if (rateLimitService.isRateLimited(username)) {
	           throw new HandleLockedException("Too Many Requests");
	        }
	        try {
	            Authentication authentication = authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(username, password)
	            );

	            SecurityContextHolder.getContext().setAuthentication(authentication);
	            rateLimitService.recordLoginAttempt(username, authentication.isAuthenticated());
	            Map<String, Object> map = new HashMap<>();
	            map.put("username", authentication.getName());
	            map.put("role", authentication.getAuthorities());
	            log.info("User Name: {}",authentication.getName());
	            log.info("Time Stamp: {}",LocalDateTime.now());
	            return ApiResponse.builder()
	            		.message("Login Successfully...")
	            		.data(map)
	            		.build();

	        } catch (AuthenticationException ex) {
	            log.error("User Name: {}",username);
	            log.error("Time Stamp: {}",LocalDateTime.now());
	        	rateLimitService.recordLoginAttempt(username, false);
	        	throw new HandleAuthException("Invalid Credentials");
	        }
	}

}
