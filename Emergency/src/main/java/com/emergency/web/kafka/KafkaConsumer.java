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

/**
 * 
* @packageName     : com.emergency.web.kafka
* @fileName        : KafkaConsumer.java
* @author          : KHK
* @date            : 2025.07.07
* @description     : KafkaConsumer 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.07.07        KHK                최초 생성
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	
	private final ObjectMapper objectMapper;
	private final OutboxMapper outboxMapper;
	private final FcmMapper fcmMapper;
	private final FcmService fcmService;
	
	@KafkaListener(topics = "EmgcRltmEvent", groupId = "emgc-rltm-service-group")
	public void EmgcRltmConsume(String jsonPayload) {
		
		String batchId = null;
		String hpId = null;
		
		// 카운트 변수 추가
		int successCount = 0;
		int failCount = 0;
		
		try {
			Map<String, String> param = objectMapper.readValue(jsonPayload, new TypeReference<Map<String, String>>() {});
			batchId = param.get("batchId");
            hpId = param.get("hpId");
            String dutyName = param.get("dutyName");
            
            String currentStatus = outboxMapper.selectOutboxStatus(batchId, hpId);
			
            if ("FINISHED".equals(currentStatus) || "PARTIAL_FAIL".equals(currentStatus)) {
                log.info("이미 처리된 건입니다(재발송 안함). status: {} - batchId: {}", currentStatus, batchId);
                return; 
            }
            
            List<Fcm> fcmList = fcmMapper.getFcmListWithHpId(hpId);
            List<String> tokens = fcmList.stream()
					                     .map(Fcm::getFcmToken)
					                     .toList();
            
            // 토큰이 없는 경우 예외처리
            if (tokens.isEmpty()) {
                log.warn("전송할 FCM 토큰이 없습니다. - batchId: {}, hpId: {}", batchId, hpId);
                
                Outbox outbox = Outbox.builder()
                        .batchId(batchId)
                        .aggregateId(hpId)
                        .status("FINISHED")
                        .build();
                  
                outboxMapper.updateOutboxStatus(outbox);
                
                return;
            }
            
            int chunkSize = 100;
            for (int i = 0; i < tokens.size(); i += chunkSize) {
            	// 청크 사이즈만큼 리스트 자르기
            	List<String> chunk = tokens.subList(i, Math.min(i + chunkSize, tokens.size()));
            	try {
            		// 기존방식 HTTP 호출을 토큰별로 하게됨 과도한 호출로 인해 배포시 아웃바운드 과부하 발생 우려
            		// fcmService.sendNotification(dutyName, dutyName + "의 정보가 변경되었습니다.", hpId, fcm.getFcmToken());
            		fcmService.sendMulticastNotification(
                            dutyName, 
                            dutyName + "의 정보가 변경되었습니다.", 
                            hpId, 
                            chunk
                    );
            		successCount++;
				} catch (Exception e) { // 여기 익셉션처리 수정해야함
					failCount++;
					log.error("FCM 멀티캐스트 전송 실패 - 토큰 수: {}, 에러: {}", chunk.size(), e.getMessage(), e);
				} 
			}
            
            String finalStatus;
            if (failCount == 0) {
            	finalStatus = "FINISHED";
            } else if (successCount == 0) {
            	finalStatus = "FAILED";
            } else {
            	finalStatus = "PARTIAL_FAIL"; // 일부 성공, 일부 실패
            }
            
            Outbox outbox = Outbox.builder()
					  .batchId(batchId)
					  .aggregateId(hpId)
					  .status(finalStatus)
					  .build();
            
            int res = outboxMapper.updateOutboxStatus(outbox);
			 
            if ("PARTIAL_FAIL".equals(finalStatus)) {
                 log.warn("FCM 부분 실패 발생 - 총 청크: {}, 성공: {}, 실패: {} - batchId: {}", 
                          (successCount + failCount), successCount, failCount, batchId);
            } else if ("FAILED".equals(finalStatus)) {
                 log.error("FCM 전체 실패 발생 - batchId: {}", batchId);
            }
		} catch (Exception e) {
			log.error("KafkaConsumer 처리 중 오류 발생 - payload: {}, 에러: {}", jsonPayload, e.getMessage(), e);
			throw new RuntimeException("KafkaConsumer 처리 중 오류 발생"); // 에러 핸드러가 처리
		}
	}
	
}
