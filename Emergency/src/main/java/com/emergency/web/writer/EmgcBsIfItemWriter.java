package com.emergency.web.writer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

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

public class EmgcBsIfItemWriter<T> implements ItemWriter<List<T>> {
	
	private JdbcBatchItemWriter<T> jdbcBatchItemWriter;
	
	public EmgcBsIfItemWriter(JdbcBatchItemWriter<T> jdbcBatchItemWriter) {
		this.jdbcBatchItemWriter = jdbcBatchItemWriter;
	}

	@Override
	public void write(Chunk<? extends List<T>> chunk) throws Exception {
		
		List<T> flattenedItems = chunk.getItems().stream()
                .flatMap(List::stream)  // List<List<T>>를 List<T>로 평탄화
                .collect(Collectors.toList());
		
		String sql = "INSERT INTO tb_emgc_rltm_master "
				+ "( "
				+ " hp_id, duty_name, post_cdn1, post_cdn2, duty_addr, duty_tel, "
				+ " duty_inf, duty_lon, duty_lat, dgid_id_name"
				+ ") "
				+ "VALUES "
				+ "( "
				+ " :hpId, :dutyName, :postCdn1, :postCdn2, :dutyAddr, :dutyTel, "
				+ " :dutyInf, :dutyLon, :dutyLat, :dgidIdName"
				+ ") "
				+ "ON DUPLICATE KEY UPDATE "
				+ " hp_id = :hpId, duty_name = :dutyName, post_cdn1 = :postCdn1, "
				+ " post_cdn2 = :postCdn2, duty_addr = :dutyAddr, duty_tel = :dutyTel, "
				+ " duty_inf = :dutyInf, duty_lon = :dutyLon, duty_lat = :dutyLat, "
				+ " dgid_id_name = :dgidIdName ";

		
		jdbcBatchItemWriter.setSql(sql);
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		jdbcBatchItemWriter.afterPropertiesSet();
		jdbcBatchItemWriter.write(new Chunk<>(flattenedItems));
	}
	
}
