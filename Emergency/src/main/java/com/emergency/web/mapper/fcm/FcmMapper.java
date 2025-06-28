package com.emergency.web.mapper.fcm;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.Fcm;

/**
 * 
* @packageName     : com.emergency.web.mapper.fcm
* @fileName        : FcmMapper.java
* @author          : KHK
* @date            : 2025.06.25
* @description     : FcmMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.25        KHK                최초 생성
 */

@Mapper
public interface FcmMapper {
	
	int insertFcmInfo(Fcm fcm);
	Fcm getFcmInfoByUserIdAndFcmToken(Fcm fcm);
	// 단일 로그인만 가능하므로 UserId만 끌고와서 삭제
	int deleteFcmInfoByUserId(String userId);
}
