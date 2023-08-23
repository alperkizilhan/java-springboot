package com.example.project1.service;

import com.example.project1.bean.UserRequest;
import com.example.project1.bean.UserResponse;
import com.example.project1.entity.User;
import org.springframework.stereotype.Service;
import com.example.project1.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final KafkaService kafkaService;



	// Constructor injection
	public UserService(UserRepository userRepository, KafkaService kafkaService) {
		this.userRepository = userRepository;
		this.kafkaService = kafkaService;
	}


	public UserResponse addUser(UserRequest user) {
		User user1 = convertToUser(user);
		User save = userRepository.save(user1);
		kafkaService.sendData(save);


		return convertToUserResponse(save);
	}

	@GetMapping("/user")
	public List<UserResponse> findAllUsers() {
		List<User> users = userRepository.findAll();

		return users.stream()
			.map(this::convertToUserResponse)
			.collect(Collectors.toList());
	}


	public UserResponse findById(Long userId) {
		return userRepository.findById(userId).map(this::convertToUserResponse).orElse(null);
	}

	public boolean deleteUserById(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			userRepository.deleteById(id);
			kafkaService.sendData(userOptional.get());
			return true;
		} else {
			return false;
		}
	}

	public UserResponse updateUserEmail(Long id, String newEmail) {
		Optional<User> userOptional = userRepository.findById(id);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setEmail(newEmail);
			User updatedUser = userRepository.save(user);
			kafkaService.sendData(updatedUser);

			return convertToUserResponse(updatedUser);
		} else {
			return null;
		}
	}
	private User convertToUser(UserRequest userRequest) {
		User user = new User();
		user.setUserName(userRequest.getUserName());
		user.setEmail(userRequest.getEmail());
		user.setCreatedAt(LocalDateTime.now());
		user.setLastUpdatedAt(LocalDateTime.now());
		return user;
	}
	private UserResponse convertToUserResponse(User user) {
		UserResponse userResponse = new UserResponse();
		userResponse.setId(user.getId());
		userResponse.setUserName(user.getUserName());
		userResponse.setEmail(user.getEmail());
		userResponse.setCreatedAt(user.getCreatedAt());
		userResponse.setLastUpdatedAt(user.getLastUpdatedAt());
		return userResponse;
	}


}
