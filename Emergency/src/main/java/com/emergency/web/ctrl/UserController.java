package com.emergency.web.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.response.auth.UserInfoResponseDto;
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
}
