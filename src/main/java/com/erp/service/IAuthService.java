package com.erp.service;

import com.erp.models.ApiResponse;

public interface IAuthService {

	public ApiResponse loginUser(String username, String password);
}
