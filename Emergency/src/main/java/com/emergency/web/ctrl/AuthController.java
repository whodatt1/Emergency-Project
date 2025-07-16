package com.emergency.web.ctrl;


import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.request.auth.JoinRequestDto;
import com.emergency.web.dto.request.auth.LoginRequestDto;
import com.emergency.web.dto.response.auth.LoginResponseDto;
import com.emergency.web.dto.response.auth.RefreshResponseDto;
import com.emergency.web.jwt.JwtUtils;
import com.emergency.web.service.auth.AuthService;
import com.emergency.web.service.fcm.FcmService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	
	// 테스트용으로 추가
	private final FcmService fcmService;
	
	private final TypeSafeProperties typeSafeProperties;
	
	private final JwtUtils jwtUtils;
	
	@PostMapping("/api/v1/auth/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, Errors errors, HttpServletResponse response) {
		
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(authService.validHandle(errors));
		}
		
		LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
		
		// AccessToken은 다양한 클라이언트에서 사용하기 쉬운 헤더에 담기
		response.setHeader(typeSafeProperties.getHeaderString(), loginResponseDto.getType() + loginResponseDto.getAccessToken());
		
		// RefreshToken은 장기간 존재하기에 XSS 공격으로의 보호, 자동으로 요청에 담아 보냄의 이점을 활용하고자 HttpOnly 쿠키를 사용
		ResponseCookie refreshTokenCookie = ResponseCookie.from(typeSafeProperties.getRefreshTokenName(), loginResponseDto.getRefreshToken())
														  .httpOnly(true) // 클라이언트 javascript에서 접근 불가 XSS 방지
														  //.secure(true) // HTTPS 가 아닌 환경에선 반환 안함
														  .path("/")
														  .maxAge(typeSafeProperties.getJwtRefreshExpirationTime() / 1000)
														  .sameSite("Strict") // 쿠키가 동일한 도메인에서 발생한 요청에 대해서만 전송 CSRF 방지
														  .build();
		
		// Set-Cookie 헤더로 쿠키를 추가
		response.addHeader("Set-Cookie", refreshTokenCookie.toString());
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/api/v1/auth/refresh")
	public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
		
		// HTTP 정보 처리는 컨트롤러단에서
		String refreshToken = jwtUtils.getRefreshTokenFromCookie(request);
		
		RefreshResponseDto refreshResponseDto = authService.refresh(refreshToken);
		
		if (refreshResponseDto != null) {
			// AccessToken은 다양한 클라이언트에서 사용하기 쉬운 헤더에 담기
			response.setHeader(typeSafeProperties.getHeaderString(), refreshResponseDto.getType() + refreshResponseDto.getAccessToken());
		}
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/api/v1/auth/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		
		// HTTP 정보 처리는 컨트롤러단에서
		String refreshToken = jwtUtils.getRefreshTokenFromCookie(request);
		
		authService.logout(refreshToken);
		
		ResponseCookie delRefreshTokenCookie = ResponseCookie.from(typeSafeProperties.getRefreshTokenName(), "")
															 .httpOnly(true)
															 //.secure(true)
															 .path("/")
															 .maxAge(0)
															 .sameSite("Strict")
															 .build();
		
		response.addHeader("Set-Cookie", delRefreshTokenCookie.toString());
		
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
