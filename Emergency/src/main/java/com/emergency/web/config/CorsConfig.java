package com.emergency.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : CorsConfig.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : CORS 정책 설정 (리액트에서 요청 가능하도록)
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Configuration
public class CorsConfig {
	
	@Bean
	CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // 자격증명을 포함하여 요청 가능하도록 설정
		// 개발용
		//config.addAllowedOrigin("http://localhost:3000");
		// 도커 로컬 테스트용
		config.addAllowedOrigin("http://localhost");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		
		// Authorization 헤더를 클라이언트에서 읽을 수 있도록 노출 설정
		config.addExposedHeader("Authorization");
		source.registerCorsConfiguration("/api/**", config);
		return new CorsFilter(source);
	}
}
