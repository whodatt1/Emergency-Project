package com.emergency.web.dto.response.emgc;

/**
 * 
* @packageName     : com.emergency.web.dto.response.auth
* @fileName        : RealTimeInfoResponseDto.java
* @author          : KHK
* @date            : 2025.01.11
* @description     : 응급실 실시간 가용병상정보 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.11        KHK                최초 생성
 */


public class RealTimeInfoResponseDto {
	private String resultCode; // 결과 코드
	private String hpid; // 병원 코드
	private String hvidate; // 입력일시
	private int hvec; // 응급실 가용 인원
	private int hvoc; // 수술실 가용 인원
}
