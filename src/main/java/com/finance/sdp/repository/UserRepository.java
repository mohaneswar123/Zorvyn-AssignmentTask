package com.finance.sdp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.finance.sdp.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByEmail(String email);

}
