package com.emergency.web.jwt;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.emergency.web.auth.PrincipalDetails;
import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
* @packageName     : com.emergency.web.jwt
* @fileName        : JwtAuthenticationFilter.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : JWT 기반 인증을 처리하며 로그인 요청을 처리하고 인증결과 반환
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Log4j2
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private TypeSafeProperties typeSafeProperties;
	private AuthenticationManager authenticationManager;
	private ObjectMapper objectMapper;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TypeSafeProperties typeSafeProperties, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.typeSafeProperties = typeSafeProperties;
        this.objectMapper = objectMapper;

        this.setFilterProcessesUrl("/api/auth/login"); // 원하는 로그인 URL로 설정
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("JwtAuthenicationFilter attemptAuthentication : 로그인중..");
		
		try {
			// 자바 객체로 역직렬화
			User user = objectMapper.readValue(request.getInputStream(), User.class);
			
			// 토큰 생성
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());
			
			// 인증 시도 성공시 Authentication 객체 반환
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			return authentication;
		} catch (IOException e) {
			log.error("인증 요청 파싱에 실패하였습니다.");
			
			try {
				// HTTP 응답 메시지
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "인증 요청 파싱에 실패하였습니다."); // 400
			} catch (IOException e2) {
				log.error("응답 작성 중 오류 발생", e2);
			}
			
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("JwtAuthenicationFilter successfulAuthentication : 인증 완료");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		// Hash 암호 방식 
		// HMAC512는 SecetKey 값을 가지고 있다.
		String jwtToken = JWT.create()
							 .withSubject(principalDetails.getUser().getId() + "token")
							 .withExpiresAt(Date.from(Instant.now().plus(typeSafeProperties.getJwtExpirationTime(), ChronoUnit.MINUTES)))
							 .withClaim("id", principalDetails.getUser().getId())
							 .sign(Algorithm.HMAC512(typeSafeProperties.getJwtSecretCd()));
		
		// Authorization 헤더에 JWT 토큰 추가
		response.addHeader(typeSafeProperties.getHeaderString(), typeSafeProperties.getTokenPrefix() + jwtToken);
	}
	
	
}
