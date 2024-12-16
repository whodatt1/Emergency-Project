package com.emergency.web.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.request.JoinRequestDto;
import com.emergency.web.dto.request.LoginRequestDto;
import com.emergency.web.dto.response.LoginResponseDto;
import com.emergency.web.service.AuthService;

import jakarta.validation.Valid;
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
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, Errors errors) {
		
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(authService.validHandle(errors));
		}
		
		LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/api/v1/auth/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody JoinRequestDto joinRequestDto, Errors errors) {
		
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(authService.validHandle(errors));
		}
		
		authService.signUp(joinRequestDto);
		return ResponseEntity.ok().body(null);
	}
}
