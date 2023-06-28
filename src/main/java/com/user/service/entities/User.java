package com.user.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	private String id;
	private String name;
	@Column(unique = true, nullable = false)
	private String email;
	private String userName;
	@Column(length = 30, nullable = false)
	private String password;
	private String address;
	private String role;
	private Boolean isVerified;
	private Integer otp;
	
	
}
