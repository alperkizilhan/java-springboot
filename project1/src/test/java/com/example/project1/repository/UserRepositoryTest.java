package com.example.project1.repository;

import com.example.project1.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserRepositoryTest {


	@Autowired
	public UserRepository userRepository;

	@BeforeEach
	void setUp() {

	}

	@Test
	void FindByIdTest() {
		// given
		User expectedUser = new User();

		expectedUser.setUserName("mahmut");
		expectedUser.setEmail("m@e.com");

		User save = userRepository.save(expectedUser);

		// when
		Optional<User> actualUser = userRepository.findById(save.getId());

		// then
		assertThat(actualUser).isNotNull();
		assertThat(actualUser.get().getId()).isEqualTo(expectedUser.getId());
		assertThat(actualUser.get().getUserName()).isEqualTo(expectedUser.getUserName());
		assertThat(actualUser.get().getEmail()).isEqualTo(expectedUser.getEmail());
	}

	@Test
	void findAll() {
		// given
		User user1 = new User();

		user1.setUserName("cem");
		user1.setEmail("cem@e.com");


		User user2 = new User();

		user2.setUserName("seda");
		user2.setEmail("seda@e.com");


		userRepository.save(user1);
		userRepository.save(user2);

		// when
		List<User> actualUsers = userRepository.findAll();

		// then

		assertThat(actualUsers).isNotNull();
		assertThat(actualUsers).hasSize(2);
		assertThat(actualUsers).extracting(User::getUserName, User::getEmail)
			.containsExactlyInAnyOrder(tuple(user1.getUserName(), user1.getEmail()),
				tuple(user2.getUserName(), user2.getEmail()));

	}

	@Test
	void deleteById() {
		// given
		User userToDelete = new User();
		userToDelete.setUserName("veysel");
		userToDelete.setEmail("v@e.com");

		User save = userRepository.save(userToDelete);

		// when

		Optional<User> deleteUser = userRepository.findById(save.getId());
		userRepository.deleteById(deleteUser.get().getId());

		// then
		assertThat(userRepository.existsById(save.getId())).isFalse();


	}
}
