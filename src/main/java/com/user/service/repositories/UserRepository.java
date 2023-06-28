package com.user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.user.service.entities.User;

public interface UserRepository extends JpaRepository<User, String>{

	@Query(nativeQuery =  true, value = "select * from user_details u where u.email = :email")
	public User findByEmail(String email);
	
	@Query(nativeQuery =  true, value = "select * from user_details u where u.user_name = :username")
	public User findByUserName(String username);
	
}
