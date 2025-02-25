package com.emergency.web.reader;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.response.emgc.EmgcBsIfResponseDto;
import com.emergency.web.mapper.emgc.EmgcRltmMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

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
public class EmgcBsIfItemReader implements ItemReader<List<EmgcBsIfResponseDto>> {
	
	@Autowired
	private TypeSafeProperties typeSafeProperties;
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private XmlMapper xmlMapper;
	
	@Autowired
	private EmgcRltmMapper emgcRltmMapper;
	
	private int offset = 0;
	
	@Override
	public List<EmgcBsIfResponseDto> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		List<String> hpIdList = emgcRltmMapper.getEmgcRltmHpIdListBeforeBatch(offset);
		
		if (hpIdList.isEmpty()) {
			return null;
		}
		
		offset += 100;
		
		List<EmgcBsIfResponseDto> list = getBsIfList(hpIdList);
		
		return list;
	}
	
	public List<EmgcBsIfResponseDto> getBsIfList(List<String> hpIdList) throws Exception {
		
		// 모아서 바로 List로 변환하여 반환
		return Flux.fromIterable(hpIdList)
				.flatMap(hpId -> webClient.get()
						.uri(uriBuilder -> uriBuilder.scheme("https")
								.host(typeSafeProperties.getApiHost())
								.path(typeSafeProperties.getApiRltmPath())
								.queryParam("serviceKey", typeSafeProperties.getApiNormalEncoding())
								.queryParam("HPID", hpId)
								.build())
						.retrieve()
						.onStatus(status -> status.isError(), clientResponse -> Mono.error(new Exception())) // API로부터 오류가 있을때 오류 반환
						.bodyToMono(String.class)
						.map(xml -> {
							try {
								return xmlMapper.readValue(xml, EmgcBsIfResponseDto.class);
							} catch (Exception e) {
								log.error("XML CONVERT ERROR: {}", e.getMessage());
								return null;
							}
						}), 10) // 10건씩 병렬로 처리
				.filter(response -> response != null)
				.collectList()
				.block();
				
	}
}
