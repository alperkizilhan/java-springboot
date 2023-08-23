package com.example.project1.repository;


import com.example.project1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Override
	Optional<User> findById(Long aLong);

	@Override
	List<User> findAll();

	@Override
	void deleteById(Long aLong);


}
