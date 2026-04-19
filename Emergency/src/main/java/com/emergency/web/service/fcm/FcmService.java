package com.emergency.web.service.fcm;

import java.util.ArrayList;
import java.util.List;
import java.util.spi.CurrencyNameProvider;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 
* @packageName     : com.emergency.web.service.fcm
* @fileName        : FcmService.java
* @author          : KHK
* @date            : 2025.06.28
* @description     : FCM Service 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.28        KHK                최초 생성
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {
	
	private final FirebaseMessaging firebaseMessaging;
	
	public void sendNotification(String title, String body, String payload, String fcmToken) {
		log.info("Attempting to send Notification (title: {}, body: {}, fcmToken: {})", title, body, fcmToken);
		send(createMessage(title, body, payload, fcmToken));
	}
	
	public List<String> sendMulticastNotification(String title, String body, String payload, List<String> chunk) {
		log.info("FCM 발송 시도 - 대상: {}명", chunk.size());
		
		List<String> failedTokensToRetry = new ArrayList<>(); 
		MulticastMessage multicastMessage = createMulticastMessage(title, body, payload, chunk);
		
		try {
			BatchResponse response = firebaseMessaging.sendEachForMulticast(multicastMessage);
			
			if (response.getFailureCount() > 0) {
				for (int j = 0; j < response.getResponses().size(); j++) {
					if (!response.getResponses().get(j).isSuccessful()) {
						String errorCode = response.getResponses().get(j).getException().getMessagingErrorCode().name();
						String token = chunk.get(j);
						
						if ("UNREGISTERED".equals(errorCode) || "INVALID_ARGUMENT".equals(errorCode)) {
							log.warn("유효하지 않은 토큰 감지 (continue): {}", token);
							continue;
						} else {
							log.error("FCM 일시적 발송 실패 (카프카 재시도 대상): {}", token);
							failedTokensToRetry.add(token);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("FCM 서버 통신 실패 (전체 카프카 재시도 대상)", e);
			failedTokensToRetry.addAll(chunk);
		}
		
        // 실패한 건들을 리턴
		return failedTokensToRetry; 
	}
	
	private void send(Message message) {
		try {
			String response = firebaseMessaging.send(message);
			log.info("Successfully send Notification: {}", response);
		} catch (Exception e) {
			log.info("Fail to send Notification: {}", e.getMessage());
		}
	}
	
	private MulticastMessage createMulticastMessage(String title, String body, String payload, List<String> tokens) {
		return MulticastMessage.builder()
				.putData("title", title)
				.putData("body", body)
				.putData("payload", payload)
				.addAllTokens(tokens)
				.build();
	}
	
	private Message createMessage(String title, String body, String payload, String fcmToken) {
		return Message.builder()
				.putData("title", title)
				.putData("body", body)
				.putData("payload", payload)
					.setToken(fcmToken)
					.build();
					
	}
	
		
}
