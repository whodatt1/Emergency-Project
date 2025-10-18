package com.emergency.web.ctrl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.request.emgc.EmgcMstRequestDto;
import com.emergency.web.dto.response.emgc.EmgcDtlResponseDto;
import com.emergency.web.service.emgc.EmgcService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 
* @packageName     : com.emergency.web.ctrl
* @fileName        : EMGCController.java
* @author          : KHK
* @date            : 2025.04.04
* @description     : 법정동 코드 관리 컨트롤러
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.04.04        KHK                최초 생성
 */

@RestController
@RequiredArgsConstructor
public class EmgcController {
	
	private final EmgcService emgcService;
	
	@GetMapping("/api/v1/emgc/mst")
	public ResponseEntity<?> getEmgcMstList(EmgcMstRequestDto emgcMstRequestDto, Pageable pageable) {
		
		System.out.println(emgcMstRequestDto.toString());
		
		// Page를 그대로 전달하는건 적합하지 않음 DTO로 감싸서 보내자 CustomDto를 만들기
		Page<?> mstList = emgcService.getEmgcMstList(emgcMstRequestDto, pageable); 
		
		return ResponseEntity.ok(mstList);
	}
	
	@GetMapping("/api/v1/emgc/dtl/{hpId}")
	public ResponseEntity<?> getMethodName(@PathVariable("hpId") String hpId) {
		
		EmgcDtlResponseDto dtl = emgcService.getEmgcDtl(hpId);
		
		return ResponseEntity.ok(dtl);
	}
	
}
