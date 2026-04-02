package com.finance.sdp.dto;

import com.finance.sdp.enums.Role;

public class LoginResponse {
	
	private String token;
    private String email;
    private String name;
    private Role role;
    private String userId;
    
    public LoginResponse() {}
    
	public LoginResponse(String token, String email, String name, Role role, String userId) {
		super();
		this.token = token;
		this.email = email;
		this.name = name;
		this.role = role;
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
	
    
}
