package com.user.service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.service.entities.User;
import com.user.service.request.dto.UserRequestDto;
import com.user.service.response.dto.UserResponseDto;
import com.user.service.services.UserService;

@SpringBootTest
public class UserControllerTest {

	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;

	private User user;
	private UserRequestDto userRequestDto;
	private UserResponseDto userResponseDto;
	private List<User> users;
	private List<UserResponseDto> userResponseDtos;

	@Autowired
	private ObjectMapper mapper;
	
	

	@MockBean
	private UserService userService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		user = new User("3855a7f1-3ff9-439a-9afc-112366cc1303", "test1", "test1@dev.in", "test1", "test1", "indore", "ROLE_NORMAL", false, null);
		
		userRequestDto = new UserRequestDto("Test 1", "test1@dev.in", "test1", "test1", "indore");
		
		userResponseDto = new UserResponseDto("3855a7f1-3ff9-439a-9afc-112366cc1303", "Test 1", "test1@dev.in", "test1", "indore", new ArrayList<>());
		
		users = Stream
				.of(new User("3855a7f1-3ff9-439a-9afc-112366cc1303", "Test 1", "test1@dev.in", "test1", "test1", "indore", "ROLE_NORMAL", false, null),
						new User("3855a7f1-3ff9-439a-9afc-112366cc1303", "Test 2", "test2@dev.in", "test2", "test2", "indore", "ROLE_NORMAL", false, null))
				.collect(Collectors.toList());
		
		userResponseDtos = Stream
				.of(new UserResponseDto("3855a7f1-3ff9-439a-9afc-112366cc1303", "Test 1", "test1@dev.in", "test1", "indore", new ArrayList<>()),
						new UserResponseDto("3855a7f1-3ff9-439a-9afc-112366cc1303", "Test 2", "test2@dev.in", "test2", "indore", new ArrayList<>()))
				.collect(Collectors.toList());
	}
	
	@Test
	@DisplayName("test_create_user")
	void createUserTest() throws Exception {

		String jsonRequest = mapper.writeValueAsString(userRequestDto);

		when(this.userService.registerUser(userRequestDto)).thenReturn(userResponseDto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/users/register").content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo(user.getEmail())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
				.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("test_delete_user")
	void deleteUserTest() throws Exception {
		
		when(this.userService.deleteUser(user.getId())).thenReturn(userResponseDto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/api/v1/users/delete/3855a7f1-3ff9-439a-9afc-112366cc1303");
		mockMvc.perform(request)
			.andDo(print())
			.andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo(user.getEmail())))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("test_get_all_users")
	void getAllUsersTest() throws Exception {
		
		when(this.userService.getAllUsers()).thenReturn(List.of(userResponseDtos.get(0), userResponseDtos.get(1)));
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v1/users/get-all-users");
		
		mockMvc.perform(request)
			.andDo(print())
			.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value("test1@dev.in"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].userName").value("test1"))
            .andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
	}
	
	@Test
	@DisplayName("test_get_user_by_id")
	void getUserById() throws Exception {
		
		when(this.userService.getUserById(user.getId())).thenReturn(userResponseDtos.get(0));
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v1/users/get-id/3855a7f1-3ff9-439a-9afc-112366cc1303").accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(request)
			.andDo(print())
			.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test1@dev.in"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("test1"))
            .andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
	}
}
