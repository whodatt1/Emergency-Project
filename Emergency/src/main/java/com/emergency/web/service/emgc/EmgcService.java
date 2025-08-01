package com.emergency.web.service.emgc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emergency.web.dto.request.emgc.EmgcMstRequestDto;
import com.emergency.web.dto.response.emgc.EmgcDtlResponseDto;
import com.emergency.web.dto.response.emgc.EmgcMstResponseDto;
import com.emergency.web.mapper.emgc.EmgcMapper;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.service.emgc
* @fileName        : EmgcService.java
* @author          : KHK
* @date            : 2024.10.16
* @description     :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */


@Service
@RequiredArgsConstructor
public class EmgcService {
	
	private final EmgcMapper emgcMapper;
	private final ModelMapper modelMapper;
	
	public Page<?> getEmgcMstList(EmgcMstRequestDto emgcMstRequestDto, Pageable pageable) {
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("emgcMstRequestDto", emgcMstRequestDto);
		paramMap.put("pageable", pageable);
		
		List<EmgcMstResponseDto> content = emgcMapper.getEmgcMstList(paramMap).stream()
				.map(mst -> modelMapper.map(mst, EmgcMstResponseDto.class)).collect(Collectors.toList());
		int contentTotalCnt = emgcMapper.getEmgcMstListCnt(paramMap);
		
		return new PageImpl<>(content, pageable, contentTotalCnt);
	}

	public EmgcDtlResponseDto getEmgcDtl(String hpId) {
		return emgcMapper.getEmgcDtl(hpId);
	}
	
}
