package com.emergency.web.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.dto.response
* @fileName        : AuthResonseDto.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : Auth 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Getter
@Builder
public class LoginResponseDto {
	
	private String accessToken;
	private String type; // Bearer 
	
}
