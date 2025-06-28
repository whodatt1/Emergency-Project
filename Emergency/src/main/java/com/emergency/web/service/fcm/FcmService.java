package com.emergency.web.service.fcm;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

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
	
	public void sendNotification(String title, String body, String fcmToken) {
		log.info("Attempting to send Notification (title: {}, body: {}, fcmToken: {})", title, body, fcmToken);
		send(createMessage(title, body, fcmToken));
	}
	
	private void send(Message message) {
		try {
			String response = firebaseMessaging.send(message);
			log.info("Successfully send Notification: {}", response);
		} catch (Exception e) {
			log.info("Fail to send Notification: {}", e.getMessage());
		}
	}
	
	private Message createMessage(String title, String body, String fcmToken) {
		return Message.builder()
				.putData("title", title)
				.putData("body", body)
				.setToken(fcmToken)
				.build();
				
	}
}
