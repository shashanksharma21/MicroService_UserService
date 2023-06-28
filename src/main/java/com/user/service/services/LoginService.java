package com.user.service.services;

import com.user.service.request.dto.LoginRequestDto;

public interface LoginService<T> {
	T login(LoginRequestDto request);

}
