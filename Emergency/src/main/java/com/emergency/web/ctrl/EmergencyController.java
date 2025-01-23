package com.emergency.web.ctrl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.request.emgc.RealTimeInfoRequestDto;
import com.emergency.web.dto.response.emgc.RealTimeInfoResponseDto;
import com.emergency.web.service.emgc.EmergencyService;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.jwt
* @fileName        : EmergencyController.java
* @author          : KHK
* @date            : 2024.10.17
* @description     : 응급 정보 공공기관 API
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.20        KHK                최초 생성
 */

@RestController
@RequiredArgsConstructor
public class EmergencyController {
	
	private final EmergencyService emergencyService;
	
	@GetMapping("/api/v1/emgc/reatimeinfo")
	public ResponseEntity<?> getEmgcRealTimeInfoList(@ModelAttribute RealTimeInfoRequestDto realTimeInfoRequestDto) {
		
		List<RealTimeInfoResponseDto> ralTimeInfoResponseDtos = emergencyService.getEmgcRealTimeInfoList(realTimeInfoRequestDto);
		
		return null;
	}
}
