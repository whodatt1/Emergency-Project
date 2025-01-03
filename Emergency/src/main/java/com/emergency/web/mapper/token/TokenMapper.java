package com.emergency.web.mapper.token;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.RefreshToken;

@Mapper
public interface TokenMapper {
	
	int saveRefreshToken(RefreshToken refreshToken);
	int deleteRefreshTokenByUserId(String id);
	RefreshToken getRefreshTokenByUserId(String id);
	int deleteExpiredRefreshToken();
}
