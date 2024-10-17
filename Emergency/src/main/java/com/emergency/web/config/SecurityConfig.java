package com.emergency.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.emergency.web.jwt.JwtAuthenticationFilter;
import com.emergency.web.jwt.JwtAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : SecurityConfig.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : 시큐리티 설정
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CorsFilter corsFilter;
	private final TypeSafeProperties typeSafeProperties;
	private final ObjectMapper objectMapper;
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
		    .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
		    .addFilter(corsFilter) // CORS 정책 필터
		    .formLogin(form -> form.disable()) // 폼로그인 미사용
		    .httpBasic(HttpBasicConfigurer::disable) // HTTP 로그인 방식 미사용
		    .addFilter(new JwtAuthenticationFilter(authenticationManager, typeSafeProperties, objectMapper));
		return http.build();
	}
}
