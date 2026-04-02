package com.finance.sdp.service;

import java.util.List;

import com.finance.sdp.dto.LoginResponse;
import com.finance.sdp.model.User;

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
	User getUserByEmail(String email);
	String activeUser(String userId);
	String deleteUser(String userId);
	
}
