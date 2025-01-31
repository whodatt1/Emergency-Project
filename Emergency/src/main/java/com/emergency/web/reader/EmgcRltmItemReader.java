package com.emergency.web.reader;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.web.reactive.function.client.WebClient;

import com.emergency.web.config.TypeSafeProperties;
import com.emergency.web.dto.response.emgc.EmgcRltmResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmgcRltmItemReader implements ItemReader<List<EmgcRltmResponse>> {
	
	private final WebClient webClient;
	
	private final TypeSafeProperties typeSafeProperties;
	
	private int currPage = 1;
	
	@Override
	public List<EmgcRltmResponse> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		return null;
	}
	 
	
}
