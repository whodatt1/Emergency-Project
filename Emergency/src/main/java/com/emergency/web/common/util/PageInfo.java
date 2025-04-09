package com.emergency.web.common.util;

import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.common.util
* @fileName        : Pagination.java
* @author          : KHK
* @date            : 2025.04.04
* @description     : 페이지네이션 클래스
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.04.04        KHK       		   최초 생성
 */

@Getter
public class Pagination {
	
	private int totalRecordCnt;
	private int totalPageCnt;
	private int startPage;
	private int endPage;
	private int limitStart;
	private boolean prev;
	private boolean next;
	
}
