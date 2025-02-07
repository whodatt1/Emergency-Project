package com.emergency.web.model;

import lombok.Builder;
import lombok.Getter;

/**
 * 
* @packageName     : com.emergency.web.model
* @fileName        : User.java
* @author          : KHK
* @date            : 2024.10.16
* @description     : EmgcRltm VO ( 응급 가용 인원 )
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.10.16        KHK                최초 생성
 */

@Getter
@Builder
public class EmgcRltm {
	
	private long rnum; // 일련번호
	private String hpid; // 기관코드
	private String phpid; // (구) 기관코드
	private String hvidate; // 입력일시
	private int hvec; // 응급실 일반 병상 인원
	private int hvoc; // [기타] 수술실 인원
	private int hvcc; // [중환자실] 신경과 인원
	private int hvncc; // [중환자실] 신생아 인원
	private int hvccc; // [중환자실] 흉부외과 인원
	private int hvicc; // [중환자실] 일반 인원
	private int hvgc; // [입원실] 일반 인원
	private String hvdnm; // 당직의
	private String hvctayn; // CT 가용 여부
	private String hvmriayn; // MRI 가용 여부
	private String hvangioayn; // 혈관촬영기 가용 여부
	private String hvventiayn; // 인공호흡기 가용 여부
	private String hvincuayn; // 인큐베이터 가용 여부
	private String hvcrrtayn; // CRRT 가용 여부
	private String hvecmoayn; // ECMO 가용 여부
	private String hvoxyayn; // 고압 산소 치료기 가용 여부
	private String hvhypoayn; // 중심 체온 조절 유도기 가용 여부
	private String hvamyn; // 구급차 가용 여부
	private String hv1; // 응급실 당직의 직통 연락처
	private int hv2; // [중환자실] 내과 인원
	private int hv3; // [중환자실] 외과 인원
	private int hv4; // 외과입원실 (정형외과) 인원
	private int hv5; // 신경과입원실 인원
	private int hv6; // [중환자실] 신경외과 인원
	private int hv7; // 약물 중환자 인원
	private int hv8; // [중환자실] 화상 인원
	private int hv9; // [중환자실] 외상 인원
	private String hv10; // VENTI(소아) 여부
	private String hv11; // 인큐베이터(보육기) 여부
	private String hv12; // 소아당직의 직통 연락처
	private int hv13; // 격리진료구역 음압격리병상
	private int hv14; // 격리진료구역 일반격리병상
	private int hv15; // 소아 음압 격리 병상
	private int hv16; // 소아 일반 격리 병상
	private int hv17; // [응급전용] 중환자실 음압격리 인원
	private int hv18; // [응급전용] 중환자실 일반격리 인원
	private int hv19; // [응급전용] 입원실 음압격리 인원
	private int hv21; // [응급전용] 입원실 일반격리 인원
	private int hv22; // 감염병 전담병상 중환자실 인원
	private int hv23; // 감염병 전담병상 중환자실 내 음압격리병상 인원
	private int hv24; // [감염] 중증 병상 인원
	private int hv25; // [감염] 준-중증 병상 인원
	private int hv26; // [감염] 중등증 병상 인원
	private int hv27; // 코호트 격리 인원
	private int hv28; // 소아 인원
	private int hv29; // 응급실 음압 격리 병상 인원
	private int hv30; // 응급실 일반 격리 병상 인원
	private int hv31; // [응급전용] 중환자실 인원
	private int hv32; // [중환자실] 소아 인원
	private int hv33; // [응급전용] 소아중환자실 인원
	private int hv34; // [중환자실] 심장내과 인원
	private int hv35; // [중환자실] 음압격리 인원
	private int hv36; // [응급전용] 입원실 인원
	private int hv37; // [응급전용] 소아입원실
	private int hv38; // [입원실] 외상전용 인원
	private int hv39; // [기타] 외상전용 수술실 인원
	private int hv40; // [입원실] 정신과 폐쇄병동 인원
	private int hv41; // [입원실] 음압격리 인원
	private int hv42; // [기타] 분만실 인원
	private int hv43; // [기타] 화상전용처치실 인원
	private String dutyname; // 기관명
	private int hvs01; // 일반_기준 인원
	private int hvs02; // 소아_기준 인원
	private int hvs03; // 응급실 음압 격리 병상_기준 인원
	private int hvs04; // 응급실 일반 격리 병상_기준 인원
	private int hvs05; // [응급전용] 중환자실_기준 인원
	private int hvs06; // [중환자실] 내과_기준 인원
	private int hvs07; // [중환자실] 외과_기준 인원
	private int hvs08; // [중환자실] 신생아_기준 인원
	private int hvs09; // [중환자실] 소아_기준 인원
	private int hvs10; // [응급전용] 소아중환자실_기준 인원
	private int hvs11; // [중환자실] 신경과_기준 인원
	private int hvs12; // [중환자실] 신경외과_기준 인원
	private int hvs13; // [중환자실] 화상_기준 인원
	private int hvs14; // [중환자실] 외상_기준 인원
	private int hvs15; // [중환자실] 심장내과_기준 인원
	private int hvs16; // [중환자실] 흉부외과_기준 인원
	private int hvs17; // [중환자실] 일반_기준 인원
	private int hvs18; // [중환자실] 음압격리_기준 인원
	private int hvs19; // [응급전용] 입원실_기준 인원
	private int hvs20; // [응급전용] 소아입원실_기준 인원
	private int hvs21; // [입원실] 외상전용_기준 인원
	private int hvs22; // [기타] 수술실_기준 인원
	private int hvs23; // [기타] 외상전용 수술실_기준 인원
	private int hvs24; // [입원실] 정신과 폐쇄병동_기준 인원
	private int hvs25; // [입원실] 음압격리_기준 인원
	private int hvs26; // [기타] 분만실_기준 인원
	private int hvs27; // CT_기준 인원
	private int hvs28; // MRI_기준 인원
	private int hvs29; // 혈관촬영기_기준 인원
	private int hvs30; // 인공호흡기 일반_기준 인원
	private int hvs31; // 인공호흡기 조산아_기준
	private int hvs32; // 인큐베이터_기준 인원
	private int hvs33; // CRRT_기준 인원
	private int hvs34; // ECMO_기준 인원
	private int hvs35; // 중심체온조절유도기_기준 인원
	private int hvs36; // [기타] 화상전용처치실_기준 인원
	private int hvs37; // 고압산소치료기_기준 인원
	private int hvs38; // [입원실] 일반_기준 인원
	private int hvs46; // 격리진료구역 음압격리_기준 인원
	private int hvs47; // 격리진료구역 일반격리_기준 인원
	private int hvs48; // 소아음압격리_기준 인원
	private int hvs49; // 소아일반격리_기준 인원
	private int hvs50; // [응급전용] 중환자실 음압격리_기준 인원
	private int hvs51; // [응급전용] 중환자실 일반격리_기준 인원
	private int hvs52; // [응급전용] 입원실 음압격리_기준 인원
	private int hvs53; // [응급전용] 입원실 일반격리_기준 인원
	private int hvs54; // 감염병 전담병상 중환자실_기준 인원
	private int hvs55; // 감염병 전담병상 중환자실 내 음압격리병상_기준 인원
	private int hvs56; // [감염] 중증 병상_기준 인원
	private int hvs57; // [감염] 준-중증 병상_기준 인원
	private int hvs58; // [감염] 중등증 병상_기준 인원
	private int hvs59; // 코호트 격리_기준 인원
}
