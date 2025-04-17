package com.emergency.web.dto.response.bjd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.dto.response.bjd
* @fileName        : BJDResponseDto.java
* @author          : KHK
* @date            : 2025.04.09
* @description     : 법정동 코드 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.04.09        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BJDResponseDto {
	
	private String bjdCd;
	private String bjdNm;
}
