package com.emergency.web.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : FcmSendHistory.java
* @author          : KHK
* @date            : 2026.04.19
* @description     : FcmSendHistory VO ( FcmSendHistory 저장 )
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2026.04.19        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor  // ⭐ MyBatis가 객체 생성할 수 있게 해줌
@AllArgsConstructor
@ToString
public class FcmSendHistory {
	
	private Long id;
	private String eventId;
	private String fcmToken;
	private LocalDateTime sendTime;
	
}
