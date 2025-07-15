package com.emergency.web.mapper.outbox;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.Fcm;
import com.emergency.web.model.Outbox;

/**
 * 
* @packageName     : com.emergency.web.mapper.outbox
* @fileName        : OutboxMapper.java
* @author          : KHK
* @date            : 2025.07.02
* @description     : OutboxMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.07.02        KHK                최초 생성
 */

@Mapper
public interface OutboxMapper {
	int insertOutbox(Outbox outbox);
	int updateOutboxStatus(Outbox outbox);
	int staleEmgcRltmOutboxCleaner();
}
