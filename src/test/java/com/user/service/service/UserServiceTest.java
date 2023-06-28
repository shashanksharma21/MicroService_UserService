package com.user.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.user.service.entities.Product;
import com.user.service.entities.User;
import com.user.service.external.service.ProductService;
import com.user.service.repositories.UserRepository;
import com.user.service.request.dto.UserRequestDto;
import com.user.service.response.dto.UserResponseDto;
import com.user.service.services.impl.UserServiceImpl;

@SpringBootTest
@SuppressWarnings("unchecked")
public class UserServiceTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private ProductService productService;
	
	private List<User> users;
	private User user;
	private UserResponseDto userResponseDto;
	private UserRequestDto userRequestDto;
	private Product product;
	private List<Product> products;
	
	@BeforeEach
	void setUp() {
		users = Stream
				.of(new User("3855a7f1-3ff9-439a-9afc-112366cc1303", "test1", "test1@dev.in", "test1", "test1", "indore", "ROLE_NORMAL", false, null),
				new User("3855a7f1-3ff9-439a-9afc-112366cc1303", "test2", "test2@dev.in", "test2", "test2", "indore", "ROLE_NORMAL", false, null))
				.collect(Collectors.toList());
		userRequestDto = new UserRequestDto("Test 1", "test1@dev.in", "test1", "test1", "indore");
		user = new  User("3855a7f1-3ff9-439a-9afc-112366cc1303", "test1", "test1@dev.in", "test1", "test1", "indore", "ROLE_NORMAL", false, null);
		userResponseDto = new UserResponseDto("3855a7f1-3ff9-439a-9afc-112366cc1303", "Test 1", "test1@dev.in", "test1", "indore", new ArrayList<>());
		
		product = new Product("7bfb2794-4c21-48fd-8b4c-9b57c1925549", "Redmi note 9", "Best SmartPhone", "Redmi", 18999.0, 10, "3855a7f1-3ff9-439a-9afc-112366cc1303");
		products = Stream
				.of( new Product("7bfb2794-4c21-48fd-8b4c-9b57c1925549", "Redmi note 9", "Best SmartPhone", "Redmi", 18999.0, 10, "3855a7f1-3ff9-439a-9afc-112366cc1303"),
						 new Product("e9238e4f-405a-4825-8360-7d29371bd941", "Redmi note 10", "Best SmartPhone", "Redmi", 21999.0, 5, "3855a7f1-3ff9-439a-9afc-112366cc1303")
						 ).collect(Collectors.toList());
	
	
	}
	
	@Test
	@DisplayName("test_get_all_users")
	void getAllUsersTest() {
		when(userRepository.findAll()).thenReturn(users);
		List<User> allUsers = (List<User>) this.userService.getAllUsers();
		assertEquals(2, allUsers.size());
	}
	
	@Test
	@DisplayName("test_get_user_By_id")
	void getUserByIdTest() {
		
		when(userRepository.findById("3855a7f1-3ff9-439a-9afc-112366cc1303")).thenReturn(Optional.of(user));
		ResponseEntity<List<Product>> responseEntity = new ResponseEntity<>(products, HttpStatus.OK);
		when(productService.getProductByUserId(user.getId())).thenReturn(responseEntity);
		when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);
		
        UserResponseDto actualResponse=  (UserResponseDto) userService.getUserById(user.getId());

        // Assert
        verify(userRepository).findById(user.getId());
        verify(productService).getProductByUserId(user.getId());

        assertEquals(userResponseDto, actualResponse);
        
	}
	
	@Test
	@DisplayName("test_delete_user")
	void testDeleteUser() {
		
		when(userRepository.findById("3855a7f1-3ff9-439a-9afc-112366cc1303")).thenReturn(Optional.of(user));
		doNothing().when(this.userRepository).delete(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        UserResponseDto actualResponse = userService.deleteUser(user.getId());

        verify(userRepository).findById(user.getId());
        verify(userRepository).delete(user);
        verify(modelMapper).map(user, UserResponseDto.class);
        assertEquals(userResponseDto, actualResponse);
		
	}
	
	@Test
	@DisplayName("test_create_user")
	void testCreateUser() {
		
		

        when(modelMapper.map(userRequestDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);
        UserResponseDto actualResponse = (UserResponseDto) userService.registerUser(userRequestDto);

        verify(modelMapper).map(userRequestDto, User.class);
        verify(userRepository).save(user);
        verify(modelMapper).map(user, UserResponseDto.class);
        assertEquals(userResponseDto, actualResponse);

	}
}
