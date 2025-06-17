package com.emergency.web.service.bookmark;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.emergency.web.auth.PrincipalDetails;
import com.emergency.web.dto.request.bookmark.BookmarkInsRequestDto;
import com.emergency.web.exception.GlobalException;


/**
 * 
* @packageName     : com.emergency.web.service.bookmark
* @fileName        : BookmarkService.java
* @author          : KHK
* @date            : 2025.06.17
* @description     :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.17        KHK                최초 생성
 */

@Service
public class BookmarkService {

	public void insertBookmark(BookmarkInsRequestDto bookmarkInsRequestDto) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 인증이 없으면 AnonymousAuthenticationFilter가 작동하여 익명토큰을 자동으로 넣음 authentication.isAuthenticated() 가 true로 세팅되어 조건 추가
		if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		String userId = principal.getUser().getUserId();
		
		
		
	}

}
