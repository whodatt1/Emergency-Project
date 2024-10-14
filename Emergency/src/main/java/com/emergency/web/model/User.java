package com.emergency.web.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class User {
	
	private String id; // PRIMARY KEY
	private String password;
	private String email; // UNIQUE KEY
	private String hp;
	private String address;
	private LocalDateTime regAt;
	private LocalDateTime modAt;
	private LocalDateTime lastLogin;
	private String roles;
	
	public List<String> getRoleList() {
		if (this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>();
	}
}
