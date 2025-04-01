package com.emergency.web.ctrl;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.service.bjd.BJDService;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.ctrl
* @fileName        : BJDController.java
* @author          : KHK
* @date            : 2025.03.26
* @description     : 법정동 코드 관리 컨트롤러
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.26        KHK                최초 생성
 */

@RestController
@RequiredArgsConstructor
public class BJDController {
	
	private final BJDService bjdService;
	
	@GetMapping("/api/v1/bjd/sido")
	public ResponseEntity<?> getSidoList() {
		return ResponseEntity.ok(bjdService.getSidoList());
	}
	
	@GetMapping("/api/v1/bjd/gugun/{sidoCd}")
	public ResponseEntity<?> getGugunList(@PathVariable("sidoCd") String sidoCd) {
		return ResponseEntity.ok(bjdService.getGugunList(sidoCd));
	}
	
	@GetMapping("/api/v1/bjd/dong/{gugunCd}")
	public ResponseEntity<?> getDongList(@PathVariable("gugunCd") String gugunCd) {
		return ResponseEntity.ok(bjdService.getDongList(gugunCd));
	}
}
