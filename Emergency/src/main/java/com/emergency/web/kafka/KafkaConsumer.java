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
            
            List<Fcm> fcmList = fcmMapper.getFcmListWithHpId(hpId);
            
            for (Fcm fcm : fcmList) {
            	try {
            		fcmService.sendNotification(dutyName, dutyName + "의 정보가 변경되었습니다.", hpId, fcm.getFcmToken());
				} catch (Exception e) {
					allSuccess = false;
					log.error("FCM 전송 실패 - token: {}, 에러: {}", fcm.getFcmToken(), e.getMessage(), e);
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
		}
	}
	
}
