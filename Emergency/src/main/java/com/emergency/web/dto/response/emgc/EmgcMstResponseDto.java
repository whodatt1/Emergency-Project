package com.emergency.web.dto.response.emgc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.dto.response.emgc
* @fileName        : EmgcMstResponseDto.java
* @author          : KHK
* @date            : 2025.04.17
* @description     : 응급 병원 MST 정보 DTO
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.04.17        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmgcMstResponseDto {
	
	private String hpId; // 기관 ID
	private String dutyName; // 기관명
	private String dutyHayn; // 입원실 가용 여부 1/2
	private String dutyEryn; // 응급실 운영 벼우 1/2
	private String postCdn1; // 우편번호1
	private String postCdn2; // 우편번호2
	private String dutyAddr; // 주소
	private String dutyTel; // 대표전화1
	private String dutyInf; // 기관설명상세
	private String dutyTime1c;
	private String dutyTime2c;
	private String dutyTime3c;
	private String dutyTime4c;
	private String dutyTime5c;
	private String dutyTime6c;
	private String dutyTime7c;
	private String dutyTime8c;
	private String dutyTime1s;
	private String dutyTime2s;
	private String dutyTime3s;
	private String dutyTime4s;
	private String dutyTime5s;
	private String dutyTime6s;
	private String dutyTime7s;
	private String dutyTime8s;
}
