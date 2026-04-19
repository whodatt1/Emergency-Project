package com.emergency.web.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import com.emergency.web.event.EmgcRltmBatchEvent;
import com.emergency.web.mapper.emgc.EmgcMapper;
import com.emergency.web.model.EmgcRltm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @packageName     : com.emergency.web.writer
* @fileName        : EmgcRltmItemWriter.java
* @author          : KHK
* @date            : 2025.02.09
* @description     : 배치 작업에서 읽어온 데이터를 저장 및 업데이트
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.02.09        KHK                최초 생성
 */

@Slf4j
@RequiredArgsConstructor
public class EmgcRltmItemWriter<T extends EmgcRltm> implements ItemWriter<EmgcRltm> {
	
	@Value("#{jobParameters['batchId']}")
    private String batchId;
	
	private final JdbcBatchItemWriter<EmgcRltm> jdbcBatchItemWriter;
	private final EmgcMapper emgcMapper;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void write(Chunk<? extends EmgcRltm> chunk) throws Exception {
		
	    List<String> hpIds = chunk.getItems().stream()
	    						  .map(EmgcRltm::getHpId)
	    						  .collect(Collectors.toList());
	    
	    // IN 쿼리로 한번에 가져옴
	    List<Map<String, Object>> existingDatesList = emgcMapper.getEmgcRltmUpdDatesByHpIds(hpIds);
	    
	    // Map으로 평탄화
	    Map<String, String> existingUpdDateMap = existingDatesList.stream()
	    		.collect(Collectors.toMap(
	    				map -> (String) map.get("hpId"), 
	    				map -> (String) map.get("updDate")
	    		));
	    
	    // 실제로 저장 혹은 업데이트할 대상만 걸러내기
	    List<EmgcRltm> targetToWrite = new ArrayList<>();
	    
	    for (EmgcRltm incomingItem : chunk) {
			String existingUpdDate = existingUpdDateMap.get(incomingItem.getHpId());
			
			if (existingUpdDate == null || !existingUpdDate.equals(incomingItem.getUpdDate())) {
				targetToWrite.add(incomingItem);
			}
		}
	    
//		jdbcBatchItemWriter.write(chunk); // 테스트용
//	    applicationEventPublisher.publishEvent(new EmgcRltmBatchEvent<>(chunk, batchId)); // 테스트용
	    
	    if (!targetToWrite.isEmpty()) {
	    	Chunk<EmgcRltm> targetChunk = new Chunk<>(targetToWrite);
            jdbcBatchItemWriter.write(targetChunk);
            applicationEventPublisher.publishEvent(new EmgcRltmBatchEvent<>(targetChunk, batchId));
	    }
    	
	}
	
}
