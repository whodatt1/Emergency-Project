package com.emergency.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.Auth;

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
	Auth findById(String id);
	int saveUser(Auth user);
}
