package com.finance.sdp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.finance.sdp.enums.Role;
import com.finance.sdp.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByEmail(String email);

	Optional<User> findByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCase(String email);

	List<User> findByRole(Role role);

	List<User> findByIsActive(Boolean isActive);

}
