package com.emergency.web.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.emergency.web.exception.GlobalException;

/**
 * 
* @packageName     : com.emergency.web.ctrl
* @fileName        : GlobalExceptionHandler.java
* @author          : KHK
* @date            : 2024.12.09
* @description     : 익셉션 처리 컨트롤러
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.12.09        KHK                최초 생성
 */

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<Map<String, String>> handleGlobalException(GlobalException ex) {
		
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("message", ex.getMessage());
		errorResponse.put("errorCd", ex.getErrorCd());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}
