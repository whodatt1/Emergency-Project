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
		
		// 여러 비동기 전송 작업(Future)들을 추적하기 위해 담아두는 리스트
		List<CompletableFuture<SendResult<String, Object>>> futures = new ArrayList<>();
	    
		// 멀티스레드(비동기 콜백) 환경에서 안전하게 결과를 수집하기 위한 Thread-Safe 큐
		Queue<String> successIds = new ConcurrentLinkedQueue<>();
	    Queue<String> failedIds = new ConcurrentLinkedQueue<>();
		
		for (EmgcRltm emgcRltm : event.getEmgcRltmItems()) {
			try {
				Map<String, String> message = new HashMap<>();
				message.put("batchId", event.getBatchId());
				message.put("hpId", emgcRltm.getHpId());
				message.put("dutyName", emgcRltm.getDutyName());
				
				String jsonPayload = objectMapper.writeValueAsString(message);
				
				// ================================================================
                // [파티션 순서 보장] Key값(hpId)을 두 번째 인자로 전달합니다.
                // 동일한 병원(hpId)의 알림은 카프카의 동일한 파티션으로 인입되어 순서가 보장됩니다.
                // ================================================================
				CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
						"EmgcRltmEvent",
						emgcRltm.getHpId(),
						jsonPayload);
				
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
				
				// 추후 모든 작업의 완료 시점을 알기 위해 리스트에 적재
				futures.add(future);
				
			} catch (Exception e) {
				log.error("Kafka 메시지 직렬화 실패 - batchId: {}, hpId: {}", 
				          event.getBatchId(), emgcRltm.getHpId(), e);
				failedIds.add(emgcRltm.getHpId());
			}
		}
		
		// ================================================================
        // [논블로킹 벌크 업데이트] 
        // 메인 스레드(배치)는 대기(join)하지 않고 바로 종료되며,
        // 모든 카프카 전송이 끝난 후 별도의 백그라운드 스레드가 DB 상태값을 일괄 업데이트합니다.
        // ================================================================
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
			.whenCompleteAsync((v, th) -> {
				
				// 성공건 일괄 처리
				if (!successIds.isEmpty()) {
					List<String> ids = new ArrayList<>(successIds);
					outboxMapper.updateOutboxStatusBulk(event.getBatchId(), ids, "COMPLETED");
				}
				
				if (!failedIds.isEmpty()) {
					List<String> ids = new ArrayList<>(failedIds);
					outboxMapper.updateOutboxStatusBulk(event.getBatchId(), ids, "FAIL");
				}
				
		});
	}

	public void retryPublish(Outbox outbox) {
		try {
            // 1. 메시지 재조립 (Outbox에 저장해둔 데이터를 기반으로)
            Map<String, String> message = new HashMap<>();
            message.put("batchId", outbox.getBatchId());
            message.put("hpId", outbox.getAggregateId()); // Outbox에서는 hpId가 aggregateId로 매핑됨
            message.put("dutyName", outbox.getDutyName()); 

            String jsonPayload = objectMapper.writeValueAsString(message);

            // 2. 비동기 전송
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                    "EmgcRltmEvent",
                    outbox.getAggregateId(),
                    jsonPayload);

            // 결과 콜백: 성공 시 즉시 완료 처리
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("재발송 성공 - hpId: {}", outbox.getAggregateId());
                    // 단건 업데이트 호출
                    outboxMapper.updateOutboxStatus(
                        Outbox.builder()
                            .batchId(outbox.getBatchId())
                            .aggregateId(outbox.getAggregateId())
                            .status("COMPLETED")
                            .build()
                    );
                } else {
                    log.error("재발송 실패 - hpId: {}", outbox.getAggregateId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("메시지 생성 중 에러 - hpId: {}", outbox.getAggregateId(), e);
        }
	}
	
}
