package com.emergency.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : Fcm.java
* @author          : KHK
* @date            : 2025.06.25
* @description     : Fcm VO ( FcmToken 저장 )
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.25        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor  // ⭐ MyBatis가 객체 생성할 수 있게 해줌
@AllArgsConstructor
@ToString
public class Fcm {
	
   private String userId;
   private String fcmToken;
   
}
