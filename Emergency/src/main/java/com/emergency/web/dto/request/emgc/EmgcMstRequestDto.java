package com.emergency.web.dto.request.emgc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.dto.request
* @fileName        : JoinRequestDto.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : 응급실 실시간 가용병상정보 요청 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmgcMstRequestDto {
	private String sidoNm;
	private String gugunNm;
	private String dongNm;
	private String dutyNm;
}
