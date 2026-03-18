package com.emergency.web.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.emergency.web.kafka.KafkaProducer;
import com.emergency.web.mapper.outbox.OutboxMapper;
import com.emergency.web.model.EmgcRltm;

@SpringBootTest
public class EmgcRltmItemWriterIntegrationTest {

	@Autowired
	private EmgcRltmItemWriter<EmgcRltm> emgcRltmItemWriter;
	
	@Autowired
	private OutboxMapper outboxMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 카프카로 실제 메시지가 날아가는 것을 방지하기 위한 가짜 객체
	@MockitoBean
	private KafkaProducer kafkaProducer;
	
	@MockitoBean
	private KafkaAdmin kafkaAdmin;
	
	@MockitoBean
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@BeforeEach
	void setUp() {
		// 1. 기존 데이터 보호를 위해 테스트용 ID만 명시적으로 사전 삭제
	    cleanUpTestData();
	    
	    // =================================================================
	    // 가짜 Step 환경(Context) 구축
	    // =================================================================
	    JobParameters jobParameters = new JobParametersBuilder()
	            .addString("batchId", "TEST-BATCH-001")
	            .toJobParameters();

	    // 파라미터를 품은 가짜 StepExecution 생성
	    StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);

	    // 현재 실행 중인 테스트 스레드에 가짜 Step 환경을 활성화
	    // Writer 객체를 만들어서 batchId를 주입.
	    StepSynchronizationManager.register(stepExecution);
	    
	    // [Given] DB에 미리 존재하는 테스트용 데이터 세팅
	    jdbcTemplate.update("INSERT INTO tb_emgc_rltm_detail (hp_id, duty_name, upd_date) VALUES (?, ?, ?)", 
	                         "HP001", "기존병원", "20240101121212");
	                         
	    jdbcTemplate.update("INSERT INTO tb_emgc_rltm_detail (hp_id, duty_name, upd_date) VALUES (?, ?, ?)", 
	                         "HP002", "업데이트대상병원", "20231231121212");
	}
	
	@AfterEach
	void tearDown() {
		cleanUpTestData();
		// 테스트가 끝나면 가짜 Step 환경을 해제.
		StepSynchronizationManager.release();
	}
	
	private void cleanUpTestData() {
		// 해당 배치 ID로 생성된 Outbox 데이터 삭제
		jdbcTemplate.update("DELETE FROM tb_outbox WHERE batch_id = 'TEST-BATCH-001'");
		// 테스트용 병원 데이터 3건 삭제
		jdbcTemplate.update("DELETE FROM tb_emgc_rltm_detail WHERE hp_id IN ('HP001', 'HP002', 'HP003')");
	}
	
	@Test
    @DisplayName("Writer는 updDate가 변경된 데이터와 신규 데이터만 필터링하여 이벤트를 발행하고 Outbox에 저장한다.")
    @Transactional // 트랜잭션 환경 구성
    void testWriterFilteringAndOutboxEvent() throws Exception {
        // given: API에서 읽어온(Reader가 넘겨준) 3건의 데이터라고 가정
        EmgcRltm item1 = EmgcRltm.builder()
        		.hpId("HP001")
        		.updDate("20240101121212")
        		.build();
        
        EmgcRltm item2 = EmgcRltm.builder()
        		.hpId("HP002")
        		.updDate("20240102121212")
        		.build();

        EmgcRltm item3 = EmgcRltm.builder()
        		.hpId("HP003")
        		.updDate("20240102121212")
        		.build();

        Chunk<EmgcRltm> chunk = new Chunk<>(List.of(item1, item2, item3));

        // when: Writer 실행
        emgcRltmItemWriter.write(chunk);

        // 테스트 환경에서는 기본적으로 자동 롤백되므로 AFTER_COMMIT 이벤트가 미발생
        // 강제로 커밋 처리를 해서 BEFORE_COMMIT과 AFTER_COMMIT 리스너를 모두 호출
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then 1: 필터링 검증 (3개가 들어왔지만, Outbox 테이블에는 HP002, HP003 딱 2건만 저장되어야 함)
        Integer outboxCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tb_outbox", Integer.class);
        assertThat(outboxCount).isEqualTo(419); // 기존 테이블 417

        // then 2: 저장된 HP002와 HP003이 정상적으로 READY_TO_PUBLISH 상태인지 확인
        String statusHp002 = outboxMapper.selectOutboxStatus("TEST-BATCH-001", "HP002");
        assertThat(statusHp002).isEqualTo("READY_TO_PUBLISH");
        
        String statusHp003 = outboxMapper.selectOutboxStatus("TEST-BATCH-001", "HP003");
        assertThat(statusHp003).isEqualTo("READY_TO_PUBLISH");

        // then 3: AFTER_COMMIT 리스너가 동작해서 카프카 전송 메서드가 호출되었는지 확인
        // (@Async 비동기 호출이므로 2초(2000ms) 이내에 1번 호출되었는지 검증)
        verify(kafkaProducer, timeout(2000).times(1)).publishEmgcRltmEvent(any());
    }
}
