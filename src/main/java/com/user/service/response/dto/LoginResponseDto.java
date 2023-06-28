package com.user.service.response.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto<T> {

	private T data;
	private String accessToken;
	private String refreshToken;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date createdAt;
}
