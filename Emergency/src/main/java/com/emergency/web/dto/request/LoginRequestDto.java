package com.emergency.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 
* @packageName     : com.emergency.web.dto.request
* @fileName        : AuthRequestDto.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : Auth 요청 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Getter
@Builder
public class LoginRequestDto {
	
	private String userId;
	private String password;
}
