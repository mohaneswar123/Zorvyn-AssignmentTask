package com.finance.sdp.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.finance.sdp.enums.Role;

@Document(collection = "users")
public class User {
	
	@Id
	private String id;
	private String username;
	private String password;
	private Role role;
	private String email;
	private Boolean isActive=true;
	private String phnoneNumber;
	private LocalDateTime createdAt=LocalDateTime.now();
	
	public User() {
	}
	
	public User(String username, String password, Role role, String email, String phnoneNumber) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
		this.isActive = true;
		this.phnoneNumber = phnoneNumber;
		this.createdAt = LocalDateTime.now();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getPhnoneNumber() {
		return phnoneNumber;
	}

	public void setPhnoneNumber(String phnoneNumber) {
		this.phnoneNumber = phnoneNumber;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
}
