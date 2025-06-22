package com.emergency.web.config;

import org.springframework.context.annotation.Configuration;

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
	
	

}
