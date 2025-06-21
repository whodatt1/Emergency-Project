package com.emergency.web.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.web.dto.request.bookmark.BookmarkDelRequestDto;
import com.emergency.web.dto.request.bookmark.BookmarkExistRequestDto;
import com.emergency.web.dto.request.bookmark.BookmarkInsRequestDto;
import com.emergency.web.service.bookmark.BookmarkService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.ctrl
* @fileName        : BookmarkController.java
* @author          : KHK
* @date            : 2025.05.07
* @description     : 북마크 관리 컨트롤러
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.05.07        KHK                최초 생성
 */

@RestController
@RequiredArgsConstructor
public class BookmarkController {
	
	private final BookmarkService bookmarkService;
	
	@PostMapping("/api/v1/bookmark/ins")
	public ResponseEntity<?> insertBookmark(@RequestBody BookmarkInsRequestDto bookmarkInsRequestDto) {
		
		bookmarkService.insertBookmark(bookmarkInsRequestDto);
		
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/api/v1/bookmark/exists/{hpId}")
	public ResponseEntity<?> existsBookmark(@PathVariable("hpId") String hpId) {
		
		Boolean exists = bookmarkService.existsBookmark(hpId);
		
		return ResponseEntity.ok(exists);
	}
	
	@PostMapping("/api/v1/bookmark/del")
	public ResponseEntity<?> deleteBookmark(@RequestBody BookmarkDelRequestDto bookmarkDelRequestDto) {
		
		bookmarkService.deleteBookmark(bookmarkDelRequestDto);
		
		return ResponseEntity.ok(null);
	}
}
