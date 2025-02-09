package com.emergency.web.reader;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : EmgcRltmItemReader.java
* @author          : KHK
* @date            : 2025.01.31
* @description     : 배치 작업에서 읽어오는 과정을 처리
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.31        KHK                최초 생성
 */

@Log4j2
@Component
public class EmgcRltmItemReader implements ItemReader<List<EmgcRltmResponseDto>> {
	
	@Autowired
	private TypeSafeProperties typeSafeProperties;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private XmlMapper xmlMapper;
	
	private int currPage = 1;
	
	@Override
	public List<EmgcRltmResponseDto> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		EmgcRltmResponseDto.ApiResponse resp = getRltmList(currPage);
		
		// 호출이 실패한 경우는 익셉션 반환
		if (!resp.requestSuccess()) {
			log.error("RltmApi call fail : " + resp.getHeader().getResultMsg());
			return null;
		}
		
		if (!resp.isLastPage()) {
			currPage++;
		} else {
			return null;
		}
		
		return resp.getBody().getItems().stream().map(rltmInfo -> {
			EmgcRltmResponseDto responseDto = modelMapper.map(rltmInfo, EmgcRltmResponseDto.class);
			
			return responseDto;
		}).collect(Collectors.toList());
	}
	 
	public EmgcRltmResponseDto.ApiResponse getRltmList(int currPage) throws Exception {
		
		log.info("API Host: {}", typeSafeProperties.getApiHost());
		log.info("API Path: {}", typeSafeProperties.getApiRltmPath());
		
		// String으로 XML 응답을 받음
		String xmlResponse = webClient
				.get()
				.uri(uriBuilder -> uriBuilder.scheme("https")
						.host(typeSafeProperties.getApiHost())
						.path(typeSafeProperties.getApiRltmPath()) // 엔드포인트 경로
						.queryParam("serviceKey", typeSafeProperties.getApiNormalEncoding()) // API 서비스키 전달
						.queryParam("pageNo", currPage)
						.queryParam("numOfRows", 100)
						.build()
						)
				.retrieve() // API로부터 데이터를 가져옴
				.onStatus(status -> status.isError(), clientResponse -> Mono.error(new Exception())) // API로부터 오류가 있을때 오류 반환
				.bodyToMono(String.class) // XML 응답을 String으로 받음
				.block(); // 비동기 요청을 동기적으로 기다리고 결과를 반환 (Mono의 단일 값 처리)
		
		log.info("XML Response: {}", xmlResponse);
		
		// XML
		return xmlMapper.readValue(xmlResponse, EmgcRltmResponseDto.ApiResponse.class);
	}
}
