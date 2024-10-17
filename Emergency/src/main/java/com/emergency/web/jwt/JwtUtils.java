package com.emergency.web.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.emergency.web.config.TypeSafeProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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
	
	// 토큰에서 유저 ID 추출
	public String getUserNameFromJwtToken(String token) {
		// 비밀 키를 UTF-8 문자열로 변환 후 HMAC512에 사용할 키로 지정
		byte[] secretByteKey = typeSafeProperties.getJwtSecretCd().getBytes(StandardCharsets.UTF_8);
		Key signKey = new SecretKeySpec(secretByteKey, "HmacSHA512");
		
		// 토큰 파싱 후 "id" 클레임 추출
		return Jwts.parserBuilder()
				   .setSigningKey(signKey)
				   .build()
				   .parseClaimsJwt(token)
				   .getBody()
				   .get("id", String.class); // 문자열로 반환
	}
	
	// 토큰 검증 메서드
	public boolean validateJwtToken(String token) throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
		
		// 비밀 키를 UTF-8 문자열로 변환 후 HMAC512에 사용할 키로 지정
		byte[] secretByteKey = typeSafeProperties.getJwtSecretCd().getBytes(StandardCharsets.UTF_8);
		Key signKey = new SecretKeySpec(secretByteKey, "HmacSHA512");
		
		// JWT 토큰을 검증
		Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token);
		return true;
		
	}
}
