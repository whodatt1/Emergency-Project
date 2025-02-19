package com.emergency.web.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmgcBsIf {
	
	private String hpid; // 기관 ID
	private String dutyName; // 기관명
	private String postCdn1; // 우편번호1
	private String postCdn2; // 우편번호2
	private String dutyAddr; // 주소
	private String dutyTel1; // 대표전화1
	private String dutyInf; // 기관설명상세
	private String wgs84Lon; // 병원 경도
	private String wgs84Lat; // 병원 위도
	private String dgidIdName; // 진료 과목
}
