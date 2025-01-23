package com.emergency.web.dto.request.emgc;

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


public class RealTimeInfoRequestDto {
	private String stage1; // 시/도
	private String stage2; // 시/군/구
	private int pageNo; // 페이지 번호
	private int numOfRows; // 목록 건수
}
