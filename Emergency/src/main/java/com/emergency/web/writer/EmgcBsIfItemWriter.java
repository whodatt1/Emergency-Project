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
				+ " hp_id, duty_name, duty_ha_yn, duty_er_yn,post_cdn1, post_cdn2, duty_addr, duty_tel, "
				+ " duty_inf, duty_time_1c, duty_time_2c, duty_time_3c, duty_time_4c, duty_time_5c "
				+ " duty_time_6c, duty_time_7c, duty_time_8c, duty_time_1s, duty_time_2s, duty_time_3s, "
				+ " duty_time_4s, duty_time_5s, duty_time_6s, duty_time_7s, duty_time_8s, duty_lon, "
				+ " duty_lat, dgid_id_name"
				+ ") "
				+ "VALUES "
				+ "( "
				+ " :hpId, :dutyName, :dutyHayn, :dutyEryn, :postCdn1, :postCdn2, :dutyAddr, :dutyTel, "
				+ " :dutyInf, :dutyTime1c, :dutyTime2c, :dutyTime3c, :dutyTime4c, :dutyTime5c, :dutyTime6c, "
				+ " :dutyTime7c, :dutyTime8c, :dutyTime1s, :dutyTime2s, :dutyTime3s, :dutyTime4s, :dutyTime5s, "
				+ " :dutyTime6s, :dutyTime7s, :dutyTime8s, :dutyLon, :dutyLat, :dgidIdName"
				+ ") "
				+ "ON DUPLICATE KEY UPDATE "
				+ " hp_id = :hpId, duty_name = :dutyName, duty_ha_yn = :dutyHayn, duty_er_yn = :dutyEryn "
				+ " post_cdn1 = :postCdn1, post_cdn2 = :postCdn2, duty_addr = :dutyAddr, duty_tel = :dutyTel, "
				+ " duty_inf = :dutyInf, duty_time_1c = :dutyTime1c, duty_time_2c = :dutyTime2c, duty_time_3c = :dutyTime3c, "
				+ " duty_time_4c = :dutyTime4c, duty_time_5c = :dutyTime5c, duty_time_6c = :dutyTime6c, duty_time_7c = :dutyTime7c, "
				+ " duty_time_8c = :dutyTime8c, duty_time_1s = :dutyTime1s, duty_time_2s = :dutyTime2s, duty_time_3s = :dutyTime3s, "
				+ " duty_time_4s = :dutyTime4s, duty_time_5s = :dutyTime5s, duty_time_6s = :dutyTime6s, duty_time_7s = :dutyTime7s, "
				+ " duty_time_8s = :dutyTime8s, duty_lon = :dutyLon, duty_lat = :dutyLat, "
				+ " dgid_id_name = :dgidIdName ";

		
		jdbcBatchItemWriter.setSql(sql);
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		jdbcBatchItemWriter.afterPropertiesSet();
		jdbcBatchItemWriter.write(new Chunk<>(flattenedItems));
	}
	
}
