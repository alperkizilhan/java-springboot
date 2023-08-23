package com.example.project1.controller;

import com.example.project1.bean.UserRequest;
import com.example.project1.bean.UserResponse;
import com.example.project1.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void getAllUsersTest() throws Exception {
		UserResponse user1 = new UserResponse();
		user1.setUserName("name");
		user1.setEmail("name@a.com");
		UserResponse user2 = new UserResponse();
		user2.setUserName("isim");
		user2.setEmail("isim@a.com");

		List<UserResponse> users =new ArrayList<>();
		users.add(user1);
		users.add(user2);

		when(userService.findAllUsers()).thenReturn(users);

		mockMvc.perform(get("/user"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()").value(users.size()))
			.andExpect(jsonPath("$[0].id").value(users.get(0).getId()))
			.andExpect(jsonPath("$[0].userName").value(users.get(0).getUserName()))
			.andExpect(jsonPath("$[0].email").value(users.get(0).getEmail()))
			.andExpect(jsonPath("$[1].id").value(users.get(1).getId()))
			.andExpect(jsonPath("$[1].userName").value(users.get(1).getUserName()))
			.andExpect(jsonPath("$[1].email").value(users.get(1).getEmail()));
	}

	@Test
	void newUserTest() throws Exception {
		UserRequest userRequest = new UserRequest();
		userRequest.setUserName("name");
		userRequest.setEmail("name@a.com");

		UserResponse userResponse = new UserResponse();
		userResponse.setUserName("name");
		userResponse.setEmail("name@a.com");

		when(userService.addUser(any(UserRequest.class))).thenReturn(userResponse);

		mockMvc.perform(post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(userResponse.getId()))
			.andExpect(jsonPath("$.userName").value(userResponse.getUserName()))
			.andExpect(jsonPath("$.email").value(userResponse.getEmail()));
	}

	@Test
	void oneTest() throws Exception {
		long userId = 1L;
		UserResponse userResponse = new UserResponse();
		userResponse.setUserName("name");
		userResponse.setEmail("name@a.com");

		when(userService.findById(userId)).thenReturn(userResponse);

		mockMvc.perform(get("/user/{id}", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(userResponse.getId()))
			.andExpect(jsonPath("$.userName").value(userResponse.getUserName()))
			.andExpect(jsonPath("$.email").value(userResponse.getEmail()));
	}

	@Test
	void deleteUserByIdTest() throws  Exception{
		long userId = 1L;
		boolean deletionResult = true;

		when(userService.deleteUserById(userId)).thenReturn(deletionResult);

		mockMvc.perform(delete("/user/{id}", userId))
			.andExpect(status().isOk())
			.andExpect(content().string("kullanıcı silindi"));
	}

	@Test
	void updateUserEmailTest() throws Exception{
		long userId = 1L;
		String newEmail = "new.email@example.com";
		UserResponse updatedUserResponse = new UserResponse();
		updatedUserResponse.setId(userId);
		updatedUserResponse.setUserName("John");
		updatedUserResponse.setEmail(newEmail);

		when(userService.updateUserEmail(userId, newEmail)).thenReturn(updatedUserResponse);

		mockMvc.perform(put("/user/{id}/email", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content((newEmail)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(userId))
			.andExpect(jsonPath("$.userName").value(updatedUserResponse.getUserName()))
			.andExpect(jsonPath("$.email").value(newEmail));
	}
}
