package com.emergency.web.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.emergency.web.dto.response.emgc.EmgcBsIfResponseDto;
import com.emergency.web.model.EmgcBsIf;

/**
 * 
 * @packageName     : com.emergency.web.processor
 * @fileName        : EmgcBsIfItemProcessor.java.java
 * @author          : KHK
 * @date            : 2025. 2. 25.
 * @description     : 배치 작업에서 읽어온 데이터를 다른 형태로 변환 (마스터 정보)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 2. 25.      KHK                최초 생성
 */

@Component
public class EmgcBsIfItemProcessor implements ItemProcessor<List<EmgcBsIfResponseDto>, List<EmgcBsIf>> {

	@Override
	public List<EmgcBsIf> process(List<EmgcBsIfResponseDto> item) throws Exception {
		// DTO to VO
		return item.stream().map(EmgcBsIfResponseDto::toEntity).collect(Collectors.toList());
	}
	
}
