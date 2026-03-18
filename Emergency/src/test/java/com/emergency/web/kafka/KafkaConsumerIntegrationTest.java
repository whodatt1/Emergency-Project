package com.emergency.web.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.emergency.web.mapper.fcm.FcmMapper;
import com.emergency.web.mapper.outbox.OutboxMapper;
import com.emergency.web.model.Fcm;
import com.emergency.web.service.fcm.FcmService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class KafkaConsumerIntegrationTest {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private OutboxMapper outboxMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 포트 무한 연결 시도 차단
    @MockitoBean
    private KafkaAdmin kafkaAdmin;

    // FCM 서버(Firebase) 가짜 객체
    @MockitoBean
    private FcmService fcmService;

    @MockitoBean
    private FcmMapper fcmMapper;

    private final String TEST_BATCH_ID = "CONSUME-BATCH-001";

    @BeforeEach
    void setUp() {
        cleanUp();
        
        // [Given] DB에 2가지 상태의 Outbox 데이터를 세팅합니다.
        String sql = "INSERT INTO tb_outbox (batch_id, aggregate_type, aggregate_id, event_type, payload, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        // 1. 이미 처리 완료된 건 (멱등성 테스트용)
        jdbcTemplate.update(sql, TEST_BATCH_ID, "EmgcRltm", "HP_FINISHED", "EmgcRltmUpdate", "{}", "FINISHED");
        
        // 2. 이제 막 카프카로 전송되어 FCM 발송을 기다리는 건 (정상 발송 테스트용)
        jdbcTemplate.update(sql, TEST_BATCH_ID, "EmgcRltm", "HP_READY", "EmgcRltmUpdate", "{}", "COMPLETED");
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    private void cleanUp() {
        jdbcTemplate.update("DELETE FROM tb_outbox WHERE batch_id = ?", TEST_BATCH_ID);
    }

    @Test
    @DisplayName("이미 FINISHED 상태인 메시지가 들어오면 FCM을 발송하지 않고 무시한다.")
    void testIdempotency_AlreadyFinished() throws Exception {
        // given: 페이로드 생성
        String jsonPayload = """
            {
                "batchId": "CONSUME-BATCH-001",
                "hpId": "HP_FINISHED",
                "dutyName": "이미처리된병원"
            }
            """;

        // when: 컨슈머 메서드 수동 호출
        kafkaConsumer.EmgcRltmConsume(jsonPayload);

        // fcmService.sendMulticastNotification(...) 메서드가 호출되지 않았음을 확인
        verify(fcmService, never()).sendMulticastNotification(anyString(), anyString(), anyString(), anyList());
    }

    @Test
    @DisplayName("[시나리오 2] 250개의 FCM 토큰이 있으면 100개씩 청크로 나누어 총 3번 발송하고 FINISHED로 바꾼다.")
    void testFcmChunkSendingAndStatusUpdate() throws Exception {
        // given: 페이로드 생성
        String jsonPayload = """
            {
                "batchId": "CONSUME-BATCH-001",
                "hpId": "HP_READY",
                "dutyName": "정상발송병원"
            }
            """;

        // =========================================================================
        // 💡 [TODO 2] 가짜 토큰 250개 만들기 (Mock 조작)
        // 목표: fcmMapper.getFcmListWithHpId("HP_READY")가 호출되면 
        //       250개의 가짜 Fcm 객체가 담긴 리스트를 반환하도록 조작하세요.
        // =========================================================================
        
        List<Fcm> dummyFcmList = new ArrayList<>();
        for (int i = 0; i < 250; i++) {
            Fcm fcm = Fcm.builder()
            		.fcmToken("TOKEN_" + i)
            		.build();
            dummyFcmList.add(fcm);
        }
        
        // 여기에 when(...).thenReturn(...) 코드를 작성하세요.
        when(fcmMapper.getFcmListWithHpId(eq("HP_READY")))
        		.thenReturn(dummyFcmList);
        

        // when: 컨슈머 메서드 수동 호출
        kafkaConsumer.EmgcRltmConsume(jsonPayload);

        // 1: fcmService.sendMulticastNotification(...) 메서드가 3번 호출되었는지 검증
        // 2: DB를 조회해서 HP_READY의 상태가 'FINISHED'로 바뀌었는지 검증

        // 여기에 코드를 작성하세요.
        verify(fcmService, times(3)).sendMulticastNotification(anyString(), anyString(), anyString(), anyList());
        
        // DB 상태 검증
        String finalStatus = outboxMapper.selectOutboxStatus("CONSUME-BATCH-001", "HP_READY");
        
        assertThat(finalStatus).isEqualTo("FINISHED");
        
        
    }
}