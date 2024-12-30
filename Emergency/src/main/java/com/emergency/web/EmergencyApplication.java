package com.emergency.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(value = {"com.emergency.web.mapper"})
@SpringBootApplication
public class EmergencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergencyApplication.class, args);
	}

}
