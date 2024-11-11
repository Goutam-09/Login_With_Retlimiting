package com.erp.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testSuccessfulLogin() throws Exception {
		mockMvc.perform(post("/auth/login").param("username", "GoutamBarfa").param("password", "12345"))
				.andExpect(status().isOk());

	}

	@Test
	public void testRateLimitExceeded() throws Exception {
		for (int i = 0; i < 5; i++) {
			mockMvc.perform(post("/auth/login").param("username", "user").param("password", "wrongpassword"))
					.andExpect(status().isUnauthorized());
		}

		mockMvc.perform(post("/auth/login").param("username", "user").param("password", "wrongpassword"))
				.andExpect(status().isLocked());
	}

	@Test
	public void testRateLimitResetsAfterSuccess() throws Exception {
		for (int i = 0; i < 5; i++) {
			mockMvc.perform(post("/auth/login").param("username", "user").param("password", "wrongpassword"))
					.andExpect(status().isUnauthorized());
		}

		mockMvc.perform(post("/auth/login").param("username", "user").param("password", "password"))
				.andExpect(status().isOk());

	}
}
