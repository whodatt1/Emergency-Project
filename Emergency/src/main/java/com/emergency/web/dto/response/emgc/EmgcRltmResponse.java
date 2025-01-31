package com.emergency.web.dto.response.emgc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : EmgcRltmResponse.java
* @author          : KHK
* @date            : 2025.01.31
* @description     : 응급 가용 인원 API 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.31        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmgcRltmResponse {
	
	private Header header;
	
	@Getter
	@Setter
	public static class Header {
		private String resultCode; // 00 => 성공
		private String resultMsg;
	}
	
	@Getter
	@Setter
	public static class Body {
		private List<Item> itemList;
		private int numOfRows;
		private int pageNo;
		private int totalCount;
	}
	
	@Getter
	@Setter
	public static class Item {
		private long rnum; // 일련번호
		private String hpid; // 기관코드
		private String phpid; // (구) 기관코드
		private String hvidate; // 입력일시
		private int hvec; // 응급실 일반 병상
		private int hvoc; // [기타] 수술실
		private int hvcc; // [중환자실] 신경과
		private int hvncc; // [중환자실] 신생아
		private int hvccc; // [중환자실] 흉부외과
		private int hvicc; // [중환자실] 일반
		private int hvgc; // [입원실] 일반
		private String hvdnm; // 당직의
		private String hvctayn; // CT 가용 여부
		private String hvmriayn; // MRI 가용 여부
		private String hvangioayn; // 혈관촬영기 가용 여부
		private String hvventiayn; // 인공호흡기 가용 여부
		private String hvincuayn; // 인큐베이터 가용 여부
		private String hvcrrtayn; // CRRT 가용 여부
		private String hvecmoayn; // ECMO 가용 여부
		private String hvoxyayn; // 고압 산소 치료기 가용 여부
		private String hvhypoayn; // 중심 체온 조절 유도기 가용 여부
		private String hvamyn; // 구급차 가용 여부
		private String hv1; // 응급실 당직의 직통 연락처
		private int hv2; // [중환자실] 내과 인원
		private int hv3; // [중환자실] 외과 인원
		private int hv4; // 외과입원실 (정형외과) 인원
		private int hv5; // 신경과입원실 인원
		private int hv6; // [중환자실] 신경외과 인원
		private int hv7; // 약물 중환자 인원
		private int hv8; // [중환자실] 화상 인원
		private int hv9; // [중환자실] 외상 인원
		private String hv10; // VENTI(소아) 여부
		private String hv11; // 인큐베이터(보육기) 여부
		private String hv12; // 소아당직의 직통 연락처
		private int hv13; // 격리진료구역 음압격리병상
		private int hv14; // 격리진료구역 일반격리병상
		private int hv15; // 소아 음압 격리 병상
		private int hv16; // 소아 일반 격리 병상
		private int hv17; // [응급전용] 중환자실 음압격리 인원
		private int hv18; // [응급전용] 중환자실 일반격리 인원
		private int hv19; // [응급전용] 입원실 음압격리 인원
		private int hv21; // [응급전용] 입원실 일반격리 인원
		private int hv22; // 감염병 전담병상 중환자실 인원
		
	}
	
	@Data
	public static class ApiResponse {
		private Header header;
		private Body body;
		
		// 리퀘스트 성공 여부 반환
		public boolean requestSuccess() {
			
			List<Item> itemList = body.getItemList();
			
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
