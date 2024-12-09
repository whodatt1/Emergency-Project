package com.emergency.web.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.request.JoinRequestDto;
import com.emergency.web.dto.request.LoginRequestDto;
import com.emergency.web.dto.response.LoginResponseDto;
import com.emergency.web.service.AuthService;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.ctrl
* @fileName        : AuthController.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : 로그인, 회원가입, 토큰 재발급 관리 컨트롤러
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@RestController
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/api/v1/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
		// 테스트 위해 서비스 호출
		LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
		return ResponseEntity.ok(loginResponseDto);
	}
	
	@PostMapping("/api/v1/auth/signup")
	public ResponseEntity<?> signUp(@RequestBody JoinRequestDto joinRequestDto) {
		// 테스트 위해 서비스 호출
		authService.signUp(joinRequestDto);
		return null;
	}
}
