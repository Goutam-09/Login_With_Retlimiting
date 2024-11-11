package com.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.models.ApiResponse;
import com.erp.service.IAuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private IAuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestParam String username, @RequestParam String password) {
		return new ResponseEntity<>(
				authService.loginUser(username, password)
				,HttpStatus.OK);
	}

}
