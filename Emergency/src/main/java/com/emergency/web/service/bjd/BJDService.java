package com.emergency.web.service.bjd;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergency.web.dto.response.bjd.BJDResponseDto;
import com.emergency.web.mapper.bjd.BJDMapper;
import com.emergency.web.model.BJD;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.service.bjd
* @fileName        : BJDService.java
* @author          : KHK
* @date            : 2025.03.26
* @description     :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.26        KHK                최초 생성
 */

@Service
@RequiredArgsConstructor
public class BJDService {
	
	private final BJDMapper bjdMapper;
	private final ModelMapper modelMapper;
	
	@Transactional
	public List<BJDResponseDto> getSidoList() {
		List<BJD> sidoList = bjdMapper.getSidoList();
		return sidoList.stream().map(bjd -> modelMapper.map(bjd, BJDResponseDto.class)).collect(Collectors.toList());
	}
	
	@Transactional
	public List<BJDResponseDto> getGugunList(String sidoCd) {
		List<BJD> gugunList = bjdMapper.getGugunList(sidoCd);
		return gugunList.stream().map(bjd -> modelMapper.map(bjd, BJDResponseDto.class)).collect(Collectors.toList());
	}
	
	@Transactional
	public List<BJDResponseDto> getDongList(String gugunCd) {
		List<BJD> dongList = bjdMapper.getDongList(gugunCd); 
		return dongList.stream().map(bjd -> modelMapper.map(bjd, BJDResponseDto.class)).collect(Collectors.toList());
	}
}
