package com.emergency.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("props")
public class TypeSafeProperties {
	private String jwtSecretCd;
	private int jwtExpirationTime;
}
