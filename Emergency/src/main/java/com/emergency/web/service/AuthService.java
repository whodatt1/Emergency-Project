package com.emergency.web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergency.web.dto.request.LoginRequestDto;
import com.emergency.web.dto.request.JoinRequestDto;
import com.emergency.web.dto.response.LoginResponseDto;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.service
* @fileName        : AuthService.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : 로그인, 회원가입, 토큰 재발급 비즈니스 로직 구간
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Service
@RequiredArgsConstructor
public class AuthService {
	
	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		return null;
	}
	
	@Transactional
	public void signUp(JoinRequestDto userRequestDto) {
	}
}
