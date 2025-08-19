package com.emergency.web.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.request.user.ChkUserRequestDto;
import com.emergency.web.dto.request.user.ModRequestDto;
import com.emergency.web.dto.request.validator.user.ModRequestValidator;
import com.emergency.web.dto.response.user.UserInfoDtlResponseDto;
import com.emergency.web.dto.response.user.UserInfoResponseDto;
import com.emergency.web.service.user.UserService;

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
	
	@PostMapping("/api/v1/user/me")
	public ResponseEntity<?> getMe() {
		
		UserInfoResponseDto userInfoResponseDto = userService.getMe();
		
		return ResponseEntity.ok(userInfoResponseDto);
	}
	
	@PostMapping("/api/v1/user/chk")
	public ResponseEntity<?> chkMe(@RequestBody ChkUserRequestDto chkUserRequestDto) {
		
		Boolean chk = userService.chkMe(chkUserRequestDto);
		
		return ResponseEntity.ok(chk);
	}
	
	@PostMapping("/api/v1/user/medetail")
	public ResponseEntity<?> getMeDetail() {
		
		UserInfoDtlResponseDto userInfoDtlResponseDto = userService.getMeDetail();
		
		return ResponseEntity.ok(userInfoDtlResponseDto);
	}
	
	@PostMapping("/api/v1/auth/modme")
	public ResponseEntity<?> modMe(@RequestBody ModRequestDto modRequestDto, Errors errors, ModRequestValidator modRequestValidator) {
	    
		modRequestValidator.validate(modRequestDto, errors);
		
		if (errors.hasErrors()) {
	        return ResponseEntity.badRequest().body(userService.validHandle(errors));
	    }
		
		userService.modMe(modRequestDto);
		
	    return ResponseEntity.ok().body(null);
	}
}
