package com.emergency.web.exception;

import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.exception
* @fileName        : GlobalException.java
* @author          : KHK
* @date            : 2024.11.11
* @description     : 커스텀 예외처리
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.11.11        KHK                최초 생성
 */

@Getter
public class GlobalException extends RuntimeException {
	
	private String errorCd;
	
	public GlobalException(String message) {
		super(message);
	}
	
	public GlobalException(String message, String errorCd) {
		super(message);
		this.errorCd = errorCd;
	}
}
