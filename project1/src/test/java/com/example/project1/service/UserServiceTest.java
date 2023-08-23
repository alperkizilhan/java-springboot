package com.example.project1.service;

import org.junit.jupiter.api.Test;
import com.example.project1.bean.UserRequest;
import com.example.project1.bean.UserResponse;
import com.example.project1.entity.User;
import com.example.project1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

	@Mock //orderServiceTest gibi yap. mocklama
	private UserRepository userRepository;

	@Mock
	private KafkaService kafkaService; // Assume you have KafkaService

	@InjectMocks
	private UserService userService;



	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testAddUser() {
		// given
		UserRequest userRequest = new UserRequest();
		userRequest.setUserName("John");
		userRequest.setEmail("john@example.com");

		User expectedUser = new User();
		expectedUser.setUserName(userRequest.getUserName());
		expectedUser.setEmail(userRequest.getEmail());
		expectedUser.setCreatedAt(LocalDateTime.now());
		expectedUser.setLastUpdatedAt(LocalDateTime.now());

		when(userRepository.save(any(User.class))).thenReturn(expectedUser);

		// when
		UserResponse actualResponse = userService.addUser(userRequest);

		// then
		assertNotNull(actualResponse);
		assertEquals(expectedUser.getUserName(), actualResponse.getUserName());
		assertEquals(expectedUser.getEmail(), actualResponse.getEmail());

		verify(kafkaService, times(1)).sendData(expectedUser); // order a da ekle
	}

	@Test
	void testFindAllUsers() {
		// given

		List<User> users = new ArrayList<>();
		User user1= new User();
		user1.setId(9999L);
		user1.setUserName("asl");
		user1.setEmail("all@wess.com");
		User user2= new User();
		user2.setId(99999L);
		user2.setUserName("asd");
		user2.setEmail("a@we.com");

		users.add(user1);
		users.add(user2);

		when(userRepository.findAll()).thenReturn(users);

		// when
		List<UserResponse> actualResponse = userService.findAllUsers();

		// then
		assertEquals(users.size(), actualResponse.size());
	}

	@Test
	void testFindById() {
		// given
		Long userId = 999L;
		User userTest= new User();
		userTest.setId(999L);
		userTest.setUserName("asl");
		userTest.setEmail("all@wess.com");

		when(userRepository.findById(userId)).thenReturn(Optional.of(userTest));

		// when
		UserResponse actualResponse = userService.findById(userId);

		// then
		assertNotNull(actualResponse);
		assertEquals(userTest.getId(), actualResponse.getId());
		assertEquals(userTest.getUserName(), actualResponse.getUserName());
		assertEquals(userTest.getEmail(), actualResponse.getEmail());
	}

	@Test
	void testDeleteUserById() {
		// given
		Long userId = 99999L;
		User userTest= new User();
		userTest.setId(99999L);
		userTest.setUserName("aaaa");
		userTest.setEmail("aaaa@wess.com");
		Optional<User> userOptional = Optional.of(userTest);

		when(userRepository.findById(userId)).thenReturn(userOptional);
		doNothing().when(userRepository).deleteById(userId);

		// when
		boolean result = userService.deleteUserById(userId);

		// then
		assertTrue(result);
		verify(kafkaService, times(1)).sendData(userTest);
	}

	@Test
	void updateUserEmail() {
		// given
		Long userId = 11111L;
		User userTest= new User();
		userTest.setId(11111L);
		userTest.setUserName("bbb");
		userTest.setEmail("bbbb@wess.com");
		String newEmail = "newemail@example.com";
		Optional<User> userOptional = Optional.of(userTest);

		when(userRepository.findById(userId)).thenReturn(userOptional);
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock save operation to return the argument

		// when
		UserResponse actualResponse = userService.updateUserEmail(userId, newEmail);

		// then
		assertNotNull(actualResponse);
		assertEquals(userTest.getId(), actualResponse.getId());
		assertEquals(userTest.getUserName(), actualResponse.getUserName());
		assertEquals(newEmail, actualResponse.getEmail());

		verify(kafkaService, times(1)).sendData(any(User.class));
	}
}
