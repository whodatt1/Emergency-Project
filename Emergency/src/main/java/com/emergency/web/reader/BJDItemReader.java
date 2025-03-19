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
import com.emergency.web.dto.response.bjd.BJDResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * 
* @packageName     : com.emergency.web.reader
* @fileName        : BJDItemReader.java
* @author          : KHK
* @date            : 2025.03.19
* @description     : 배치 작업에서 읽어오는 과정을 처리 (법정동 코드)
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.19        KHK                최초 생성
 */

@Log4j2
@Component
public class BJDItemReader implements ItemReader<List<BJDResponseDto>> {
	
	@Autowired
	private TypeSafeProperties typeSafeProperties;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private int currPage = 1;
	
	@Override
	public List<BJDResponseDto> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		BJDResponseDto.ApiResponse resp = getBJDList(currPage);
		
		if (!resp.requestSuccess()) {
			log.error("BJDApi call fail !");
			return null;
		}
		
		if (!resp.isLastPage()) {
			currPage++;
		} else {
			return null;
		}
		
		return resp.getData().stream().map(bdjItem -> {
			BJDResponseDto responseDto = modelMapper.map(bdjItem, BJDResponseDto.class);
			
			return responseDto;
		}).collect(Collectors.toList());
	}
	
	public BJDResponseDto.ApiResponse getBJDList(int currPage) throws Exception {
		
		return webClient
				.get()
				.uri(uriBuilder -> uriBuilder.scheme("https")
						.host(typeSafeProperties.getBjdApiHost())
						.path(typeSafeProperties.getApiRltmPath()) // 엔드포인트 경로
						.queryParam("page", currPage)
						.queryParam("perPage", 100)
						.queryParam("serviceKey", typeSafeProperties.getBjdApiNormarDecoding())
						.build()
						)
				.retrieve() // API로부터 데이터를 가져옴
				.onStatus(status -> status.isError(), clientResponse -> Mono.error(new Exception())) // API로부터 오류가 있을때 오류 반환
				.bodyToMono(BJDResponseDto.ApiResponse.class)
				.block();
	}
}
