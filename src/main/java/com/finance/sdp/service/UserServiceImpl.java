package com.finance.sdp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finance.sdp.dto.LoginResponse;
import com.finance.sdp.enums.Role;
import com.finance.sdp.model.User;
import com.finance.sdp.repository.UserRepository;
import com.finance.sdp.security.JwtTokenProvider;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	public UserServiceImpl(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager,
			JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public User registerUser(User user) {
		validateRegisterInput(user);

		if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
			throw new IllegalArgumentException("Email already registered");
		}

		User newUser = new User();
		newUser.setUsername(user.getUsername().trim());
		newUser.setEmail(user.getEmail().trim().toLowerCase(Locale.ROOT));
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setRole(Role.Viewer);
		newUser.setPhnoneNumber(user.getPhnoneNumber());
		newUser.setIsActive(true);
		newUser.setCreatedAt(LocalDateTime.now());

		return userRepository.save(newUser);
	}

	@Override
	public LoginResponse loginUser(String email, String password) {
		String normalizedEmail = normalizeEmail(email);

		User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

		if (!Boolean.TRUE.equals(user.getIsActive())) {
			throw new IllegalArgumentException("User account is inactive");
		}

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(normalizedEmail, password));

		String token = jwtTokenProvider.generateToken(
				normalizedEmail,
				Map.of(
						"role", user.getRole().name(),
						"userId", user.getId()));

		return new LoginResponse(token, user.getEmail(), user.getUsername(), user.getRole(), user.getId());
	}

	@Override
	public String updateUser(String userId, User updatedUser) {
		User existingUser = getUserById(userId);

		if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
			existingUser.setUsername(updatedUser.getUsername().trim());
		}

		if (updatedUser.getPhnoneNumber() != null && !updatedUser.getPhnoneNumber().isBlank()) {
			existingUser.setPhnoneNumber(updatedUser.getPhnoneNumber().trim());
		}

		if (updatedUser.getRole() != null) {
			existingUser.setRole(updatedUser.getRole());
		}

		if (updatedUser.getIsActive() != null) {
			existingUser.setIsActive(updatedUser.getIsActive());
		}

		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}

		userRepository.save(existingUser);
		return "User updated successfully";
	}

	@Override
	public User getUserById(String userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

	@Override
	public String inActiveUser(String userId) {
		User user = getUserById(userId);
		user.setIsActive(false);
		userRepository.save(user);
		return "User deactivated successfully";
	}

	@Override
	public String assignRole(String userId, String role) {
		User user = getUserById(userId);
		Role parsedRole = parseRole(role);

		user.setRole(parsedRole);
		userRepository.save(user);
		return "Role assigned successfully";
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getUsersByRole(String role) {
		return userRepository.findByRole(parseRole(role));
	}

	@Override
	public List<User> getUsersByAcitveStatus(String status) {
		if (status == null || status.isBlank()) {
			throw new IllegalArgumentException("Status is required");
		}

		String normalizedStatus = status.trim().toLowerCase(Locale.ROOT);
		if (!"true".equals(normalizedStatus) && !"false".equals(normalizedStatus)) {
			throw new IllegalArgumentException("Status must be either true or false");
		}

		boolean active = Boolean.parseBoolean(normalizedStatus);
		return userRepository.findByIsActive(active);
	}

	private void validateRegisterInput(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User payload is required");
		}
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			throw new IllegalArgumentException("Email is required");
		}
		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new IllegalArgumentException("Password is required");
		}
		if (user.getUsername() == null || user.getUsername().isBlank()) {
			throw new IllegalArgumentException("Username is required");
		}
	}

	private String normalizeEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email is required");
		}
		return email.trim().toLowerCase(Locale.ROOT);
	}

	private Role parseRole(String role) {
		if (role == null || role.isBlank()) {
			throw new IllegalArgumentException("Role is required");
		}

		for (Role value : Role.values()) {
			if (value.name().equalsIgnoreCase(role.trim())) {
				return value;
			}
		}

		throw new IllegalArgumentException("Invalid role provided");
	}
}
