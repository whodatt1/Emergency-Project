package com.emergency.web.mapper.bookmark;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.dto.request.bookmark.BookmarkMstRequestDto;
import com.emergency.web.dto.response.emgc.EmgcMstResponseDto;
import com.emergency.web.model.Bookmark;

/**
 * 
* @packageName     : com.emergency.web.mapper.bookmark
* @fileName        : BookmarkMapper.java
* @author          : KHK
* @date            : 2025.06.18
* @description     : BookmarkMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.06.18        KHK                최초 생성
 */

@Mapper
public interface BookmarkMapper {
	
	int insertBookmark(Bookmark bookmark);
	int existsBookmark(Bookmark bookmark);
	int deleteBookmark(Bookmark bookmark);
	List<EmgcMstResponseDto> getBookmarkMstList(BookmarkMstRequestDto bookmarkMstRequestDto);
	int getBookmarkMstListTotalCnt(BookmarkMstRequestDto bookmarkMstRequestDto);
}
