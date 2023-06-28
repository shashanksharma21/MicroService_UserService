package com.user.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.request.dto.UserRequestDto;
import com.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api/v1/users")
public class UserController<T> {

	@Autowired
	private UserService userService;
	
	// REGISTER
	@PostMapping("/register")
	public ResponseEntity<T> registerUser(@RequestBody UserRequestDto userRequestDto) {
		return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.CREATED).body(this.userService.registerUser(userRequestDto));
	}
	
	// UPDATE
	@PutMapping("/update/{userId}")
	public ResponseEntity<T> updateUser(@RequestBody UserRequestDto userRequestDto, @PathVariable String userId) {
		return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(userRequestDto, userId));
	}
	
	// DELETE
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<T> deleteUser(@PathVariable String userId) {
		return (ResponseEntity<T>) ResponseEntity.ok(this.userService.deleteUser(userId));
	}
	
	// GET ALL
	@GetMapping("/get-all-users")
	public ResponseEntity<T> getAllUsers() {
		return (ResponseEntity<T>) ResponseEntity.ok(this.userService.getAllUsers());
	}
	
	// GET BY ID
	@CircuitBreaker(name = "userProductBreaker", fallbackMethod = "userProductFallBack")
	@GetMapping("/get-id/{userId}")
	public ResponseEntity<T> getUserById(@PathVariable String userId) {
		return (ResponseEntity<T>) ResponseEntity.ok(this.userService.getUserById(userId));
	}
	
	// circuitBrakerFallback_Method
	public ResponseEntity<T> userProductFallBack(@PathVariable String userId, Exception e) {
		return (ResponseEntity<T>) ResponseEntity.ok(this.userService.getUserWithoutProduct(userId));
	}
}
