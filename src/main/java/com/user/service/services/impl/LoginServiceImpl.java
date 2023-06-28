package com.user.service.services.impl;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.user.service.entities.User;
import com.user.service.jwt.util.JwtTokenUtil;
import com.user.service.repositories.UserRepository;
import com.user.service.request.dto.CommonErrorResponse;
import com.user.service.request.dto.LoginRequestDto;
import com.user.service.response.dto.LoginResponseDto;

import com.user.service.services.LoginService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@SuppressWarnings("unchecked")
public class LoginServiceImpl<T> implements LoginService {

	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	
	@Override
	public T login(LoginRequestDto request) {
		log.info("Login Service :: Login Method");
		
		if(request.getEmail()== null && request.getPassword() == null ) {
			log.error("LoginService :: Login() :: Please Enter Email or Username !!");
			return (T) new CommonErrorResponse("Please Enter Email or Username !!", false, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
		}
		
		if( (request.getEmail() == null || request.getPassword() == null) && request.getPassword() == null ) {
			log.error("LoginService :: Login() :: Please Enter Email And Password !!");
			return (T) new CommonErrorResponse("Please Enter Email or Username !!", false, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
		}
		
		User user = null ;
		user = request.getEmail() == null ? this.userRepository.findByUserName(request.getUsername()) : this.userRepository.findByEmail(request.getEmail());
		log.info("LoginService :: Logged in User is {}", user);
		
		if( user == null) {
			log.info("LoginService :: User Not Fount with this email or username");
			return (T) new CommonErrorResponse("User not found with this Email or Username !!", false, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
		}
		
		if( !user.getPassword().equals(request.getPassword())) {
			log.info("LoginService :: Invalid Password !!");
			return (T) new CommonErrorResponse("Invalid Password !!", false, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
		}
		
		log.info("LoginService :: Password Matched !!");
		
		String reqUserName ;
		if( request.getEmail() == null) reqUserName=request.getUsername();
		else reqUserName = request.getEmail();	
		
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(reqUserName, request.getPassword(), true, true, true, true, new ArrayList<>());
		
		String token = this.jwtTokenUtil.generateToken(userDetails);
		String refreshToken = this.jwtTokenUtil.generateRefreshToken(userDetails);
		
		return (T) new LoginResponseDto(user, token, refreshToken, new Date());
	}


	
}
