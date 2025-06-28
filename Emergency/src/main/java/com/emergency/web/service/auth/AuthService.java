package com.emergency.web.service.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
import com.emergency.web.dto.request.auth.JoinRequestDto;
import com.emergency.web.dto.request.auth.LoginRequestDto;
import com.emergency.web.dto.response.auth.LoginResponseDto;
import com.emergency.web.dto.response.auth.RefreshResponseDto;
import com.emergency.web.exception.GlobalException;
import com.emergency.web.jwt.JwtUtils;
import com.emergency.web.mapper.fcm.FcmMapper;
import com.emergency.web.mapper.token.TokenMapper;
import com.emergency.web.mapper.user.UserMapper;
import com.emergency.web.model.Fcm;
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
*                    1. 단일 로그인 허용
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
	
	private final FcmMapper fcmMapper;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final TypeSafeProperties typeSafeProperties;
	
	private final JwtUtils jwtUtils;
	
	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		
		// PrincipalDetails 객체를 만들기 위해 User 객체 가져오기
		User user = userMapper.findById(loginRequestDto.getUserId());
		
		if (user == null) {
			throw new GlobalException("존재하지 않는 아이디입니다. 아이디를 확인해주세요.", "USER_ID_NOT_FOUND");
		}
		
		if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
	        throw new GlobalException("비밀번호가 틀렸습니다. 비밀번호를 확인해주세요.", "INVALID_PASSWORD");
	    }
		
		// PrincipalDetails 객체 생성
		PrincipalDetails principalDetails = new PrincipalDetails(user);
		
		// 인증 객체 생성 (권한 관리를 스프링에게 위임)
		Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		
		// SecurityContextHolder에 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtUtils.createAccessToken(authentication);
		
		String refreshToken = handleRefreshToken(principalDetails.getUser().getUserId(), authentication);
		
		// fcm 토큰 저장
		Fcm fcm = Fcm.builder()
					 .userId(principalDetails.getUser().getUserId())
					 .fcmToken(loginRequestDto.getFcmToken())
					 .build();
		
		handleFcmToken(fcm);
		
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
	
	// 이 부분 다시 확인해서 수정
	@Transactional
	public RefreshResponseDto refresh(String refreshToken) {
		
		if (refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
			// SecurityContextHolder에서 인증객체를 가져오기
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			// 인증이 없으면 AnonymousAuthenticationFilter가 작동하여 익명토큰을 자동으로 넣음 authentication.isAuthenticated() 가 true로 세팅되어 조건 추가
			if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
				throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
			}
			
				
			// 새로운 accessToken 생성
			String accessToken = jwtUtils.createAccessToken(authentication);
			
			return RefreshResponseDto.builder()
									 .accessToken(accessToken)
									 .type(typeSafeProperties.getTokenPrefix())
									 .build();
		}
		
		return null;
	}
	
	@Transactional
	public void logout(String refreshToken) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 인증이 없으면 AnonymousAuthenticationFilter가 작동하여 익명토큰을 자동으로 넣음 authentication.isAuthenticated() 가 true로 세팅되어 조건 추가
		if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		String userId = principal.getUser().getUserId();
		
		if (refreshToken != null) {
			int delResult = tokenMapper.deleteRefreshTokenByUserId(jwtUtils.getUserNameFromJwtToken(refreshToken));
			
			if (delResult < 1) {
				throw new GlobalException("만료된 토큰 삭제에 실패하였습니다.", "REFRESH_TOKEN_DELETE_FAIL");
			}
		}
		
		int delResult = fcmMapper.deleteFcmInfoByUserId(userId);
		
		if (delResult < 1) {
			throw new GlobalException("FCM 토큰 삭제에 실패하였습니다.", "FCM_TOKEN_DELETE_FAIL");
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
						.postCd(userRequestDto.getPostCd())
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
	
	// 로그인 시 리프레쉬토큰 핸들링
	private String handleRefreshToken(String userId, Authentication authentication) {
		
		RefreshToken existingToken = tokenMapper.getRefreshTokenByUserId(userId);
		
		if (existingToken != null) {
			if (jwtUtils.validateJwtToken(existingToken.getToken())) {
				return existingToken.getToken();
			} else {
				int delResult = tokenMapper.deleteRefreshTokenByUserId(userId);
				
				if (delResult < 1) {
					throw new GlobalException("만료된 토큰 삭제에 실패하였습니다.", "REFRESH_TOKEN_DELETE_FAIL");
				}
			}
		}
		
		String refreshToken = jwtUtils.createRefreshToken(authentication);
		
		RefreshToken rt = RefreshToken.builder()
				.userId(userId)
				.token(refreshToken)
				.expiryDate(jwtUtils.getExpirationFromJwtToken(refreshToken))
				.build();
		
		// db에 저장 여기서 이미 존재하는 refreshToken이 있다면 삭제 후 생성하도록 하는 로직을 추가해야함
		int tokenResult = tokenMapper.saveRefreshToken(rt);
		
		if (tokenResult < 1) {
			throw new GlobalException("토큰 저장에 실패하였습니다.", "REFRESH_TOKEN_SAVE_FAIL");
		}
		
		return refreshToken;
	}
	
	// 로그인 시 FCM토큰 핸들링
	private void handleFcmToken(Fcm fcm) {
		
		Fcm existingToken = fcmMapper.getFcmInfoByUserIdAndFcmToken(fcm);
		
		if (existingToken != null) {
			if (fcm.getUserId().equals(existingToken.getUserId()) && !fcm.getFcmToken().equals(existingToken.getFcmToken())) {
				int delResult = fcmMapper.deleteFcmInfoByUserId(fcm.getUserId());
				
				if (delResult < 1) {
					throw new GlobalException("FCM 토큰 삭제에 실패하였습니다.", "FCM_TOKEN_DELETE_FAIL");
				}
			} else {
				return;
			}
		}
		
		int insResult = fcmMapper.insertFcmInfo(fcm);
		
		if (insResult < 1) {
			throw new GlobalException("FCM 토큰 생성에 실패하였습니다.", "FCM_TOKEN_INSERT_FAIL");
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
