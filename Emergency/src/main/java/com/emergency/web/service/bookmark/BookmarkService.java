package com.emergency.web.service.bookmark;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergency.web.auth.PrincipalDetails;
import com.emergency.web.dto.request.bookmark.BookmarkDelRequestDto;
import com.emergency.web.dto.request.bookmark.BookmarkInsRequestDto;
import com.emergency.web.exception.GlobalException;
import com.emergency.web.mapper.bookmark.BookmarkMapper;
import com.emergency.web.model.Bookmark;

import lombok.RequiredArgsConstructor;


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
@RequiredArgsConstructor
public class BookmarkService {
	
	private final BookmarkMapper bookmarkMapper;
	
	@Transactional
	public void insertBookmark(BookmarkInsRequestDto bookmarkInsRequestDto) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 인증이 없으면 AnonymousAuthenticationFilter가 작동하여 익명토큰을 자동으로 넣음 authentication.isAuthenticated() 가 true로 세팅되어 조건 추가
		if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		String userId = principal.getUser().getUserId();
		
		Bookmark bookmark = Bookmark.builder()
									.userId(userId)
									.hpId(bookmarkInsRequestDto.getHpId())
									.regAt(LocalDateTime.now())
									.modAt(LocalDateTime.now())
									.build();
		
		int res = bookmarkMapper.insertBookmark(bookmark);
		
		if (res < 1) {
			throw new GlobalException("즐겨찾기에 실패하였습니다. 고객센터에 문의해주세요.", "BOOKMARK_SAVE_FAILED");
		}
		
	}
	
	@Transactional
	public Boolean existsBookmark(String hpId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 인증이 없으면 AnonymousAuthenticationFilter가 작동하여 익명토큰을 자동으로 넣음 authentication.isAuthenticated() 가 true로 세팅되어 조건 추가
		if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		String userId = principal.getUser().getUserId();
		
		Bookmark bookmark = Bookmark.builder()
									.userId(userId)
									.hpId(hpId)
									.build();
		
		int cnt = bookmarkMapper.existsBookmark(bookmark);
		
		boolean exists = false;
		
		if (cnt > 0) {
			exists = true;
		}
		
		return exists;
	}
	
	@Transactional
	public void deleteBookmark(BookmarkDelRequestDto bookmarkDelRequestDto) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 인증이 없으면 AnonymousAuthenticationFilter가 작동하여 익명토큰을 자동으로 넣음 authentication.isAuthenticated() 가 true로 세팅되어 조건 추가
		if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			throw new GlobalException("인증되지 않은 사용자입니다.", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		String userId = principal.getUser().getUserId();
		
		Bookmark bookmark = Bookmark.builder()
									.userId(userId)
									.hpId(bookmarkDelRequestDto.getHpId())
									.build();
		
		int res = bookmarkMapper.deleteBookmark(bookmark);
		
		if (res < 1) {
			throw new GlobalException("즐겨찾기 제거에 실패하였습니다. 고객센터에 문의해주세요.", "BOOKMARK_DELETE_FAILED");
		}
	}

}
