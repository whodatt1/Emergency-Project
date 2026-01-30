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
		
		boolean allSuccess = true;
		
		try {
			Map<String, String> param = objectMapper.readValue(jsonPayload, new TypeReference<Map<String, String>>() {});
			String batchId = param.get("batchId");
            String hpId = param.get("hpId");
            String dutyName = param.get("dutyName");
            
            //String currentStatus = outboxMapper.selectOutboxStatus(batchId, hpId);
			
			// 이미 'FINISHED'라면? (이전에 처리했는데 Kafka가 또 보낸 경우)
//			if ("FINISHED".equals(currentStatus)) {
//				log.info("이미 처리된 메시지입니다. (Skip) - batchId: {}, hpId: {}", batchId, hpId);
//				return;
//			}
            
            List<Fcm> fcmList = fcmMapper.getFcmListWithHpId(hpId);
            List<String> tokens = fcmList.stream()
					                     .map(Fcm::getFcmToken)
					                     .toList();
            
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
				} catch (Exception e) { // 여기 익셉션처리 수정해야함
					//allSuccess = false;
					//log.error("FCM 전송 실패 - token: {}, 에러: {}", fcm.getFcmToken(), e.getMessage(), e);
					log.error("FCM 멀티캐스트 전송 실패 - 토큰 수: {}, 에러: {}", chunk.size(), e.getMessage(), e);
					throw new RuntimeException("FCM 멀티캐스트 전송 실패"); // 에러 핸드러가 처리
				} 
			}
            
            Outbox outbox = Outbox.builder()
					  .batchId(batchId)
					  .aggregateId(hpId)
					  .status(allSuccess ? "FINISHED" : "FAILED")
					  .build();
            
            int res = outboxMapper.updateOutboxStatus(outbox);
			 
			if (res > 0) {
				log.info("Outbox 상태 업데이트 완료 - batchId: {}, hpId: {}, status: {}", 
				         batchId, hpId, allSuccess ? "FINISHED" : "FAILED");
			} else {
				log.warn("Outbox 상태 업데이트 실패 - 영향받은 row 없음 - batchId: {}, hpId: {}", 
						 batchId, hpId);
			}
		} catch (Exception e) {
			log.error("KafkaConsumer 처리 중 오류 발생 - payload: {}, 에러: {}", jsonPayload, e.getMessage(), e);
			throw new RuntimeException("KafkaConsumer 처리 중 오류 발생"); // 에러 핸드러가 처리
		}
	}
	
}
