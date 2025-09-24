package com.emergency.web.config;


import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.emergency.web.common.util.IntegerDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * 
 * 
 * @packageName     : com.emergency.web.config
 * @fileName        : GlobalConfig.java
 * @author          : KHK
 * @date            : 2025. 3. 5.
 * @description     : 전역 Config 정의
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 3. 5.        KHK                최초 생성
 */

@Log4j2
@Configuration
public class GlobalConfig {
	
	// WebConfig 설정 예정
	@Bean
	public WebClient webClient() {
		
		HttpClient httpClient = HttpClient.create()
				// 서버와 연결을 시도할 때 최대 10초 동안 기다림
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                // 서버로부터 응답을 받을 때 최대 120초 동안 기다림
                .responseTimeout(Duration.ofSeconds(120))
                // 연결이 맺어진 이후에 읽기/쓰기 타임아웃 핸들러 추가
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(120)) // 읽기 타임아웃 120초
                            .addHandlerLast(new WriteTimeoutHandler(120)) // 쓰기 타임아웃 120초
                );
		
		// 서비스 키 인코딩 문제로 인한 UriBuilderFactory 설정
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

		
		return WebClient.builder()
				.uriBuilderFactory(factory) // URI 인코딩 설정 적용
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultHeader("Accept", MediaType.APPLICATION_XML_VALUE) // 기본 헤더 설정 << 이거 지워도 되겠네
				.exchangeStrategies(ExchangeStrategies.builder()
		                .codecs(configurer -> 
		                        configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // 16MB
		                .build())
				.filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
					log.info("Request URL: {}", clientRequest.url()); // 요청 URL 로그 출력 호스트 에러때문에 추가
					return Mono.just(clientRequest);
				})).build();
		
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public XmlMapper xmlMapper() {
		XmlMapper mapper = new XmlMapper();
		
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Integer.class, new IntegerDeserializer());
		
		mapper.registerModule(module);
		
		return mapper;
	}
	
	@Bean
	@Primary
	public ObjectMapper objectMapper() {
	    return new ObjectMapper();
	}
	
	// 컨트롤러 응답시 XML로 처리되는 상황 발생
	// ObjectMapper를 우선시하도록 설정함
	@Bean
	public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(ObjectMapper objectMapper) {
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    converter.setObjectMapper(objectMapper);
	    return converter;
	}
}
