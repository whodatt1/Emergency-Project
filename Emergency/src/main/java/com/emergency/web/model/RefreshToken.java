package com.emergency.web.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : RefreshToken.java
* @author          : KHK
* @date            : 2024.10.20
* @description     : RefreshToken VO
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.20        KHK                최초 생성
 */

@Getter
@Builder
public class RefreshToken {
	
	private long id; // PRIMARY KEY
	private String userId;
	private String token;
	private LocalDateTime expiryDate;
	
}
