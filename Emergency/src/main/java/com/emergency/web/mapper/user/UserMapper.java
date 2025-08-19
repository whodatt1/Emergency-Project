package com.emergency.web.mapper.user;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.User;

/**
 * 
* @packageName     : com.emergency.web.mapper
* @fileName        : UserMapper.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : UserMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Mapper
public interface UserMapper {
	User findById(String id);
	int saveUser(User user);
	int updateLastLogin(String id);
	int modMe(User user);
}
