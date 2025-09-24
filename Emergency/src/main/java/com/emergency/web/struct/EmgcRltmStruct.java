package com.emergency.web.struct;

import org.mapstruct.Mapper;
import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;

/**
 * 
* @packageName     : com.emergency.web.struct
* @fileName        : EmgcRltmStruct.java
* @author          : KHK
* @date            : 2025.09.19
* @description     : EmgcRltmStruct
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.09.19        KHK                최초 생성
 */

@Mapper(componentModel = "spring")
public interface EmgcRltmStruct {
	EmgcRltmResponseDto toDto(EmgcRltmResponseDto.Item item);
}
