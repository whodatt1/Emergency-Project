package com.emergency.web.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {
	
	private String userId;
	private String password;
	private String email;
	private String hp;
	private String address;
	private LocalDateTime regAt;
	private LocalDateTime modAt;
	private String roles;
}
