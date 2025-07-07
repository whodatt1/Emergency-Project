package com.emergency.web.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.emergency.web.event.EmgcRltmBatchEvent;
import com.emergency.web.kafka.KafkaProducer;
import com.emergency.web.mapper.outbox.OutboxMapper;
import com.emergency.web.model.EmgcRltm;
import com.emergency.web.model.Outbox;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @packageName     : com.emergency.web.handler
* @fileName        : ApplicationEventHandler.java
* @author          : KHK
* @date            : 2025.07.02
* @description     : ApplicationEventHandler 이벤트 핸들링 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.07.02        KHK                최초 생성
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationEventHandler {
	
	private final OutboxMapper outboxMapper;
	private final ObjectMapper objectMapper;
	private final KafkaProducer kafkaProducer;
	
	// OutBox를 채택한 이유 after_commit 이벤트 리스너가 동작하기 전이나 도중 문제가 생겨 kafka에 메시지 발행에 문제가 생길경우
	// outbox 테이블을 재조회하여 알림을 전달할 수 있다.
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void handleEmgcRltmBatchEventBefore(EmgcRltmBatchEvent<EmgcRltm> event) {
		List<EmgcRltm> emgcRltmList = event.getEmgcRltmItems();
		// 여기서 db outbox 테이블에 저장
		for (EmgcRltm emgcRltm : emgcRltmList) {
			Map<String, String> payload = new HashMap<>();
			payload.put("hpId", emgcRltm.getHpId());
			payload.put("dutyName", emgcRltm.getDutyName());
			
			try {
				Outbox outbox = Outbox.builder()
									  .batchId(event.getBatchId())
									  .aggregateType("EmgcRltm")
									  .aggregateId(emgcRltm.getHpId())
									  .eventType("EmgcRltmUpdate")
									  .payload(objectMapper.writeValueAsString(payload))
									  .status("READY_TO_PUBLISH")
									  .build();
				
				int res = outboxMapper.insertOutbox(outbox);
				
				if (res < 1) {
			        log.warn("Outbox 저장 실패 (영향 받은 row 없음): hpId={}", emgcRltm.getHpId());
			    }
			} catch (JsonProcessingException e) {
				log.error("직렬화 실패: {}", emgcRltm.getHpId(), e);
			}
		}
	}
	
	@Async // @Async를 사용하면 별도의 쓰레드에서 처리되므로, 메인 트랜잭션 흐름을 빠르게 마무리 가능
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleEmgcRltmBatchEventAfter(EmgcRltmBatchEvent<EmgcRltm> event) {
		// 여기서 db outbox 테이블에 저장된 객체를 불러와 kafka 컨슈머 서비스 메서드 호출
		kafkaProducer.publishEmgcRltmEvent(event);
	}
}
