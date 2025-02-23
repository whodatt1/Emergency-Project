package com.emergency.web.dto.request.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.dto.request
* @fileName        : JoinRequestDto.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : Join 요청 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {
	
	@NotEmpty(message = "아이디는 필수 입력 사항입니다.")
	private String userId;
	
	@NotEmpty(message = "비밀번호는 필수 입력 사항입니다.")
	private String password;
	
	@NotEmpty(message = "이메일은 필수 입력 사항입니다.")
	@Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
	private String email;
	
	@NotEmpty(message = "휴대폰 번호는 필수 입력 사항입니다.")
	@Pattern(regexp = "^(010|011|016|017|018|019)-?\\d{3,4}-?\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
	private String hp;
	
	@NotEmpty(message = "우편 번호는 필수 입력 사항입니다.")
	private String postCd;
	
	@NotEmpty(message = "주소는 필수 입력 사항입니다.")
	private String address;
}
