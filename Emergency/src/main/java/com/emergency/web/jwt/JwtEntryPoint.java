package com.emergency.web.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.emergency.web.exception.GlobalException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {@Override
	
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.error("권한 없음 오류: {}", authException.getMessage());
		
		throw new GlobalException("권한이 없거나 유효하지 않은 인증입니다.", "UNAUTHORIZED_ACCESS"); 
	}
	
	
}
