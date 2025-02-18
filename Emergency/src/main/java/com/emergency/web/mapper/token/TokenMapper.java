package com.emergency.web.mapper.token;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.RefreshToken;

/**
 * 
* @packageName     : com.emergency.web.mapper
* @fileName        : TokenMapper.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : TokenMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Mapper
public interface TokenMapper {
	
	int saveRefreshToken(RefreshToken refreshToken);
	int deleteRefreshTokenByUserId(String id);
	RefreshToken getRefreshTokenByUserId(String id);
	int deleteExpiredRefreshToken();
}
