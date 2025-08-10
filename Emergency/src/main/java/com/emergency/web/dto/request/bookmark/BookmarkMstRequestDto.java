package com.emergency.web.dto.request.bookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
* @packageName     : com.emergency.web.dto.request.bookmark
* @fileName        : BookmarkMstRequestDto.java
* @author          : KHK
* @date            : 2025.08.06
* @description     : Bookmark Mst List 요청 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.08.06        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkMstRequestDto {
	private String dutyNm;
	private int offset;
	private int size;
	private String userId;
}
