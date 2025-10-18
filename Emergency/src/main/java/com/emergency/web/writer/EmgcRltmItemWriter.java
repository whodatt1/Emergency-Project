package com.emergency.web.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import com.emergency.web.event.EmgcRltmBatchEvent;
import com.emergency.web.kafka.KafkaProducer;
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
		
//		List<T> targetToWrite = new ArrayList<>();
//		
//		for (T incomingItem : flattenedItems) {
//			String updDate = emgcMapper.getEmgcRltmUpdDateByHpId(incomingItem.getHpId());
//			
//			if (updDate == null || !updDate.equals(incomingItem.getUpdDate())) {
//				targetToWrite.add(incomingItem);
//			}
//		}
		
		//jdbcBatchItemWriter.write(new Chunk<>(targetToWrite));
		jdbcBatchItemWriter.write(chunk); // 테스트용
		
    	//applicationEventPublisher.publishEvent(new EmgcRltmBatchEvent<>(targetToWrite, batchId));
    	applicationEventPublisher.publishEvent(new EmgcRltmBatchEvent<>(chunk, batchId)); // 테스트용
	}
	
}
