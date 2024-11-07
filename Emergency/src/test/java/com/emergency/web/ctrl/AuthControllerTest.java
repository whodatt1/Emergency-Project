package com.emergency.web.ctrl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.emergency.web.dto.request.JoinRequestDto;
import com.emergency.web.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
* @packageName     : com.emergency.web.ctrl
* @fileName        : AuthControllerTest.java
* @author          : KHK
* @date            : 2024.10.23
* @description     : AuthContoller TDD
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.23        KHK                최초 생성
 */

@WebMvcTest
@WebAppConfiguration
public class AuthControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	AuthService authService;
	
	@MockBean
	BCryptPasswordEncoder encoder;
	
	@Autowired
	ObjectMapper objectMapper;
	
	JoinRequestDto joinRequestDto;
	
	@BeforeEach // 테스트 이전 실행
	public void init() {
		// 고정된 시간 설정
	    LocalDateTime fixedTime = LocalDateTime.of(2024, 10, 23, 0, 0, 0, 0);
	    
	    // JoinRequestDto 초기화
	    joinRequestDto = JoinRequestDto.builder()
	                                   .userId("kim")
	                                   .password("1234")
	                                   .email("abc@abc.com")
	                                   .hp("010-1111-1111")
	                                   .address("seoul")
	                                   .regAt(fixedTime)
	                                   .modAt(fixedTime)
	                                   .roles("ROLE_USER")
	                                   .build();
	}
	
	@Test
	@DisplayName("회원가입 성공 시")
	@WithMockUser
	void join_sucess() throws Exception {
		
		// when
		mockMvc.perform(post("/api/v1/auth/signup")
						.with(csrf())
				        .contentType(MediaType.APPLICATION_JSON_VALUE)
				        .content(objectMapper.writeValueAsBytes(joinRequestDto)))
		       .andExpect(status().isOk());
		
		// then
		verify(authService).signUp(any());
	}
	
	@Test
	@DisplayName("회원가입 실패 시")
	@WithMockUser
	void join_fail() throws Exception {
		
	}
}
