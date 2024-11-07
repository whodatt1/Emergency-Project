package com.emergency.web.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : User.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : User VO
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Getter
@Builder
public class Auth {
	
	private String userId; // PRIMARY KEY
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
