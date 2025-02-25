package com.emergency.web.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;
import com.emergency.web.model.EmgcRltm;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : EmgcRltmItemReader.java
* @author          : KHK
* @date            : 2025.02.09
* @description     : 배치 작업에서 읽어온 데이터를 다른 형태로 변환 (세부 정보)
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.02.09        KHK                최초 생성
 */

@Component
public class EmgcRltmItemProcessor implements ItemProcessor<List<EmgcRltmResponseDto>, List<EmgcRltm>> {

	@Override
	public List<EmgcRltm> process(List<EmgcRltmResponseDto> item) throws Exception {
		// DTO to VO
		return item.stream().map(EmgcRltmResponseDto::toEntity).collect(Collectors.toList());
	}
	
}
