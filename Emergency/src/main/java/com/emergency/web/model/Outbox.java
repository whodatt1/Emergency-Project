package com.emergency.web.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : Outbox.java
* @author          : KHK
* @date            : 2025.07.02
* @description     : Outbox VO
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.07.02        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor  // ⭐ MyBatis가 객체 생성할 수 있게 해줌
@AllArgsConstructor
public class Outbox {
	private long id;                 // PK
	private String aggregateType;    // 도메인 객체 유형
	private String aggregateId;      // 도메인 객체 ID
	private String eventType;        // 이벤트 타입
	private String payload;          // 이벤트 관련 JSON 데이터
	private LocalDateTime timestamp; // 이벤트 기록 시간
	private String status;           // 메시지 상태 (READY_TO_PUBLISH, PUBLISHED, 등)
}