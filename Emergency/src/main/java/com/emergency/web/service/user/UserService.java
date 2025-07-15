package com.emergency.web.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.emergency.web.dto.response.auth.UserInfoResponseDto;
import com.emergency.web.exception.GlobalException;
import com.emergency.web.mapper.user.UserMapper;
import com.emergency.web.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @packageName     : com.emergency.web.service.user
* @fileName        : UserService.java
* @author          : KHK
* @date            : 2025.01.11
* @description     :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.11        KHK                최초 생성
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
	
	private final UserMapper userMapper;
	
	public UserInfoResponseDto getMe() {
		
		log.info("getMe running");
		// SecurityContext에서 인증된 사용자 정보 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
			
		String userId = authentication.getName();
		
		User user = userMapper.findById(userId);
		
		if (user == null) {
			throw new GlobalException("존재하지 않는 아이디입니다. 아이디를 확인해주세요.", "USER_ID_NOT_FOUND", HttpStatus.UNAUTHORIZED);
		}
		
		return UserInfoResponseDto.builder()
								  .userId(user.getUserId())
								  .build();
	}
}
