package com.emergency.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : BJD.java
* @author          : KHK
* @date            : 2025.03.19
* @description     : BJD VO ( 법정동 )
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.19        KHK                최초 생성
 */

@Getter
@Builder
@NoArgsConstructor  // ⭐ MyBatis가 객체 생성할 수 있게 해줌
@AllArgsConstructor
public class BJD {
	
   private String bjdCd;
   private String bjdNm;
   private String useOrNot;
   
}
