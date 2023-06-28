package com.user.service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

	private String name;
	private String email;
	private String userName;
	private String password;
	private String address;	
}
