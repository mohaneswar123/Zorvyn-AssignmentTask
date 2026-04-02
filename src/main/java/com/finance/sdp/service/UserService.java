package com.finance.sdp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.finance.sdp.dto.LoginResponse;
import com.finance.sdp.model.Transaction;
import com.finance.sdp.model.User;

@Service
public interface UserService {
	
	User registerUser(User user);
	LoginResponse loginUser(String email, String password);
	String updateUser(String userId, User updatedUser);
	User getUserById(String userId);
	
	String inActiveUser(String userId);
	String assignRole(String userId, String role);
	List<User> getAllUsers();
	List<User> getUsersByRole(String role);
	List<User> getUsersByAcitveStatus(String status);
	
}
