package com.emergency.web.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.emergency.web.mapper.fcm.FcmMapper;
import com.emergency.web.model.Fcm;
import com.emergency.web.service.fcm.FcmService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class KafkaConsumerIntegrationTest {

    @Autowired
    private KafkaConsumer kafkaConsumer;

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

    // DB 조회를 가로채기 위한 가짜 객체
    @MockitoBean
    private FcmMapper fcmMapper;

    // Dispatcher가 카프카로 쏘는 걸 가로채기 위한 가짜 객체
    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final String TEST_EVENT_ID = "TEST_BATCH_001_HP_TEST_chunk0";

    @BeforeEach
    void setUp() {
        cleanUp();
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    private void cleanUp() {
        // [수정됨] Worker 멱등성 테스트를 위해 발송 이력 테이블을 초기화합니다.
        jdbcTemplate.update("DELETE FROM tb_fcm_send_history WHERE event_id = ?", TEST_EVENT_ID);
    }

    @Test
    @DisplayName("[Dispatcher 테스트] 1,250개의 토큰이 있을 때 500개씩 청크로 정확히 3번 분할되어 카프카 큐로 전송된다.")
    void testDispatcherChunkSplitting() throws Exception {
        // given: Producer가 보낸 가짜 메인 이벤트 페이로드
        String jsonPayload = """
            {
                "batchId": "TEST_BATCH_001",
                "hpId": "HP_TEST",
                "dutyName": "테스트병원"
            }
            """;

        // 가짜 토큰 1,250개 생성
        List<Fcm> dummyFcmList = new ArrayList<>();
        for (int i = 0; i < 1250; i++) {
            dummyFcmList.add(Fcm.builder().fcmToken("TOKEN_" + i).build());
        }
        
        // Dispatcher가 DB에서 토큰을 조회할 때 1,250개를 반환하도록 조작
        when(fcmMapper.getFcmListWithHpId(eq("HP_TEST"))).thenReturn(dummyFcmList);

        // when: 메인 컨슈머(Dispatcher) 호출
        kafkaConsumer.EmgcRltmConsume(jsonPayload);

        // then: 500, 500, 250 단위로 쪼개져서 kafkaTemplate.send가 정확히 "3번" 호출되었는지 검증!
        verify(kafkaTemplate, times(3)).send(eq("FcmSendTask"), eq("HP_TEST"), anyString());
    }

    @Test
    @DisplayName("[Worker 멱등성 테스트] 동일한 메시지가 2번 유입되어도, DB 제약조건에 의해 FCM 발송은 단 1회만 수행된다.")
    void testWorkerIdempotencyWithRealDatabase() throws Exception {
        // given: Dispatcher가 쪼개서 보낸 가짜 Worker 페이로드 생성
        List<String> mockTokens = List.of("TOKEN_A", "TOKEN_B");
        Map<String, Object> taskMessage = new HashMap<>();
        taskMessage.put("hpId", "HP_TEST");
        taskMessage.put("dutyName", "테스트병원");
        taskMessage.put("tokens", mockTokens);
        taskMessage.put("retryCount", 0);
        taskMessage.put("eventId", TEST_EVENT_ID);
        
        String jsonPayload = objectMapper.writeValueAsString(taskMessage);

        // sendMulticastNotification가 호출되면 빈 리스트(전원 성공)를 반환하도록 세팅
        when(fcmService.sendMulticastNotification(anyString(), anyString(), anyString(), anyList()))
            .thenReturn(new ArrayList<>());

        // when 1: Worker 최초 수신 (정상 발송되어야 함)
        kafkaConsumer.processFcmTask(jsonPayload);
        
        // when 2: 카프카 네트워크 지연 등으로 인해 Worker가 동일한 메시지를 중복 수신
        kafkaConsumer.processFcmTask(jsonPayload);

        // then: 
        // 메서드가 2번 호출되었음에도, DB의 INSERT IGNORE 방어막 덕분에
        // 실제 구글 FCM 서버로 발송을 요청하는 메서드는 단 "1번"만 호출되었는지 검증!
        verify(fcmService, times(1)).sendMulticastNotification(anyString(), anyString(), eq("HP_TEST"), anyList());
        
        // DB에 중복 저장되지 않고 토큰 수(2개)만큼만 정확히 1세트 저장되었는지 검증
        Integer historyCount = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM tb_fcm_send_history WHERE event_id = ?", Integer.class, TEST_EVENT_ID);
        assertThat(historyCount).isEqualTo(2);
    }
}