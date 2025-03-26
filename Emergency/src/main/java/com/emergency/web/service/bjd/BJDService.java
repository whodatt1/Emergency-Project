package com.emergency.web.service.bjd;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emergency.web.mapper.bjd.BJDMapper;
import com.emergency.web.model.BJD;

import lombok.RequiredArgsConstructor;

/**
 * 
* @packageName     : com.emergency.web.service.bjd
* @fileName        : BJDService.java
* @author          : KHK
* @date            : 2025.03.26
* @description     :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.26        KHK                최초 생성
 */

@Service
@RequiredArgsConstructor
public class BJDService {
	
	private final BJDMapper bjdMapper;
	
	public List<BJD> getSidoList() {
//		return bjdMapper.getSidoList();
		return null;
	}
	
	public List<BJD> getGugunList() {
//		return bjdMapper.getGugunList();
		return null;
	}
	
	public List<BJD> getDongList() {
//		return bjdMapper.getDongList();
		return null;
	}
}
