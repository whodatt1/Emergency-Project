package com.emergency.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : TypeSafeProperties.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : yml 프로퍼티 정보를 가지고 있는 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties("props")
public class TypeSafeProperties {
	// JWT
	private String jwtSecretCd;
	private int jwtAccessExpirationTime;
	private int jwtRefreshExpirationTime;
	private String tokenPrefix;
	private String headerString;
	private String refreshTokenName;
	// API
	private String apiNormalEncoding;
	private String apiHost;
	private String apiRltmPath;
}
