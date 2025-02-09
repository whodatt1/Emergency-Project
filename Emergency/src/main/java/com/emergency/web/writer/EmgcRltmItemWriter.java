package com.emergency.web.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : EmgcRltmItemWriter.java
* @author          : KHK
* @date            : 2025.02.09
* @description     : 배치 작업에서 읽어온 데이터를 다른 형태로 변환
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.02.09        KHK                최초 생성
 */

public class EmgcRltmItemWriter<T> implements ItemWriter<List<T>> {
	
	// 여러개를 모아 한번에 저장하기 위해 사용
	private final JdbcBatchItemWriter<T> jdbcBatchItemWriter;
	
	public EmgcRltmItemWriter(JdbcBatchItemWriter<T> jdbcBatchItemWriter) {
		this.jdbcBatchItemWriter = jdbcBatchItemWriter;
	}

	@Override
	public void write(Chunk<? extends List<T>> chunk) throws Exception {
		
	}
	
}
