package com.emergency.web.kafka;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.emergency.web.event.EmgcRltmBatchEvent;
import com.emergency.web.mapper.outbox.OutboxMapper;
import com.emergency.web.model.EmgcRltm;
import com.emergency.web.model.Outbox;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @packageName     : com.emergency.web.kafka
* @fileName        : KafkaProducer.java
* @author          : KHK
* @date            : 2025.07.07
* @description     : KafkaProducer 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.07.07        KHK                최초 생성
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
	
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final OutboxMapper outboxMapper;
	
	public void publishEmgcRltmEvent(EmgcRltmBatchEvent<EmgcRltm> event) {
		
		for (EmgcRltm emgcRltm : event.getEmgcRltmItems()) {
			Map<String, String> message = new HashMap<>();
			message.put("batchId", event.getBatchId());
			message.put("hpId", emgcRltm.getHpId());
			message.put("dutyName", emgcRltm.getDutyName());
			
			Outbox outbox = Outbox.builder()
								  .batchId(event.getBatchId())
								  .aggregateId(emgcRltm.getHpId())
								  .status("PROCESSING")
								  .build();
			
			try {
				String jsonPayload = objectMapper.writeValueAsString(message);
				kafkaTemplate.send("EmgcRltmEvent", jsonPayload)
							 .whenComplete((result ,ex) -> {
								 // result => kafka 전송이 성공했을때의 결과객체
								 // ex => 실패했을 때 예외 예외가 없다면 null 반환
								 if (ex == null) {
									 int res = outboxMapper.updateOutboxStatus(outbox);
									 
									 if (res > 0) {
										    log.info("Outbox 상태 업데이트 완료 - batchId: {}, hpId: {}, status: PROCESSING", 
										             event.getBatchId(), emgcRltm.getHpId());
									 } else {
										 log.warn("Outbox 상태 업데이트 실패 - 영향받은 row 없음 - batchId: {}, hpId: {}", 
									             event.getBatchId(), emgcRltm.getHpId());
									 }
								 } else {
									 log.error("Kafka 메시지 전송 실패 - batchId: {}, hpId: {}, 에러: {}",
						                      event.getBatchId(), emgcRltm.getHpId(), ex.getMessage(), ex);
								 }
							 });
			} catch (Exception e) {
				log.error("Kafka 메시지 직렬화 실패 - batchId: {}, hpId: {}", 
				          event.getBatchId(), emgcRltm.getHpId(), e);
			}
		}
	}
	
}
