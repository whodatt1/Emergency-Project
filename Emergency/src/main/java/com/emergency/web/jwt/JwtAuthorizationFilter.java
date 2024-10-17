package com.emergency.web.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.emergency.web.config.TypeSafeProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
* @packageName     : com.emergency.web.jwt
* @fileName        : JwtAuthorizationFilter.java
* @author          : KHK
* @date            : 2024.10.17
* @description     : 사용자의 요청마다 JWT를 검증하는 필터 클래스
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.17        KHK                최초 생성
 */

@Log4j2
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter  {
	
	private final UserDetailsService userDetailsService;
	private final JwtUtils jwtUtils;
	private final TypeSafeProperties typeSafeProperties;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			// 토큰 파싱
			String jwtToken = parseJwt(request);
			
			if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
				// 유저 ID 추출
				String userId = jwtUtils.getUserNameFromJwtToken(jwtToken);
				
				// userDetail 객체 가져오기
				UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
				
				// 토큰 생성
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				// 인증 정보를 설정할 때 추가적인 상세정보를 추가해 줌 이걸 사용하여 보안 관련 처리 가능
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// 권한을 시큐리티에서 관리하도록 하기 위해 SecurityContextHolder에 athentication 저장 (인증 정보가 스레드 로컬에 저장되는 방식)
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (MalformedJwtException e) {
			log.error("유효하지 않은 JWT 토큰입니다.");
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
	    } catch (ExpiredJwtException e) {
	    	log.error("JWT 토큰이 만료되었습니다.");
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 만료되었습니다.");
	    } catch (UnsupportedJwtException e) {
	    	log.error("지원되지 않는 JWT 토큰입니다.");
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다.");
	    } catch (IllegalArgumentException e) {
	    	log.error("JWT Claims string이 비어있습니다.");
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT Claims string이 비어있습니다.");
	    } catch (Exception e) {
	    	log.error("서버 오류가 발생했습니다.");
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
	    }
	}
	
	private String parseJwt(HttpServletRequest request) {
		// Authorization 헤더 값을 가져옴
		String headerAuth = request.getHeader(typeSafeProperties.getHeaderString());
		
		// 토큰 타입 검증 후 반환
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(typeSafeProperties.getTokenPrefix())) {
			return headerAuth.substring(7, headerAuth.length());
		}
		
		return null;
	}
}
