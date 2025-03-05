package com.emergency.web.dto.response.emgc;

import java.util.List;

import com.emergency.web.model.EmgcBsIf;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : EmgcBsIfResponseDto.java
* @author          : KHK
* @date            : 2025.01.31
* @description     : 응급 병원 기본 정보 API 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.31        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmgcBsIfResponseDto {
	
	private String hpid; // 기관 ID
	private String dutyName; // 기관명
	private String postCdn1; // 우편번호1
	private String postCdn2; // 우편번호2
	private String dutyAddr; // 주소
	private String dutyTel1; // 대표전화1
	private String dutyTel3; // 응급실전화
	private int hvec; // 응급실 인원
	private int hvoc; // 수술실 인원
	private int hvcc; // 신경 중환자
	private int hvncc; // 신생 중환자
	private int hvccc; // 흉부 중환자
	private int hvicc; // 일반 중환자
	private int hvgc; // 입원실
	private String dutyHayn; // 입원실 가용 여부 1/""
	private int dutyHano; // 병상 수
	private String dutyInf; // 기관설명상세
	private String dutyMapimg; // 긴이약도
	private String dutyEryn; // 응급실 운영 벼우 1/2
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
	private String MKioskTy25; // 응급실
	private String MKioskTy1;
	private String MKioskTy2;
	private String MKioskTy3;
	private String MKioskTy4;
	private String MKioskTy5;
	private String MKioskTy6;
	private String MKioskTy7;
	private String MKioskTy8;
	private String MKioskTy9;
	private String MKioskTy10;
	private String MKioskTy11;
	private String wgs84Lon; // 병원 경도
	private String wgs84Lat; // 병원 위도
	private String dgidIdName; // 진료 과목
	private int hpbdn;
	private int hpccuyn;
	private int hpcuyn;
	private int hperyn;
	private int hpgryn;
	private int hpicuyn;
	private int hpnicuyn;
	private int hpopyn;
	
	@Getter
	@Setter
	public static class Header {
		private String resultCode; // 00 => 성공
		private String resultMsg;
	}
	
	@Getter
	@Setter
	public static class Body {
		private List<Item> items;
		private int numOfRows;
		private int pageNo;
		private int totalCount;
	}
	
	@Getter
	@Setter
	@ToString
	public static class Item {
		private String hpid; // 기관 ID
		private String dutyName; // 기관명
		private String postCdn1; // 우편번호1
		private String postCdn2; // 우편번호2
		private String dutyAddr; // 주소
		private String dutyTel1; // 대표전화1
		private String dutyTel3; // 응급실전화
		private int hvec; // 응급실 인원
		private int hvoc; // 수술실 인원
		private int hvcc; // 신경 중환자
		private int hvncc; // 신생 중환자
		private int hvccc; // 흉부 중환자
		private int hvicc; // 일반 중환자
		private int hvgc; // 입원실
		private String dutyHayn; // 입원실 가용 여부 1/""
		private int dutyHano; // 병상 수
		private String dutyInf; // 기관설명상세
		private String dutyMapimg; // 긴이약도
		private String dutyEryn; // 응급실 운영 벼우 1/2
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
		@JsonProperty("MKioskTy25")
		private String MKioskTy25; // 응급실
		@JsonProperty("MKioskTy1")
		private String MKioskTy1;
		@JsonProperty("MKioskTy2")
		private String MKioskTy2;
		@JsonProperty("MKioskTy3")
		private String MKioskTy3;
		@JsonProperty("MKioskTy4")
		private String MKioskTy4;
		@JsonProperty("MKioskTy5")
		private String MKioskTy5;
		@JsonProperty("MKioskTy6")
		private String MKioskTy6;
		@JsonProperty("MKioskTy7")
		private String MKioskTy7;
		@JsonProperty("MKioskTy8")
		private String MKioskTy8;
		@JsonProperty("MKioskTy9")
		private String MKioskTy9;
		@JsonProperty("MKioskTy10")
		private String MKioskTy10;
		@JsonProperty("MKioskTy11")
		private String MKioskTy11;
		private String wgs84Lon; // 병원 경도
		private String wgs84Lat; // 병원 위도
		private String dgidIdName; // 진료 과목
		private int hpbdn;
		private int hpccuyn;
		private int hpcuyn;
		private int hperyn;
		private int hpgryn;
		private int hpicuyn;
		private int hpnicuyn;
		private int hpopyn;
	}
	
	public EmgcBsIf toEntity() {
		
		return EmgcBsIf.builder()
				.hpId(hpid)
				.dutyName(dutyName)
				.postCdn1(postCdn1)
				.postCdn2(postCdn2)
				.dutyAddr(dutyAddr)
				.dutyTel(dutyTel1)
				.dutyInf(dutyInf)
				.dutyLon(wgs84Lon)
				.dutyLat(wgs84Lat)
				.dgidIdName(dgidIdName)
				.build();
	}
	
	@Data
	public static class ApiResponse {
		private Header header;
		private Body body;
		
		// 리퀘스트 성공 여부 반환 (개별건이라 미사용)
		public boolean requestSuccess() {
			
			List<Item> itemList = body.getItems();
			
			if (itemList != null && itemList.size() != 0) {
				return true;
			}
			
			return false;
		}
		
		// 마지막 페이지 여부 반환
		public boolean isLastPage() {
			
			if (!requestSuccess()) {
				return true;
			}
			
			return false;
		}
	}
}
