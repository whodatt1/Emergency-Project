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
	
	// 최대 재시도 횟수 및 대기 시간 설정
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 500; // 0.5초 대기
	
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
                
                boolean isSent = false;
                int attempt = 0;
                Exception lastException = null;

                // 실패한 청크만 3번까지 재시도하는 루프
                while (!isSent && attempt < MAX_RETRY_ATTEMPTS) {
                    try {
                        attempt++;
                        fcmService.sendMulticastNotification(
                                dutyName, 
                                dutyName + "의 정보가 변경되었습니다.", 
                                hpId, 
                                chunk
                        );
                        isSent = true; // 성공하면 루프 탈출
                        successCount += chunk.size();
                    } catch (Exception e) {
                        lastException = e;
                        log.warn("FCM 발송 일시적 실패 (시도 {}/{}) - 에러: {}", attempt, MAX_RETRY_ATTEMPTS, e.getMessage());
                        
                        // 재시도 전 잠깐 대기 (Backoff)
                        if (attempt < MAX_RETRY_ATTEMPTS) {
                            try { Thread.sleep(RETRY_DELAY_MS * attempt); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                        }
                    }
                }
                
                // 3번 다 해봤는데도 실패하면?
                if (!isSent) {
                    failCount += chunk.size();
                    log.error("[CHUNK FAIL] 3회 재시도 후에도 최종 실패 - batchId: {}, 토큰수: {}, 에러: {}", batchId, chunk.size(), lastException.getMessage());
                    // 여기서도 throw하지 않고 다음 청크로 넘어갑니다 (부분 실패 허용)
                }
            }
            
            // 5. 최종 상태 업데이트
            // 하나라도 성공했거나, 재시도 끝에 실패했더라도 일단 처리는 끝난 것임.
            String finalStatus = (successCount > 0) ? "FINISHED" : "FAILED";
            updateOutboxStatus(batchId, hpId, finalStatus);
            
            log.info("[RESULT] 처리 완료 - 성공: {}, 실패: {} (batchId: {})", successCount, failCount, batchId);

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