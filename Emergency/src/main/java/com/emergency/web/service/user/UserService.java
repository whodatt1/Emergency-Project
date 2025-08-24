package com.emergency.web.service.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.emergency.web.dto.request.user.ChkUserRequestDto;
import com.emergency.web.dto.request.user.ModRequestDto;
import com.emergency.web.dto.response.user.ChkUserResponseDto;
import com.emergency.web.dto.response.user.UserInfoDtlResponseDto;
import com.emergency.web.dto.response.user.UserInfoResponseDto;
import com.emergency.web.exception.GlobalException;
import com.emergency.web.jwt.JwtUtils;
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
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final JwtUtils jwtUtils;
	
	@Transactional
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
	
	@Transactional
	public ChkUserResponseDto chkMe(ChkUserRequestDto chkUserRequestDto) {
		
		Boolean chk = false;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
			
		String userId = authentication.getName();
		
		User user = userMapper.findById(userId);
		
		if (bCryptPasswordEncoder.matches(chkUserRequestDto.getPassword(), user.getPassword())) {
	        chk = true;
	    }
		
		// Verify Token 생성
		String verifyToken = jwtUtils.createVerifyToken(authentication);
		
		return ChkUserResponseDto.builder()
								 .chk(chk)
								 .verifyToken(verifyToken)
								 .build();
	}
	
	@Transactional
	public UserInfoDtlResponseDto getMeDetail(String verifyToken) {
		
		// SecurityContext에서 인증된 사용자 정보 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		if (verifyToken == null || !jwtUtils.validateJwtToken(verifyToken)) {
	        throw new GlobalException("유효하지 않은 검증 토큰입니다.", "INVALID_VERIFY_TOKEN", HttpStatus.UNAUTHORIZED);
	    }
			
		String userId = authentication.getName();
		
		User user = userMapper.findById(userId);
		
		if (user == null) {
			throw new GlobalException("존재하지 않는 아이디입니다. 아이디를 확인해주세요.", "USER_ID_NOT_FOUND", HttpStatus.UNAUTHORIZED);
		}
		
		return UserInfoDtlResponseDto.builder()
								  	 .email(user.getEmail())
								  	 .hp(user.getHp())
								  	 .postCd(user.getPostCd())
								  	 .address(user.getAddress())
								  	 .build();
	}
	
	@Transactional
	public void modMe(ModRequestDto modRequestDto) {
		
		// SecurityContext에서 인증된 사용자 정보 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		String userId = authentication.getName();
		
		User.UserBuilder userBuilder = User.builder()
				.userId(userId)
		        .email(modRequestDto.getEmail())
		        .hp(modRequestDto.getHp())
		        .postCd(modRequestDto.getPostCd())
		        .address(modRequestDto.getAddress());

		if (modRequestDto.isChangePassword()) {
			userBuilder.password(bCryptPasswordEncoder.encode(modRequestDto.getPassword())); // 여기서 조건부 세팅
		}

		int res = userMapper.modMe(userBuilder.build());
		
		if (res < 1) {
			throw new GlobalException("회원정보 변경에 실패하였습니다. 고객센터에 문의해주세요.", "USER_MOD_FAILED");
		}
	}
	
	// 밸리데이션 핸들링
	public Map<String, String> validHandle(Errors errors) {
		
		Map<String, String> validResult = new HashMap<>();
		
		validResult.put("errorCd", "INVALID_FORM");
		
		for (FieldError error : errors.getFieldErrors()) {
			validResult.put(error.getField(), error.getDefaultMessage());
		}
		
		return validResult;
	}

	
}
