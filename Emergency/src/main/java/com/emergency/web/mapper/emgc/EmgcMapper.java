package com.emergency.web.mapper.emgc;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.emergency.web.dto.response.emgc.EmgcDtlResponseDto;
import com.emergency.web.model.EmgcBsIf;
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
public interface EmgcMapper {
	
	List<String> getEmgcRltmHpIdListBeforeBatch(int offset);
	List<EmgcBsIf> getEmgcMstList(Map<String, Object> paramMap);
	int getEmgcMstListCnt(Map<String, Object> paramMap);
	String getEmgcRltmUpdDateByHpId(String hpId);
	EmgcDtlResponseDto getEmgcDtl(String hpId);
}
