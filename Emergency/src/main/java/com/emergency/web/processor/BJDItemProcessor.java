package com.emergency.web.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.emergency.web.dto.response.bjd.BJDResponseDto;
import com.emergency.web.model.BJD;

/**
 * 
* @packageName     : com.emergency.web.processor
* @fileName        : BDJItemProcessor.java
* @author          : KHK
* @date            : 2025.03.19
* @description     : 배치 작업에서 읽어온 데이터를 다른 형태로 변환 (법정동 코드)
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.19        KHK                최초 생성
 */

@Component
public class BJDItemProcessor implements ItemProcessor<List<BJDResponseDto>, List<BJD>> {

	@Override
	public List<BJD> process(List<BJDResponseDto> item) throws Exception {
		// TODO Auto-generated method stub
		return item.stream().map(BJDResponseDto::toEntity).collect(Collectors.toList());
	}
	
	
}
