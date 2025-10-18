package com.emergency.web.struct;

import org.mapstruct.Mapper;

import com.emergency.web.dto.response.emgc.EmgcBsIfResponseDto;

/**
 * 
* @packageName     : com.emergency.web.struct
* @fileName        : EmgcBsIfStruct.java
* @author          : KHK
* @date            : 2025.09.19
* @description     : EmgcBsIfStruct
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.09.19        KHK                최초 생성
 */

@Mapper(componentModel = "spring")
public interface EmgcBsIfStruct {
	EmgcBsIfResponseDto toDto(EmgcBsIfResponseDto.Item item);
}
