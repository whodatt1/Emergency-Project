package com.emergency.web.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
