package com.emergency.web.reader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.response.emgc.EmgcBsIfResponseDto;
import com.emergency.web.mapper.emgc.EmgcMapper;
import com.emergency.web.struct.EmgcBsIfStruct;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
* @packageName     : com.emergency.web.reader
* @fileName        : EmgcBslfItemReader.java
* @author          : KHK
* @date            : 2025.02.19
* @description     : 배치 작업에서 읽어오는 과정을 처리 (디테일의 마스터 정보)
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.02.19        KHK                최초 생성
 */

@Log4j2
@Component
@StepScope
@RequiredArgsConstructor
public class EmgcBsIfItemReader implements ItemReader<EmgcBsIfResponseDto> {
	
	private final TypeSafeProperties typeSafeProperties;
	
	private final EmgcBsIfStruct emgcBsIfStruct;
	
	private final WebClient webClient;
	
	private final XmlMapper xmlMapper;
	
	private final EmgcMapper emgcRltmMapper;
	
	// List에 담긴 객체를 단일 객체로 넘겨주기 위한 버퍼
	private Queue<EmgcBsIfResponseDto> buffer = new LinkedList<>();
	
	private String lastHpId = ""; // Keyset Pagination 기준
	
	private int pageSize = 500;
	
	@Override
	public EmgcBsIfResponseDto read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		if (buffer.isEmpty()) {
			List<String> hpIdList = getHpIdList();
			
			if (hpIdList.isEmpty()) {
				return null;
			}
			
			List<EmgcBsIfResponseDto.ApiResponse> resps = getBsIfList(hpIdList);
			
			buffer.addAll(
					resps.stream()
						.map(resp -> emgcBsIfStruct.toDto(resp.getBody().getItems().get(0)))
						.collect(Collectors.toList())
			);
		}
		
		return buffer.poll();
	}
	
	private List<String> getHpIdList() {
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("lastHpId", lastHpId);
		paramMap.put("pageSize", pageSize);
		
		List<String> hpIdList = emgcRltmMapper.getEmgcRltmHpIdListBeforeBatch(paramMap);
		
		if (!hpIdList.isEmpty()) {
			lastHpId = hpIdList.get(hpIdList.size() - 1);
		}
		
		return hpIdList;
	}
	
	private List<EmgcBsIfResponseDto.ApiResponse> getBsIfList(List<String> hpIdList) throws Exception {
		
		// 여기서 APIRESPONSE로 받아서 거기서 추출해야됨
		// 모아서 바로 List로 변환하여 반환
		return Flux.fromIterable(hpIdList)
				.flatMap(hpId -> webClient.get()
						.uri(uriBuilder -> uriBuilder.scheme("https")
								.host(typeSafeProperties.getApiHost())
								.path(typeSafeProperties.getApiBsIfPath())
								.queryParam("serviceKey", typeSafeProperties.getApiNormalEncoding())
								.queryParam("HPID", hpId)
								.build())
						.retrieve()
						.onStatus(status -> status.isError(), clientResponse -> Mono.error(new Exception())) // API로부터 오류가 있을때 오류 반환
						.bodyToMono(String.class)
						.map(xml -> {
							try {
								log.info("XML Response SUC: {}", xml);
								
								return xmlMapper.readValue(xml, EmgcBsIfResponseDto.ApiResponse.class);
							} catch (Exception e) {
								log.error("XML RESPONSE ERROR: {}", e.getMessage());
								return null;
							}
						}), 10) // 10건씩 병렬로 처리
				.filter(response -> response != null)
				.collectList()
				.block();
	}
}
