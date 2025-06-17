package com.emergency.web.dto.request.bookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.dto.request.bookmark
* @fileName        : BookmarkInsRequestDto.java
* @author          : KHK
* @date            : 2025.06.17
* @description     : Bookmark ins 요청 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.17        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkInsRequestDto {
	
	private String hpId;
}
