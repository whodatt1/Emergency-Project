package com.emergency.web.mapper.emgc;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.EmgcRltm;

/**
 * 
* @packageName     : com.emergency.web.mapper
* @fileName        : EmgcRltmMapper.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : EmgcRltmMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.02.14        KHK                최초 생성
 */

@Mapper
public interface EmgcRltmMapper {
	
	List<EmgcRltm> getEmgcRltmIdAndUpddate();
}
