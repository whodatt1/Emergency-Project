package com.emergency.web.service.fcm;

import java.util.List;

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
	
	public void sendMulticastNotification(String title, String body, String payload, List<String> chunk) {
		log.info("Attempting to send Notification (title: {}, body: {}, tokens: {})", title, body, chunk);
		sendMulticast(createMulticastMessage(title, body, payload, chunk));
	}
	
	private void send(Message message) {
		try {
			String response = firebaseMessaging.send(message);
			log.info("Successfully send Notification: {}", response);
		} catch (Exception e) {
			log.info("Fail to send Notification: {}", e.getMessage());
		}
	}
	
	private void sendMulticast(MulticastMessage mutlicastMessage) {
		try {
			BatchResponse response = firebaseMessaging.sendEachForMulticast(mutlicastMessage);
			log.info("Multicast result - success: {}, failure: {}",
				    response.getSuccessCount(), response.getFailureCount());
			
			if (response.getFailureCount() > 0) {
				response.getResponses().stream()
						.filter(r -> !r.isSuccessful())
						.forEach(r -> log.warn("Multicast Failed ! : {}", r.getException().getMessage()));
			}
		} catch (Exception e) {
			log.error("Fail to send Multicast Notification: {}", e.getMessage(), e);
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
