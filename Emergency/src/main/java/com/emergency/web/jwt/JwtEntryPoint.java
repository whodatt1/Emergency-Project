package com.emergency.web.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
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
public class JwtEntryPoint implements AuthenticationEntryPoint {
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.error("권한 없음 오류: {}", authException.getMessage());
		
		// @ControllerAdvice는 DispatcherServlet 내부에서 컨트롤러 예외를 처리하므로, Security 필터 체인 단계 예외는 못 잡는 경우가 많다고 한다..
		// throw new GlobalException("권한이 없거나 유효하지 않은 인증입니다.", "UNAUTHORIZED_ACCESS", HttpStatus.UNAUTHORIZED); 
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한이 없거나 유효하지 않은 인증입니다.");
	}
	
	
}
