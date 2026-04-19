package com.emergency.web.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.emergency.web.mapper.fcm.FcmMapper;
import com.emergency.web.mapper.fcmsendhistory.FcmSendHistoryMapper;
import com.emergency.web.model.Fcm;
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
	// private final OutboxMapper outboxMapper; // Consumer는 더이상 DB 상태를 건들지 않음
	private final FcmMapper fcmMapper;
	private final FcmService fcmService;
	private final KafkaTemplate<String, Object> kafkaTemplate; // 작업 분배 및 재시도를 위해 주입
	private final FcmSendHistoryMapper fcmSendHistoryMapper;
	
    // 메인 토픽 컨슈머 : 알림 대상을 찾아 청크 단위 작업으로 쪼개어 던짐
	@KafkaListener(topics = "EmgcRltmEvent", groupId = "emgc-rltm-service-group")
	public void EmgcRltmConsume(String jsonPayload) {
		
		try {
			Map<String, String> param = objectMapper.readValue(jsonPayload, new TypeReference<Map<String, String>>() {});
            String hpId = (String) param.get("hpId");
            String dutyName = (String) param.get("dutyName");
            String batchId = param.get("batchId");
            
            List<Fcm> fcmList = fcmMapper.getFcmListWithHpId(hpId);
            List<String> tokens = fcmList.stream()
                                         .map(Fcm::getFcmToken)
                                         .toList();
            
            if (tokens.isEmpty()) {
                log.info("전송할 FCM 토큰이 없습니다. - hpId: {}", hpId);
                return;
            }
            
            // FCM 발송 (Chunk 단위 분할)
            int chunkSize = 500;
            int chunkIndex = 0;
            for (int i = 0; i < tokens.size(); i += chunkSize) {
                List<String> chunk = tokens.subList(i, Math.min(i + chunkSize, tokens.size()));
                
                // 중복 발송을 방지하기 위한 고유 식별자 생성
                String eventId = batchId + "_" + hpId + "_chunk" + (chunkIndex++);
                
                Map<String, Object> taskMessage = new HashMap<>();
                taskMessage.put("hpId", hpId);
                taskMessage.put("dutyName", dutyName);
                taskMessage.put("tokens", chunk);
                taskMessage.put("retryCount", 0);
                taskMessage.put("eventId", eventId); // DB 중복 체크용 키
                
                kafkaTemplate.send("FcmSendTask", hpId, objectMapper.writeValueAsString(taskMessage));
            }
            log.info("[DISPATCHER] hpId: {} 알림 대상 {}명 -> 작업 큐 분배 완료", hpId, tokens.size());
		} catch (Exception e) {
			log.error("메인 Dispatcher 시스템 에러", e);
            // 예외를 던져야 KafkaConfig에 설정해둔 DefaultErrorHandler가 
            // 3초 대기 후 이 리스너를 다시 호출
            throw new RuntimeException("Dispatcher Error", e);
		}
	}
	
    // 작업 토픽 컨슈머: 실제 FCM을 발송하고, 실패 시 재시도 토픽 활용
    @KafkaListener(topics = {"FcmSendTask", "FcmSendTask-Retry"}, groupId = "fcm-worker-group", concurrency = "3")
    public void processFcmTask(String jsonPayload) {
        try {
            Map<String, Object> param = objectMapper.readValue(jsonPayload, new TypeReference<Map<String, Object>>() {});
            String hpId = (String) param.get("hpId");
            String dutyName = (String) param.get("dutyName");
            List<String> snapshotTokens = (List<String>) param.get("tokens");
            String eventId = (String) param.get("eventId");
            int retryCount = (Integer) param.get("retryCount");
            
            if (retryCount > 0) {
                log.info("[WORKER] 재시도 {}회차 발송 시작 - 대상: {}명", retryCount, snapshotTokens.size());
                Thread.sleep(3000); // 카프카에서 즉시 소비하는 것을 막기 위해 약간의 백오프
            }
            
            // 이번 실행을 구별하기 위한 고유 ID 생성
            String executionId = UUID.randomUUID().toString();
            
            // 중복 발송 방지 로직
            // DB 이력 테이블에 INSERT IGNORE 일괄 시도 (중복된 키 무시)
            // - 최초 실행 Worker: DB에 정상 Insert 되며 execution_id가 자기 것으로 기록됨.
            // - 중복 재시도 Worker: DB의 Unique Key(eventId, token) 제약조건에 막혀 무시됨 (기존 execution_id 유지)
            fcmSendHistoryMapper.insertHistoryIgnoreBulk(eventId, executionId, snapshotTokens);
            
            List<String> validTokensToSend = fcmSendHistoryMapper.getInsertedTokens(eventId, executionId);

            // 보낼 토큰이 없으면 이미 모두 처리된 중복 메시지이므로 즉시 종료
            if (validTokensToSend.isEmpty()) {
                log.info("[WORKER] 발송할 유효한 토큰이 없습니다. (모두 중복 처리됨) - eventId: {}", eventId);
                return;
            }
            
            // Insert 완료된 유효 토큰만 다시 조회

            // FcmService는 내부적으로 3번 로컬 재시도를 하고, 최종 실패한 건들만 리스트로 반환
            List<String> failedTokens = fcmService.sendMulticastNotification(
                    dutyName, dutyName + "의 정보가 변경되었습니다.", hpId, validTokensToSend
            );

            // 최종 실패한 토큰이 존재한다면? 재시도 토픽으로 넘김
            if (!failedTokens.isEmpty()) {
                if (retryCount < 3) { // 시스템 전체 최대 3회(혹은 원하는 만큼) 재시도 허용
                    param.put("tokens", failedTokens);
                    param.put("retryCount", retryCount + 1);
                    
                    // 실패한 녀석들만 모아서 Retry 토픽으로 전송
                    kafkaTemplate.send("FcmSendTask-Retry", hpId, objectMapper.writeValueAsString(param));
                    log.warn("[WORKER] 발송 실패 토큰 {}건 -> 재시도 큐 이관 (현재 {}회)", failedTokens.size(), retryCount + 1);
                } else {
                    log.error("최대 재시도 초과. 최종 발송 실패 토큰: {}건", failedTokens.size());
                }
            } else {
                if (retryCount > 0) log.info("[WORKER] 재시도 성공");
            }

        } catch (Exception e) {
            log.error("Worker 컨슈머 에러", e);
            throw new RuntimeException("Worker Error", e);
        }
    }
}