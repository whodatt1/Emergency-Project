package com.emergency.web.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
		
		// CompletableFuture => 비동기로 별도의 Thread에서 작업을 진행한다.
		List<CompletableFuture<SendResult<String, Object>>> futures = new ArrayList<>();
	    // 멀티스레드 환경에서 안전하게 성공한 ID를 담을 큐 (Thread-Safe)
		Queue<String> successIds = new ConcurrentLinkedQueue<>();
	    // 실패한 ID를 담을 큐 (Thread-Safe)
	    Queue<String> failedIds = new ConcurrentLinkedQueue<>();
		
		for (EmgcRltm emgcRltm : event.getEmgcRltmItems()) {
			try {
				Map<String, String> message = new HashMap<>();
				message.put("batchId", event.getBatchId());
				message.put("hpId", emgcRltm.getHpId());
				message.put("dutyName", emgcRltm.getDutyName());
				
				String jsonPayload = objectMapper.writeValueAsString(message);
				
				// 비동기 전송 시작 
				CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("EmgcRltmEvent", jsonPayload);
				
				future.whenComplete((result, ex) -> {
					if (ex == null) {
						// 성공시 성공큐에 저장
						successIds.add(emgcRltm.getHpId());
					} else {
						log.error("Kafka 전송 실패 - batchId: {}, hpId: {}", 
						          event.getBatchId(), emgcRltm.getHpId(), ex);
						failedIds.add(emgcRltm.getHpId());
					}
				});
				
				// 추후에 기다리기 위하여 추가
				futures.add(future);
				
			} catch (Exception e) {
				log.error("Kafka 메시지 직렬화 실패 - batchId: {}, hpId: {}", 
				          event.getBatchId(), emgcRltm.getHpId(), e);
				failedIds.add(emgcRltm.getHpId());
			}
		}
		// 배열의 타입을 명시해서 안전하게 CompletableFuture 타입으로 배열을 만듦
		// 순서대로 수행 후 결과값을 가져오되 예외가 Unchecked Exception으로 발생
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		
		// 성공건 일괄 처리
		if (!successIds.isEmpty()) {
			List<String> ids = new ArrayList<>(successIds);
			outboxMapper.updateOutboxStatusBulk(event.getBatchId(), ids, "PROCESSING");
		}
		
		if (!failedIds.isEmpty()) {
			List<String> ids = new ArrayList<>(failedIds);
			outboxMapper.updateOutboxStatusBulk(event.getBatchId(), ids, "FAIL");
		}
	}
	
}
