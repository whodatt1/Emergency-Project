package com.emergency.web.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import lombok.RequiredArgsConstructor;

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

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
	
	private final TypeSafeProperties typeSafeProperties;
	
	// Producer를 만들고 관리하는 역할
	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		// 브로커 설정
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, typeSafeProperties.getBootstrapServers());
		// Key 직렬화
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// Value 직렬화
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}
	
	// Consumer를 만들고 관리하는 역할
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		Map<String, Object> config = new HashMap<>();
		// 브로커 설정
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, typeSafeProperties.getBootstrapServers());
		// 그룹 설정
		config.put(ConsumerConfig.GROUP_ID_CONFIG, typeSafeProperties.getConsumerGroup());
		// Key 직렬화
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// Value 직렬화
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}
	
	// Kafka 메시지 전송을 도와주는 도구 내부에서 producerFactory를 통해 Producer를 얻음
	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
	
}
