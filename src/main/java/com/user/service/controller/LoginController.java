package com.user.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.request.dto.LoginRequestDto;
import com.user.service.services.LoginService;


@RestController
@RequestMapping("/auth")
@SuppressWarnings("unchecked")
public class LoginController<T> {


	@Autowired
	private LoginService<T> loginService;
	
	@PostMapping("/login")
	public ResponseEntity<T> login(@RequestBody LoginRequestDto loginRequest) {
		Object login = this.loginService.login(loginRequest);
		return (ResponseEntity<T>) ResponseEntity.ok(login);
	}
}
