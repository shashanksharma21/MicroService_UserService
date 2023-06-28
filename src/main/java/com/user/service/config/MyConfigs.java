package com.user.service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfigs {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
