package com.emergency.web.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
import com.emergency.web.dto.response.RefreshResponseDto;
import com.emergency.web.exception.GlobalException;
import com.emergency.web.jwt.JwtUtils;
import com.emergency.web.mapper.token.TokenMapper;
import com.emergency.web.mapper.user.UserMapper;
import com.emergency.web.model.RefreshToken;
import com.emergency.web.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
		
		if (user != null) {
			throw new GlobalException("존재하지 않는 아이디입니다. 아이디를 확인해주세요.", "DUPLICATE_USER_ID");
		}
		
		// PrincipalDetails 객체 생성
		PrincipalDetails principalDetails = new PrincipalDetails(user);
		
		// 인증 객체 생성 (권한 관리를 스프링에게 위임)
		Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		
		// SecurityContextHolder에 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtUtils.createAccessToken(authentication);
		
		RefreshToken existingToken = tokenMapper.getRefreshTokenByUserId(loginRequestDto.getUserId());
		
		String refreshToken;
		
		// 유효한 토큰이 이미 존재하는지 검증
		if (existingToken != null && jwtUtils.validateJwtToken(existingToken.getToken())) {
			refreshToken = existingToken.getToken();
		} else {
			refreshToken = jwtUtils.createRefreshToken(authentication);
			
			RefreshToken rt = RefreshToken.builder()
					.userId(jwtUtils.getUserNameFromJwtToken(refreshToken))
					.token(refreshToken)
					.expiryDate(jwtUtils.getExpirationFromJwtToken(refreshToken))
					.build();
			
			// db에 저장 여기서 이미 존재하는 refreshToken이 있다면 삭제 후 생성하도록 하는 로직을 추가해야함
			int tokenResult = tokenMapper.saveRefreshToken(rt);
			
			if (tokenResult < 1) {
				throw new GlobalException("토큰 저장에 실패하였습니다.", "REFRESH_TOKEN_SAVE_FAIL");
			}
		}
		
		// 최종 로그인 업데이트 작업
		int lastLoginResult = userMapper.updateLastLogin(loginRequestDto.getUserId());
		
		if (lastLoginResult < 1) {
			throw new GlobalException("최종 로그인 업데이트에 실패하였습니다.", "LAST_LOGIN_UPDATE_FAIL");
		}
		
		
		return LoginResponseDto.builder()
							   .accessToken(accessToken)
							   .refreshToken(refreshToken)
							   .type(typeSafeProperties.getTokenPrefix())
							   .build();
	}
	
	@Transactional
	public RefreshResponseDto refresh(HttpServletRequest request) {
		
		// 쿠키에서 refreshToken 추출
		String refreshToken = getRefreshTokenFromCookie(request);
		
		if (refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
			// SecurityContextHolder에서 인증객체를 가져오기
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if (authentication != null && authentication.isAuthenticated()) {
				
				// 새로운 accessToken 생성
				String accessToken = jwtUtils.createAccessToken(authentication);
				
				return RefreshResponseDto.builder()
										 .accessToken(accessToken)
										 .type(typeSafeProperties.getTokenPrefix())
										 .build();
			} else {
				throw new GlobalException("SecurityContext에 인증 정보가 존재하지 않습니다.", "REFRESH_ERROR");
			}
		}
		
		return null;
	}
	
	@Transactional
	public void logout(HttpServletRequest request) {
		
		// 쿠키에서 refreshToken 추출
		String refreshToken = getRefreshTokenFromCookie(request);
		
		if (refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
			tokenMapper.deleteRefreshTokenByUserId(jwtUtils.getUserNameFromJwtToken(refreshToken));
		}
		
		SecurityContextHolder.clearContext();
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
	
	// 쿠키로부터 리프레쉬 토큰 추출
	public String getRefreshTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(typeSafeProperties.getRefreshTokenName())) {
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}
}
