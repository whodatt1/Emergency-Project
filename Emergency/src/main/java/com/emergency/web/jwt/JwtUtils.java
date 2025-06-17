package com.emergency.web.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.emergency.web.auth.PrincipalDetails;
import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.exception.GlobalException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
* @packageName     : com.emergency.web.jwt
* @fileName        : JwtUtils.java
* @author          : KHK
* @date            : 2024.10.17
* @description     : JWT UTILS
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.17        KHK                최초 생성
 */

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtUtils {
	
	private final TypeSafeProperties typeSafeProperties;
	
	public String createAccessToken(Authentication authentication) {
	    try {
	        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
	        Date expiryDate = new Date(new Date().getTime() + typeSafeProperties.getJwtAccessExpirationTime());
	        byte[] secretByteKey = typeSafeProperties.getJwtSecretCd().getBytes(StandardCharsets.UTF_8);
	        Key signKey = new SecretKeySpec(secretByteKey, "HmacSHA512");

	        return Jwts.builder()
	                   .setSubject(principalDetails.getUsername())
	                   .claim("user-email", principalDetails.getUser().getEmail())
	                   .setIssuedAt(new Date())
	                   .setExpiration(expiryDate)
	                   .signWith(signKey, SignatureAlgorithm.HS512)
	                   .compact();
	    } catch (JwtException e) {
	        // JWT 관련 오류 발생 시 처리
	        throw new GlobalException("JWT 생성 중 오류가 발생했습니다.", "ACCESS_TOKEN_CREATION_ERROR");
	    } catch (Exception e) {
	        // 다른 일반적인 예외 처리
	        throw new GlobalException("토큰 생성 중 오류가 발생했습니다.", "ACCESS_TOKEN_CREATION_ERROR");
	    }
	}

	public String createRefreshToken(Authentication authentication) {
	    try {
	        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
	        Date expiryDate = new Date(new Date().getTime() + typeSafeProperties.getJwtRefreshExpirationTime());
	        byte[] secretByteKey = typeSafeProperties.getJwtSecretCd().getBytes(StandardCharsets.UTF_8);
	        Key signKey = new SecretKeySpec(secretByteKey, "HmacSHA512");

	        return Jwts.builder()
	                   .setSubject(principalDetails.getUsername())
	                   .setIssuedAt(new Date())
	                   .setExpiration(expiryDate)
	                   .signWith(signKey, SignatureAlgorithm.HS512)
	                   .compact();
	    } catch (JwtException e) {
	        // JWT 관련 오류 발생 시 처리
	        throw new GlobalException("리프레쉬 토큰 생성 중 오류가 발생했습니다.", "REFRESH_TOKEN_CREATION_ERROR");
	    } catch (Exception e) {
	        // 다른 일반적인 예외 처리
	        throw new GlobalException("토큰 생성 중 오류가 발생했습니다.", "REFRESH_TOKEN_CREATION_ERROR");
	    }
	}

	public String getUserNameFromJwtToken(String token) {
	    try {
	        // 비밀 키를 UTF-8 문자열로 변환 후 HMAC512에 사용할 키로 지정
	        byte[] secretByteKey = typeSafeProperties.getJwtSecretCd().getBytes(StandardCharsets.UTF_8);
	        Key signKey = new SecretKeySpec(secretByteKey, "HmacSHA512");

	        // 토큰 파싱 후 subject 추출
	        return Jwts.parserBuilder()
	                   .setSigningKey(signKey)
	                   .build()
	                   .parseClaimsJws(token)
	                   .getBody()
	                   .getSubject();
	    } catch (JwtException e) {
	        // JWT 파싱 오류 또는 서명 검증 실패 시 처리
	        throw new GlobalException("유효하지 않은 토큰입니다.", "INVALID_TOKEN");
	    } catch (Exception e) {
	        // 다른 일반적인 예외 처리
	        throw new GlobalException("토큰 처리 중 오류가 발생했습니다.", "TOKEN_PROCESSING_ERROR");
	    }
	}

	
	// 토큰에서 만료기간 추출
	public LocalDateTime getExpirationFromJwtToken(String token) {
	    try {
	        // 비밀 키를 UTF-8 문자열로 변환 후 HMAC512에 사용할 키로 지정
	        byte[] secretByteKey = typeSafeProperties.getJwtSecretCd().getBytes(StandardCharsets.UTF_8);
	        Key signKey = new SecretKeySpec(secretByteKey, "HmacSHA512");

	        // JWT 파싱 및 만료일 추출
	        Date expiration = Jwts.parserBuilder()
	                              .setSigningKey(signKey)
	                              .build()
	                              .parseClaimsJws(token)
	                              .getBody()
	                              .getExpiration();

	        // LocalDateTime으로 변환하여 반환
	        return expiration.toInstant()
	                         .atZone(ZoneId.systemDefault()) // 시스템 기본 시간대 사용
	                         .toLocalDateTime();
	    } catch (JwtException e) {
	        // JWT 파싱 오류 또는 서명 검증 실패 시 처리
	        throw new GlobalException("유효하지 않은 토큰입니다.", "INVALID_TOKEN");
	    } catch (Exception e) {
	        // 다른 일반적인 예외 처리
	        throw new GlobalException("토큰 처리 중 오류가 발생했습니다.", "TOKEN_PROCESSING_ERROR");
	    }
	}
	
	// 토큰 검증 메서드
	public boolean validateJwtToken(String token) {
		
		try {
			// 비밀 키를 UTF-8 문자열로 변환 후 HMAC512에 사용할 키로 지정
			byte[] secretByteKey = typeSafeProperties.getJwtSecretCd().getBytes(StandardCharsets.UTF_8);
			Key signKey = new SecretKeySpec(secretByteKey, "HmacSHA512");
			
			// JWT 토큰을 검증
			Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token);
			
			return true;
		} catch (MalformedJwtException e) {
			log.error("유효하지 JWT 토큰 : {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("만료된 JWT 토큰 : {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("지원되지 않는 JWT 토큰 : {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT Claims String이 비어있습니다. : {}", e.getMessage());
		}
		
		return false;
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
