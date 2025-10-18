package com.emergency.web.event;


import org.springframework.batch.item.Chunk;

import com.emergency.web.model.EmgcRltm;

import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.event
* @fileName        : EmgcRltmBatchEvent.java
* @author          : KHK
* @date            : 2025.07.02
* @description     : EmgcRltm 이벤트 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.07.02        KHK                최초 생성
 */

@Getter
public class EmgcRltmBatchEvent<T extends EmgcRltm> {
	private final Chunk<T> emgcRltmItems;
	private final String batchId;
	
	public EmgcRltmBatchEvent(Chunk<T> emgcRltmItems, String batchId) {
		this.emgcRltmItems = emgcRltmItems;
		this.batchId = batchId;
	}
}
