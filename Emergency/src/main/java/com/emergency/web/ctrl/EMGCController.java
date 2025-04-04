package com.emergency.web.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.request.emgc.BaseInfoRequestDto;

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
public class EMGCController {
	
	
	
	@GetMapping("/api/v1/emgc/mst")
	public ResponseEntity<?> getEmgcMstList(@ModelAttribute BaseInfoRequestDto baseInfoRequestDto) {
		
		
		
		return ResponseEntity.ok(null);
	}
}
