package com.emergency.web.config;


import java.time.Duration;

import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.emergency.web.model.EmgcRltm;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.netty.channel.ChannelOption;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Log4j2
@Configuration
public class GlobalConfig {
	
	// WebConfig 설정 예정
	@Bean
	public WebClient webClient() {
		
		// 무한대기 방지
		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 서버와 연결 시도 최대 5초 설정
				.responseTimeout(Duration.ofSeconds(5)); // 응답이 5초 이내 오지 않을경우 타임아웃
		
		// 서비스 키 인코딩 문제로 인한 UriBuilderFactory 설정
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

		
		return WebClient.builder()
				.uriBuilderFactory(factory) // URI 인코딩 설정 적용
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultHeader("Accept", MediaType.APPLICATION_XML_VALUE) // 기본 헤더 설정 << 이거 지워도 되겠네
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
		return new XmlMapper();
	}
	
}
