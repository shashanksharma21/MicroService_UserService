package com.user.service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.service.entities.User;
import com.user.service.request.dto.LoginRequestDto;
import com.user.service.request.dto.UserRequestDto;
import com.user.service.response.dto.LoginResponseDto;
import com.user.service.response.dto.UserResponseDto;
import com.user.service.services.LoginService;

@SpringBootTest
public class LoginControllerTest {

	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	

	private LoginRequestDto loginRequestDto;
	private LoginResponseDto loginResponseDto;
	private User user;

	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private LoginService loginService;
	
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		user = new User("3855a7f1-3ff9-439a-9afc-112366cc1303", "test1", "test1@dev.in", "test1", "test1", "indore", "ROLE_NORMAL", false, null);
		
		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setEmail("test1@dev.in");
		loginRequestDto.setPassword("test1");
		
		loginResponseDto = new LoginResponseDto(user, "token", "refresh_token", new Date());
	}
	
	@Test
	@DisplayName("test_login_user")
	void createUserTest() throws Exception {

		String jsonRequest = mapper.writeValueAsString(loginRequestDto);

		when(this.loginService.login(loginRequestDto)).thenReturn(loginResponseDto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/auth/login").content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", Matchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken", Matchers.notNullValue()))
				.andExpect(status().isOk());
	}
	
}
