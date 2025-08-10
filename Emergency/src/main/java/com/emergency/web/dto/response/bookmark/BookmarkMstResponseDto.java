package com.emergency.web.dto.response.bookmark;


import java.util.List;

import com.emergency.web.dto.response.emgc.EmgcMstResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.dto.response.bookmark
* @fileName        : BookmarkMstResponseDto.java
* @author          : KHK
* @date            : 2025.08.07
* @description     : Bookmark Mst List 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.08.07        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookmarkMstResponseDto {
	
	private List<EmgcMstResponseDto> content;
	private int totalCnt;
}
