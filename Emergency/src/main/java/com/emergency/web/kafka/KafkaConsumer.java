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
		
		int successCount = 0;
		int failCount = 0;
		
		try {
			Map<String, String> param = objectMapper.readValue(jsonPayload, new TypeReference<Map<String, String>>() {});
			batchId = param.get("batchId");
            hpId = param.get("hpId");
            String dutyName = param.get("dutyName");
            
            String currentStatus = outboxMapper.selectOutboxStatus(batchId, hpId);
			
            if ("FINISHED".equals(currentStatus) || "FAILED".equals(currentStatus)) {
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
            
            int chunkSize = 100;
            for (int i = 0; i < tokens.size(); i += chunkSize) {
            	List<String> chunk = tokens.subList(i, Math.min(i + chunkSize, tokens.size()));
            	try {
            		fcmService.sendMulticastNotification(
                            dutyName, 
                            dutyName + "의 정보가 변경되었습니다.", 
                            hpId, 
                            chunk
                    );
            		successCount++;
				} catch (Exception e) {
					failCount++;
					log.error("FCM 멀티캐스트 전송 실패 - 토큰 수: {}, 에러: {}", chunk.size(), e.getMessage());
                    // 여기서 throw 하지 않으므로 루프는 계속 진행됨 (의도된 동작)
				} 
			}
            
            String finalStatus;
            if (successCount > 0) {
                // 1개라도 성공했으면 처리가 끝난 것으로 간주 (일부 실패했어도 재발송 안 함)
                finalStatus = "FINISHED";
            } else {
                // 성공이 0개라면 (즉, 모든 시도가 에러났거나 토큰이 없었거나) -> 완전 실패
                finalStatus = "FAILED";
            }
            
            updateOutboxStatus(batchId, hpId, finalStatus);
			
            if (failCount > 0) {
                log.warn("FCM 발송 완료했으나 일부 실패 존재 - 상태: {}, 성공청크: {}, 실패청크: {}", 
                         finalStatus, successCount, failCount);
           }

		} catch (Exception e) {
			// [보완 2] 예외 발생 시 DB 상태를 'FAILED'로 변경해야 함
			log.error("KafkaConsumer 처리 중 치명적 오류 발생 (재시도 중단) - payload: {}, 에러: {}", jsonPayload, e.getMessage(), e);
			
			// JSON 파싱조차 실패해서 batchId가 null일 수도 있으니 체크
			if (batchId != null && hpId != null) {
				try {
					updateOutboxStatus(batchId, hpId, "FAILED");
				} catch (Exception dbEx) {
					log.error("DB 상태 업데이트 중 2차 에러 발생", dbEx);
				}
			}
			
			// [핵심 주석]
			// Kafka의 무한 재시도(Redelivery)를 방지하기 위해 예외를 상위로 던지지 않음(Swallow).
			// 예외가 발생하더라도 이 메시지는 '처리됨(Committed)'으로 간주하고 넘어감.
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