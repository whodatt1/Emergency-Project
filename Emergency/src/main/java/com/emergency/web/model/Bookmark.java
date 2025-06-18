package com.emergency.web.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : Bookmark.java
* @author          : KHK
* @date            : 2025.06.18
* @description     : Bookmark VO ( 즐겨찾기 )
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.18        KHK                최초 생성
 */

@Getter
@Builder
public class Bookmark {
	
	private String userId; // PRIMARY KEY
	private String hpId; // PRIMARY KEY
	private LocalDateTime regAt;
	private LocalDateTime modAt;
}
