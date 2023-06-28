package com.user.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.user.service.jwt.JwtAuthenticationEntryPoint;
import com.user.service.jwt.JwtRequestFilter;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint entryPoint;
	
	
	@Autowired
	private JwtRequestFilter requestFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity
			.cors().disable()
			.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/auth/login").permitAll()
			.requestMatchers("/api/v1/users/register").permitAll()
			.requestMatchers("/api/v1/users/get-all-users").permitAll()
			.requestMatchers("/actuator/**").permitAll()
			.anyRequest()
			.authenticated().and()
			.exceptionHandling()
			.authenticationEntryPoint(entryPoint)
			.and()
			.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		return httpSecurity.build();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
