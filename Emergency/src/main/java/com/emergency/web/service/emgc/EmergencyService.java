package com.emergency.web.service.emgc;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emergency.web.dto.request.emgc.RealTimeInfoRequestDto;
import com.emergency.web.dto.response.emgc.RealTimeInfoResponseDto;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.jwt
* @fileName        : EmergencyController.java
* @author          : KHK
* @date            : 2024.10.17
* @description     : 응급 정보 공공기관 비즈니스 로직
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.20        KHK                최초 생성
 */

@Service
@RequiredArgsConstructor
public class EmergencyService {
	
	// 실시간 정보 공공기관 api 통하여 가져오기
	public List<RealTimeInfoResponseDto> getEmgcRealTimeInfoList(RealTimeInfoRequestDto realTimeInfoRequestDto) {
		return null;
	}
}
