package com.emergency.web.dto.request.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.dto.request
* @fileName        : AuthRequestDto.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : Auth 요청 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
	
	@NotEmpty(message = "아이디는 필수 입력 사항입니다.")
	private String userId;
	
	@NotEmpty(message = "비밀번호는 필수 입력 사항입니다.")
	private String password;
}
