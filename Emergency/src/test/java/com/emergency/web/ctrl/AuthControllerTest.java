package com.emergency.web.ctrl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
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
import com.emergency.web.dto.request.LoginRequestDto;
import com.emergency.web.dto.response.LoginResponseDto;
import com.emergency.web.exception.GlobalException;
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
			   .andDo(print())
		       .andExpect(status().isOk());
		
		// then
		verify(authService).signUp(any());
	}
	
	@Test
	@DisplayName("회원가입 실패 시 (아이디 중복)")
	@WithMockUser
	void join_fail() throws Exception {
		
		// given
		doThrow(new GlobalException("아이디가 중복되었습니다.", "DUPLICATE_USER_ID")).when(authService).signUp(any());
		
		// when
		mockMvc.perform(post("/api/v1/auth/signup")
						.with(csrf())
				        .contentType(MediaType.APPLICATION_JSON_VALUE)
				        .content(objectMapper.writeValueAsBytes(joinRequestDto)))
			   .andDo(print())
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message").value("아이디가 중복되었습니다."))
			   .andExpect(jsonPath("$.errorCd").value("DUPLICATE_USER_ID"));
				
		// then
		verify(authService).signUp(any());
	}
	
	@Test
	@DisplayName("로그인 실패 시 (아이디 없음)")
	@WithMockUser
	void login_fail_id_not_found() throws Exception {
		
		// given
		doThrow(new GlobalException("아이디가 존재하지 않습니다.", "USER_ID_NOT_FOUND")).when(authService).login(any());
		
		// when
		mockMvc.perform(post("/api/v1/auth/login")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsBytes(LoginRequestDto.builder()
																			   .userId(joinRequestDto.getUserId())
																			   .password(joinRequestDto.getPassword())
																			   .build())))
			   .andDo(print())
			   .andExpect(status().isBadRequest())
			   .andExpect(jsonPath("$.message").value("아이디가 존재하지 않습니다."))
			   .andExpect(jsonPath("$.errorCd").value("USER_ID_NOT_FOUND"));
		
		// then
		verify(authService).login(any());
	}
	
	@Test
	@DisplayName("로그인 실패 시 (패스워드 틀림)")
	@WithMockUser
	void login_fail_invalid_password() throws Exception {
		
		// given
		doThrow(new GlobalException("패스워드가 일치하지 않습니다.", "INVALID_PASSWORD")).when(authService).login(any());
		
		// when
		mockMvc.perform(post("/api/v1/auth/login")
			           .with(csrf())
			           .contentType(MediaType.APPLICATION_JSON_VALUE)
			           .content(objectMapper.writeValueAsBytes(LoginRequestDto.builder()
			        		   												  .userId(joinRequestDto.getUserId())
			        		   												  .password(joinRequestDto.getPassword())
			        		   												  .build())))
					   .andDo(print())
					   .andExpect(status().isBadRequest())
					   .andExpect(jsonPath("$.message").value("패스워드가 일치하지 않습니다."))
					   .andExpect(jsonPath("$.errorCd").value("INVALID_PASSWORD"));
		
		// then
		verify(authService).login(any());
	}
	
	@Test
	@DisplayName("로그인 성공 시")
	@WithMockUser
	void login_success() throws Exception {
		
		// given
		when(authService.login(any())).thenReturn(LoginResponseDto.builder()
																  .accessToken("token")
																  .type("Bearer ")
																  .build());
		
		// when
		mockMvc.perform(post("/api/v1/auth/login")
		           	   .with(csrf())
		           	   .contentType(MediaType.APPLICATION_JSON_VALUE)
			           .content(objectMapper.writeValueAsBytes(LoginRequestDto.builder()
			        		   												  .userId(joinRequestDto.getUserId())
			        		   												  .password(joinRequestDto.getPassword())
			        		   												  .build())))
					   .andDo(print())
					   .andExpect(status().isOk())
					   .andExpect(jsonPath("$.accessToken").value("token"))
					   .andExpect(jsonPath("$.type").value("Bearer "));
		
		// then
		verify(authService).login(any());
	}
}
