package com.emergency.web.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.emergency.web.auth.PrincipalDetails;
import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.request.JoinRequestDto;
import com.emergency.web.dto.request.LoginRequestDto;
import com.emergency.web.dto.response.LoginResponseDto;
import com.emergency.web.exception.GlobalException;
import com.emergency.web.jwt.JwtUtils;
import com.emergency.web.mapper.token.TokenMapper;
import com.emergency.web.mapper.user.UserMapper;
import com.emergency.web.model.RefreshToken;
import com.emergency.web.model.User;

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
	
	private final UserMapper userMapper;
	
	private final TokenMapper tokenMapper;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final TypeSafeProperties typeSafeProperties;
	
	private final JwtUtils jwtUtils;
	
	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		
		// PrincipalDetails 객체를 만들기 위해 User 객체 가져오기
		User user = userMapper.findById(loginRequestDto.getUserId());
		
		// PrincipalDetails 객체 생성
		PrincipalDetails principalDetails = new PrincipalDetails(user);
		
		// 인증 객체 생성 (권한 관리를 스프링에게 위임)
		Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		
		// SecurityContextHolder에 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtUtils.createAccessToken(authentication);
		String refreshToken = jwtUtils.createRefreshToken(authentication);
		
		RefreshToken rt = RefreshToken.builder()
				.userId(jwtUtils.getUserNameFromJwtToken(refreshToken))
				.token(refreshToken)
				.expiryDate(jwtUtils.getExpirationFromJwtToken(refreshToken))
				.build();
		
		// db에 저장
		tokenMapper.saveRefreshToken(rt);
		
		LoginResponseDto loginResponseDto = LoginResponseDto.builder()
															.accessToken(accessToken)
															.refreshToken(refreshToken)
															.type(typeSafeProperties.getTokenPrefix())
															.build();
		
		return loginResponseDto;
	}
	
	@Transactional
	public void signUp(JoinRequestDto userRequestDto) {
		
		// 중복 체크
		User dupleUser = userMapper.findById(userRequestDto.getUserId());
		
		if (dupleUser != null) {
			throw new GlobalException("이미 사용중인 아이디입니다. 다른 아이디를 사용해 주세요.", "DUPLICATE_USER_ID");
		}
		
		User user = User.builder()
						.userId(userRequestDto.getUserId())
						.password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
						.email(userRequestDto.getEmail())
						.hp(userRequestDto.getHp())
						.address(userRequestDto.getAddress())
						.regAt(LocalDateTime.now())
						.modAt(LocalDateTime.now())
						.roles("ROLE_USER") // default
						.build();
		
		// 최종 저장
		int res = userMapper.saveUser(user);
		
		if (res < 1) {
			throw new GlobalException("회원가입에 실패하였습니다. 고객센터에 문의해주세요.", "USER_SAVE_FAILED");
		}
	}
	
	// 밸리데이션 핸들링
	public Map<String, String> validHandle(Errors errors) {
		
		Map<String, String> validResult = new HashMap<>();
		
		for (FieldError error : errors.getFieldErrors()) {
			validResult.put(error.getField(), error.getDefaultMessage());
		}
		
		return validResult;
	}
}
