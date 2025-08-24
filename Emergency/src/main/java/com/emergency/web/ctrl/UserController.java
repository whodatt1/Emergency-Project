package com.emergency.web.ctrl;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.request.user.ChkUserRequestDto;
import com.emergency.web.dto.request.user.ModRequestDto;
import com.emergency.web.dto.request.validator.user.ModRequestValidator;
import com.emergency.web.dto.response.user.ChkUserResponseDto;
import com.emergency.web.dto.response.user.UserInfoDtlResponseDto;
import com.emergency.web.dto.response.user.UserInfoResponseDto;
import com.emergency.web.jwt.JwtUtils;
import com.emergency.web.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.ctrl
* @fileName        : UserController.java
* @author          : KHK
* @date            : 2025.01.11
* @description     : 유저정보 관리 컨트롤러
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.11        KHK                최초 생성
 */

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	private final TypeSafeProperties typeSafeProperties;
	
	private final JwtUtils jwtUtils;
	
	@PostMapping("/api/v1/user/me")
	public ResponseEntity<?> getMe() {
		
		UserInfoResponseDto userInfoResponseDto = userService.getMe();
		
		return ResponseEntity.ok(userInfoResponseDto);
	}
	
	@PostMapping("/api/v1/user/chk")
	public ResponseEntity<?> chkMe(@RequestBody ChkUserRequestDto chkUserRequestDto, HttpServletResponse response) {
		
		ChkUserResponseDto chkUserResponseDto = userService.chkMe(chkUserRequestDto);
		
		ResponseCookie verifyTokenCookie = ResponseCookie.from(typeSafeProperties.getVerifyTokenName(), chkUserResponseDto.getVerifyToken())
														  .httpOnly(true) // 클라이언트 javascript에서 접근 불가 XSS 방지
														  //.secure(true) // HTTPS 가 아닌 환경에선 반환 안함
														  .path("/")
														  .maxAge(typeSafeProperties.getJwtVerifyExpirationTime() / 1000)
														  .sameSite("Strict") // 쿠키가 동일한 도메인에서 발생한 요청에 대해서만 전송 CSRF 방지
														  .build();
		
		response.setHeader("Set-Cookie", verifyTokenCookie.toString());
		
		return ResponseEntity.ok(chkUserResponseDto.isChk());
	}
	
	@PostMapping("/api/v1/user/medetail")
	public ResponseEntity<?> getMeDetail(HttpServletRequest request) {
		
		String verifyToken = jwtUtils.getVerifyTokenFromCookie(request);
		
		UserInfoDtlResponseDto userInfoDtlResponseDto = userService.getMeDetail(verifyToken);
		
		return ResponseEntity.ok(userInfoDtlResponseDto);
	}
	
	@PostMapping("/api/v1/user/outdetail")
	public ResponseEntity<?> outMeDetail(HttpServletResponse response) {
		
		ResponseCookie verifyTokenCookie = ResponseCookie.from(typeSafeProperties.getVerifyTokenName(), "")
				  .httpOnly(true) // 클라이언트 javascript에서 접근 불가 XSS 방지
				  //.secure(true) // HTTPS 가 아닌 환경에선 반환 안함
				  .path("/")
				  .maxAge(0)
				  .sameSite("Strict") // 쿠키가 동일한 도메인에서 발생한 요청에 대해서만 전송 CSRF 방지
				  .build();

		response.setHeader("Set-Cookie", verifyTokenCookie.toString());
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/api/v1/user/modme")
	public ResponseEntity<?> modMe(@RequestBody ModRequestDto modRequestDto, Errors errors, ModRequestValidator modRequestValidator) {
	    
		modRequestValidator.validate(modRequestDto, errors);
		
		if (errors.hasErrors()) {
	        return ResponseEntity.badRequest().body(userService.validHandle(errors));
	    }
		
		userService.modMe(modRequestDto);
		
	    return ResponseEntity.ok().body(null);
	}
}
