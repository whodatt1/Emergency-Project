package com.emergency.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor  // ⭐ MyBatis가 객체 생성할 수 있게 해줌
@AllArgsConstructor
@ToString
public class EmgcBsIf {
	private String hpId; // 기관 ID
	private String dutyName; // 기관명
	private String dutyHayn; // 입원실 가용 여부 1/2
	private String dutyEryn; // 응급실 운영 여우 1/2
	private String postCdn1; // 우편번호1
	private String postCdn2; // 우편번호2
	private String dutyAddr; // 주소
	private String dutyTel; // 대표전화1
	private String dutyErTel; // 응급실전화
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
	private String dutyLon; // 병원 경도
	private String dutyLat; // 병원 위도
	private String dgidIdName; // 진료 과목
}
