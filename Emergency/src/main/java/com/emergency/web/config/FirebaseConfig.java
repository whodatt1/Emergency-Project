package com.emergency.web.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : FirebaseConfig.java
* @author          : KHK
* @date            : 2025.06.22
* @description     : FirebaseConfig
* 					 1. 메시지 전달을 위한 파이어베이스 설정
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.22        KHK                최초 생성
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {
	
	private final TypeSafeProperties typeSafeProperties;
	
	@Bean
 	public FirebaseApp firebaseApp() {
		try {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(typeSafeProperties.getFirebasePath()).getInputStream())
					)
					.build();
			
			log.info("firebase app 초기화 성공");
			return FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			log.info("firebase app 초기화 실패 {}", e.getMessage());
			return null;
		}
	}
	
	// 푸시 메시지 전송에 사용하는 객체
	@Bean
	public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
		return FirebaseMessaging.getInstance(firebaseApp);
	}

}
