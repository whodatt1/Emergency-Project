package com.emergency.web.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : BJD.java
* @author          : KHK
* @date            : 2025.03.19
* @description     : BJD VO ( 법정동 )
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.03.19        KHK                최초 생성
 */

@Getter
@Builder
public class BJD {
	
    private String pastCd;
    private String riNm;
    private Long lawCd;
    private LocalDate deletedDate;
    private LocalDate createdDate;
    private int rank;
    private String siNm;
    private String sidoNm;
    private String dongNm;
}
