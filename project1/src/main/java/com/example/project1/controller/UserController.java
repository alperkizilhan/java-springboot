package com.example.project1.controller;

import com.example.project1.bean.UserRequest;
import com.example.project1.bean.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.project1.service.UserService;
import java.util.List;

@RestController
public class UserController {


	private final UserService userService;

	UserController( UserService userService) {

		this.userService = userService;
	}

	@GetMapping("/user")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		try {

			List<UserResponse> users = userService.findAllUsers();

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/user")
	UserResponse newUser(@RequestBody UserRequest newUser) {
		return userService.addUser(newUser);
	}

	@GetMapping("/user/{id}")
	UserResponse one(@PathVariable Long id) {
		return userService.findById(id);
	}


	@DeleteMapping("/user/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
		boolean deletionResult = userService.deleteUserById(id);
		if (deletionResult) {
			return new ResponseEntity<>("kullanıcı silindi", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("kullanıcı bulunamdı", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/user/{id}/email")
	public ResponseEntity<UserResponse> updateUserEmail(@PathVariable Long id, @RequestBody String email) {
		UserResponse updatedUserDTO = userService.updateUserEmail(id, email);

		if (updatedUserDTO != null) {
			return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	}




