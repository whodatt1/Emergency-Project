package com.emergency.web.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 
* @packageName     : com.emergency.web.common.util
* @fileName        : IntegerDeserializer.java
* @author          : KHK
* @date            : 2025.03.26
* @description     : API 받을 시 int 데이터값이 이상하게 들어오는 경우가 
* 					 있어 처리를 위하여 만든 클래스
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.26        KHK       		   최초 생성
 */

public class IntegerDeserializer extends JsonDeserializer<Integer> {

	@Override
	public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		
		try {
			
			return Integer.parseInt(p.getText().trim());
		} catch (NumberFormatException e) {
			// 잘못된 값이 들어오면 0을 세팅
			return 0;
		}
	}
	
}
