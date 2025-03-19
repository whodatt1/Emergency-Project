package com.emergency.web.dto.response.bjd;

import java.time.LocalDate;
import java.util.List;

import com.emergency.web.model.BJD;
import com.emergency.web.model.EmgcBsIf;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BJDResponseDto {
	
    private String pastCd;
    private String riNm;
    private Long lawCd;
    private LocalDate deletedDate;
    private LocalDate createdDate;
    private int rank;
    private String siNm;
    private String sidoNm;
    private String dongNm;
	
	@Getter
	@Setter
	@ToString
	public static class Item {
		@JsonProperty("과거법정동코드")
	    private String pastCd;
	    @JsonProperty("리명")
	    private String riNm;
	    @JsonProperty("법정동코드")
	    private Long lawCd;
	    @JsonProperty("삭제일자")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDate deletedDate;
	    @JsonProperty("생성일자")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDate createdDate;
	    @JsonProperty("순위")
	    private int rank;
	    @JsonProperty("시군구명")
	    private String siNm;
	    @JsonProperty("시도명")
	    private String sidoNm;
	    @JsonProperty("읍면동명")
	    private String dongNm;
	}
	
	public BJD toEntity() {
		return BJD.builder()
				.pastCd(pastCd)
				.riNm(riNm)
				.lawCd(lawCd)
				.deletedDate(deletedDate)
				.createdDate(createdDate)
				.rank(rank)
				.siNm(siNm)
				.sidoNm(sidoNm)
				.dongNm(dongNm)
				.build();
	}
	
	@Data
	public static class ApiResponse {
		private int currentCount;
		private List<Item> data;
		
		// 리퀘스트 성공 여부 반환 (개별건이라 미사용)
		public boolean requestSuccess() {
			
			if (data != null && data.size() != 0) {
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
