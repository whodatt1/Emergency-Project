package com.emergency.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.User;

@Mapper
public interface UserMapper {
	User findById(String id);
}
