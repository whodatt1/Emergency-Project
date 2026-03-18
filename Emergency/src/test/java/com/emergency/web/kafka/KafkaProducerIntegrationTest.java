package com.emergency.web.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.emergency.web.event.EmgcRltmBatchEvent;
import com.emergency.web.mapper.outbox.OutboxMapper;
import com.emergency.web.model.EmgcRltm;
import com.emergency.web.scheduler.EmergencyScheduler;

@SpringBootTest
public class KafkaProducerIntegrationTest {
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Autowired
	private OutboxMapper outboxMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 9092 포트 무한 연결 시도 차단
	@MockitoBean
	private KafkaAdmin kafkaAdmin;
	
	@MockitoBean
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@MockitoBean
	private EmergencyScheduler emergencyScheduler;
	
	private final String TEST_BATCH_ID = "ASYNC-BATCH-001";
	
	@BeforeEach
	void setUp() {
		cleanUp();
		
		// [Given] DB에 Outbox 데이터가 'READY_TO_PUBLISH' 상태로 들어있다고 가정
        String sql = "INSERT INTO tb_outbox (batch_id, aggregate_type, aggregate_id, event_type, payload, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql, TEST_BATCH_ID, "EmgcRltm", "HP001", "EmgcRltmUpdate", "{\"hpId\":\"HP001\"}", "READY_TO_PUBLISH");
        jdbcTemplate.update(sql, TEST_BATCH_ID, "EmgcRltm", "HP002", "EmgcRltmUpdate", "{\"hpId\":\"HP002\"}", "READY_TO_PUBLISH");
        jdbcTemplate.update(sql, TEST_BATCH_ID, "EmgcRltm", "HP003", "EmgcRltmUpdate", "{\"hpId\":\"HP003\"}", "READY_TO_PUBLISH");
	}
	
	@AfterEach
	void tearDown() {
		cleanUp();
	}
	
	private void cleanUp() {
		jdbcTemplate.update("DELETE FROM tb_outbox WHERE batch_id = ?", TEST_BATCH_ID);
	}
	
	@Test
	@DisplayName("Kafka 전송 시 성공/실패 여부에 따라 Outbox 테이블의 상태가 벌크 업데이트 된다.")
	void testAsyncBulkUpdateOnKafkaCallback() throws Exception {
		// given : 이벤트 객체 생성 (병원 3개 세팅)
		EmgcRltm item1 = EmgcRltm.builder().hpId("HP001").dutyName("병원1").build();
        EmgcRltm item2 = EmgcRltm.builder().hpId("HP002").dutyName("병원2").build();
        EmgcRltm item3 = EmgcRltm.builder().hpId("HP003").dutyName("병원3").build();
        
        Chunk<EmgcRltm> chunk = new Chunk<>(List.of(item1, item2, item3));

        EmgcRltmBatchEvent<EmgcRltm> event = new EmgcRltmBatchEvent<>(chunk, TEST_BATCH_ID);
        
        // "성공"과 "실패" 상황
        
        // HP001, HP002는 전송에 성공
        when(kafkaTemplate.send(eq("EmgcRltmEvent"), eq("HP001"), anyString()))
        		.thenReturn(CompletableFuture.completedFuture(null));
        
        when(kafkaTemplate.send(eq("EmgcRltmEvent"), eq("HP002"), anyString()))
        		.thenReturn(CompletableFuture.completedFuture(null));
        
        // HP003은 전송중 네트워크 에러
        when(kafkaTemplate.send(eq("EmgcRltmEvent"), eq("HP003"), anyString()))
        		.thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        // when: 카프카 프로듀서의 전송 메서드 실행
        kafkaProducer.publishEmgcRltmEvent(event);

        // 비동기 대기 (벌크 업데이트가 별도 스레드에서 수행되므로 해당 작업 여유 시간 부여)
        Thread.sleep(1000); 
        
        // DB 검증: 아웃박스 벌크 업데이트 결과 확인
        String statusHp001 = outboxMapper.selectOutboxStatus(TEST_BATCH_ID, "HP001");
        String statusHp002 = outboxMapper.selectOutboxStatus(TEST_BATCH_ID, "HP002");
        String statusHp003 = outboxMapper.selectOutboxStatus(TEST_BATCH_ID, "HP003");

        
        // DB를 조회하여 HP001과 HP002는 'COMPLETED', HP003은 'FAIL'인지 확인
        
        // 성공한 2건은 COMPLETED로 바뀌어야 함
        assertThat(statusHp001).isEqualTo("COMPLETED");
        assertThat(statusHp002).isEqualTo("COMPLETED");
        
        // 에러난 1건은 FAIL로 바뀌어야 함
        assertThat(statusHp003).isEqualTo("FAIL");
	}
}
