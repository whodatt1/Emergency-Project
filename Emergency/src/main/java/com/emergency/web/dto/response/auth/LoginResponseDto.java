package com.emergency.web.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.dto.response
* @fileName        : LoginResponseDto.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : Login 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
	
	private String accessToken;
	private String refreshToken;
	private String type; // Bearer 
	
}
