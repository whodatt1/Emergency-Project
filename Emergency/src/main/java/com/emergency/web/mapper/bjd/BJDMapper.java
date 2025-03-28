package com.emergency.web.mapper.bjd;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.model.BJD;

/**
 * 
* @packageName     : com.emergency.web.mapper
* @fileName        : BJDMapper.java
* @author          : KHK
* @date            : 2025.03.26
* @description     : BJDMapper
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.26        KHK                최초 생성
 */

@Mapper
public interface BJDMapper {
	
	List<BJD> getSidoList();
	List<BJD> getGugunList(String sidoCd);
	List<BJD> getDongList(String gugunCd);
}
