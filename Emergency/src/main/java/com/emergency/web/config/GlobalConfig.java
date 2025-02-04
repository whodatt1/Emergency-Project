package com.emergency.web.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GlobalConfig {
	
	// WebConfig 설정 예정
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
