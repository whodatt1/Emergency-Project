package com.emergency.web.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : KafkaProducerConfig.java
* @author          : KHK
* @date            : 2025.06.22
* @description     : KafkaConfig
* 					 1. 저장 로직과 알림 로직의 구분을 위해 사용
* 					 2. 동일 데이터 다른 서비스로 확장 가능 (현재는 알림)
*                    3. 변경된 데이터 실시간 처리를 위해
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.22        KHK                최초 생성
 */

@Log4j2
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
	
	private final TypeSafeProperties typeSafeProperties;
	
	// Producer를 만들고 관리하는 역할
	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		log.info("프로듀서 팩토리 : " + typeSafeProperties.getBootstrapServers());
		
		Map<String, Object> config = new HashMap<>();
		// 최대 3회 재시도
//		config.put(ProducerConfig.RETRIES_CONFIG, 5);
		// 재시도 간 1초 대기
//	    config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 3000);
		// 브로커 설정
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, typeSafeProperties.getBootstrapServers());
		// Key 직렬화
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// Value 직렬화
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
	    // 메시지 압축 설정 (옵션: snappy, gzip, lz4, zstd)
	    config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		
		return new DefaultKafkaProducerFactory<>(config);
	}
	
	// Consumer를 만들고 관리하는 역할
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		log.info("컨슈머 팩토리 : " + typeSafeProperties.getBootstrapServers());
		
		Map<String, Object> config = new HashMap<>();
		// 브로커 설정
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, typeSafeProperties.getBootstrapServers());
		// 그룹 설정
		config.put(ConsumerConfig.GROUP_ID_CONFIG, typeSafeProperties.getConsumerGroup());
		// Key 직렬화
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		// Value 직렬화
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}
	
	// Kafka 메시지 전송을 도와주는 도구 내부에서 producerFactory를 통해 Producer를 얻음
//	@Bean
//	public KafkaTemplate<String, Object> kafkaTemplate() {
//		return new KafkaTemplate<>(producerFactory());
//	}
//	
//	@Bean
//	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
//		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory());
//		factory.setCommonErrorHandler(errorHandler());
//	    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
//		return factory;
//	}
	
	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate(MeterRegistry registry) {
	    KafkaTemplate<String, Object> template = new KafkaTemplate<>(producerFactory());

	    // Producer 메트릭 바인딩
	    new KafkaClientMetrics(template.getProducerFactory().createProducer()).bindTo(registry);

	    return template;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(MeterRegistry registry) {
	    ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    factory.setCommonErrorHandler(errorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
	    // 컨슈머 팩토리에 Micrometer 리스너를 추가하여 메트릭을 바인딩합니다.
        ((DefaultKafkaConsumerFactory) consumerFactory()).addListener(new MicrometerConsumerListener<>(registry));

	    return factory;
	}
	
	// Consumer Exception 발생시 재시도 핸들러 추가
	@Bean
	public DefaultErrorHandler errorHandler() {
		return new DefaultErrorHandler((consumerRecord, exception) -> {
											log.error("Error consuming record: {}", consumerRecord, exception);
										}, new FixedBackOff(3000, 5));
	}
	
}
