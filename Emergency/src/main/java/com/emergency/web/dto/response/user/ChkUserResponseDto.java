package com.emergency.web.dto.response.user;

import com.emergency.web.dto.request.user.ChkUserRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.dto.response.user
* @fileName        : ChkUserResponseDto.java
* @author          : KHK
* @date            : 2025.08.20
* @description     : 회원정보 관리 확인 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.08.20        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChkUserResponseDto {
	private boolean chk;
	private String verifyToken;
}
