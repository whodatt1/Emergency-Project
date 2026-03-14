package com.emergency.web.kafka;

import java.util.List;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.emergency.web.mapper.fcm.FcmMapper;
import com.emergency.web.mapper.outbox.OutboxMapper;
import com.emergency.web.model.Fcm;
import com.emergency.web.model.Outbox;
import com.emergency.web.service.fcm.FcmService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	
	private final ObjectMapper objectMapper;
	private final OutboxMapper outboxMapper;
	private final FcmMapper fcmMapper;
	private final FcmService fcmService;
	
    // concurrency = "3": 파티션이 3개 이상일 경우 3개의 스레드가 병렬 처리 (속도 향상)
	@KafkaListener(topics = "EmgcRltmEvent", groupId = "emgc-rltm-service-group", concurrency = "3")
	public void EmgcRltmConsume(String jsonPayload) {
		
		String batchId = null;
		String hpId = null;
		
		try {
			Map<String, String> param = objectMapper.readValue(jsonPayload, new TypeReference<Map<String, String>>() {});
			batchId = param.get("batchId");
            hpId = param.get("hpId");
            String dutyName = param.get("dutyName");
            
            
            String currentStatus = outboxMapper.selectOutboxStatus(batchId, hpId);
            if ("FINISHED".equals(currentStatus)) {
                log.info("이미 처리된 건입니다(재발송 안함). status: {} - batchId: {}", currentStatus, batchId);
                return; 
            }
            
            List<Fcm> fcmList = fcmMapper.getFcmListWithHpId(hpId);
            List<String> tokens = fcmList.stream()
                                         .map(Fcm::getFcmToken)
                                         .toList();
            
            if (tokens.isEmpty()) {
                log.warn("전송할 FCM 토큰이 없습니다. - batchId: {}, hpId: {}", batchId, hpId);
                updateOutboxStatus(batchId, hpId, "FINISHED");
                return;
            }
            
            // FCM 발송 (Chunk 단위 분할)
            int chunkSize = 100;
            for (int i = 0; i < tokens.size(); i += chunkSize) {
                List<String> chunk = tokens.subList(i, Math.min(i + chunkSize, tokens.size()));
                
                // 수동 재시도(while, sleep) 제거 -> 에러 발생 시 밖으로 던짐!
                fcmService.sendMulticastNotification(
                        dutyName, 
                        dutyName + "의 정보가 변경되었습니다.", 
                        hpId, 
                        chunk
                );
            }
            
            // 모든 청크가 에러 없이 발송 완료되면 상태를 FINISHED로 업데이트
            updateOutboxStatus(batchId, hpId, "FINISHED");
            log.info("[RESULT] FCM 발송 완료 (batchId: {})", batchId);

		} catch (Exception e) {
            // 에러를 먹지 않고 RuntimeException으로 감싸서 던집니다!
            log.error("KafkaConsumer FCM 발송 중 에러 발생. 스프링 카프카가 재시도를 수행합니다. - payload: {}", jsonPayload, e);
            
            // 예외를 던져야 KafkaConfig에 설정해둔 DefaultErrorHandler가 
            // 3초 대기 후 이 리스너를 다시 호출
            throw new RuntimeException("FCM 발송 실패에 따른 카프카 재시도 트리거", e);
		}
	}
	
	// 코드 중복 제거
	private void updateOutboxStatus(String batchId, String aggregateId, String status) {
		Outbox outbox = Outbox.builder()
				  .batchId(batchId)
				  .aggregateId(aggregateId)
				  .status(status)
				  .build();
		outboxMapper.updateOutboxStatus(outbox);
	}
	
}