package com.emergency.web.reader;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EmgcRltmItemReader implements ItemReader<List<EmgcRltmResponseDto>> {
	
	private final WebClient webClient;
	
	private final TypeSafeProperties typeSafeProperties;
	
	private final ModelMapper modelMapper;
	
	private int currPage = 1;
	
	@Override
	public List<EmgcRltmResponseDto> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		EmgcRltmResponseDto.ApiResponse result = getRltmList(currPage);
		
		// 호출이 실패한 경우는 익셉션 반환
		if (!result.requestSuccess()) {
			throw new Exception("");
		}
		
		if (!result.isLastPage()) {
			currPage++;
		} else {
			return null;
		}
		
		return result.getBody().getItemList().stream().map(rltmInfo -> {
			EmgcRltmResponseDto responseDto = modelMapper.map(rltmInfo, EmgcRltmResponseDto.class);
			
			return responseDto;
		}).collect(Collectors.toList());
	}
	 
	public EmgcRltmResponseDto.ApiResponse getRltmList(int currPage) throws Exception {
		return webClient
				.get()
				.uri(uriBuilder -> uriBuilder.scheme("https")
						.host(typeSafeProperties.getApiHost()) // API 서버의 호스트 주소
						.path(typeSafeProperties.getApiRltmPath()) // 엔드포인트 경로
						.queryParam("serviceKey", typeSafeProperties.getApiNormalEncoding()) // API 서비스키 전달
						.queryParam("pageNo", currPage)
						.queryParam("numOfRows", 10)
						.build())
				.retrieve() // API로부터 데이터를 가져옴
				.onStatus(status -> status.isError(), clientResponse -> Mono.error(new Exception())) // API로부터 오류가 있을때 오류 반환
				.bodyToMono(EmgcRltmResponseDto.ApiResponse.class) // 서버의 응답을 EmgcRltmResponseDto.ApiResponse 타입으로 반환
				.block(); // 비동기 요청을 동기적으로 기다리고 결과를 반환 (Mono의 단일 값 처리)
	}
}
