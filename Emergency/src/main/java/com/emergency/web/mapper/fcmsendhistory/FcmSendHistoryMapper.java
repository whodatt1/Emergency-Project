package com.emergency.web.mapper.fcmsendhistory;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
* @packageName     : com.emergency.web.mapper.fcm
* @fileName        : FcmSendHistoryMapper.java
* @author          : KHK
* @date            : 2026.04.19
* @description     : FcmSendHistoryMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2026.04.19        KHK                최초 생성
 */

@Mapper
public interface FcmSendHistoryMapper {
	
	// 중복 방지 다중 INSERT (INSERT IGNORE)
	int insertHistoryIgnoreBulk(@Param("eventId") String eventId, @Param("executionId") String executionId, @Param("tokens") List<String> tokens);
    // 특정 이벤트에 대해 이미 Insert 된 토큰들만 조회
    List<String> getInsertedTokens(@Param("eventId") String eventId, @Param("executionId") String executionId);
    
}
