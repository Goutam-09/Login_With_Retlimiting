package com.erp.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
	
	private final Map<String, UserLoginAttempt> loginAttempts = new HashMap<>();
	private static final int MAX_ATTEMPTS = 5;
	private static final long WINDOW_TIME = TimeUnit.MINUTES.toMillis(15); 
	private static final long COOLDOWN_TIME = TimeUnit.MINUTES.toMillis(30); 

	public boolean isRateLimited(String key) {
		UserLoginAttempt attempt = loginAttempts.getOrDefault(key, new UserLoginAttempt());
		
		if (attempt.getAttemptCount() > MAX_ATTEMPTS
				&& (System.currentTimeMillis() - attempt.getFirstAttemptTime() < COOLDOWN_TIME)) {
			return true;
		}

		if (attempt.getAttemptCount() >= MAX_ATTEMPTS
				&& System.currentTimeMillis() - attempt.getFirstAttemptTime() < WINDOW_TIME) {
			attempt.incrementAttempts();
			return true;
		}

		return false;
	}

	public void recordLoginAttempt(String key, boolean success) {
		UserLoginAttempt attempt = loginAttempts.getOrDefault(key, new UserLoginAttempt());

		if (success) {
			loginAttempts.put(key, new UserLoginAttempt());
		} else {
			long currentTime = System.currentTimeMillis();
			if (currentTime - attempt.getFirstAttemptTime() > WINDOW_TIME) {
				attempt.reset(currentTime);
			}
			attempt.incrementAttempts();
			loginAttempts.put(key, attempt);
		}
	}

	private static class UserLoginAttempt {
		private long firstAttemptTime;
		private int attemptCount;

		public UserLoginAttempt() {
			this.firstAttemptTime = System.currentTimeMillis();
			this.attemptCount = 0;
		}

		public long getFirstAttemptTime() {
			return firstAttemptTime;
		}

		public int getAttemptCount() {
			return attemptCount;
		}

		public void incrementAttempts() {
			attemptCount++;
		}

		public void reset(long currentTime) {
			this.firstAttemptTime = currentTime;
			this.attemptCount = 0;
		}
	}
}
