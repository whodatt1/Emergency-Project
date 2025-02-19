package com.emergency.web.dto.response.emgc;

import java.util.List;

import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto.Body;
import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto.Header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmgcBsIfResponseDto {
	
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
	public static class Item {
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
	
	@Data
	public static class ApiResponse {
		private Header header;
		private Body body;
		
		// 리퀘스트 성공 여부 반환
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
