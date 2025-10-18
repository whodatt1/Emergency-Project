package com.emergency.web.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import com.emergency.web.model.EmgcBsIf;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.writer
* @fileName        : EmgcBsIfItemWriter.java
* @author          : KHK
* @date            : 2025.03.05
* @description     : 배치 작업에서 읽어온 데이터를 저장 및 업데이트
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.05        KHK                최초 생성
 */

@RequiredArgsConstructor
public class EmgcBsIfItemWriter<T extends EmgcBsIf> implements ItemWriter<EmgcBsIf> {
	
	private final JdbcBatchItemWriter<EmgcBsIf> jdbcBatchItemWriter;

	@Override
	public void write(Chunk<? extends EmgcBsIf> chunk) throws Exception {
		jdbcBatchItemWriter.write(chunk);
	}
	
}
