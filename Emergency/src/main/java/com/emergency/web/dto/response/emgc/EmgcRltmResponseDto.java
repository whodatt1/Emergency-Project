package com.emergency.web.dto.response.emgc;

import java.util.List;

import com.emergency.web.model.EmgcRltm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : EmgcRltmResponse.java
* @author          : KHK
* @date            : 2025.01.31
* @description     : 응급 가용 인원 API 응답 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.01.31        KHK                최초 생성
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmgcRltmResponseDto {
	
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
	private String hvventisoayn; // 인공호흡기 조산아 가용 여부
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
	private String hv5; // 신경과입원실
	private int hv6; // [중환자실] 신경외과 인원
	private String hv7; // 약물 중환자
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
	private String hv42; // [기타] 분만실
	private String hv43; // [기타] 화상전용처치실
	private int hv60; // 외상소생실 V4 추가
	private int hv61; // 외상환자진료구역 V4 추가
	private String dutyName; // 기관명
	private String dutyTel3;
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
	private int hvs60; // 외상소생실_기준 인원
	private int hvs61; // 외상환자진료구역_기준 인원
	
	@Getter
	@Setter
	public static class Header {
		private String resultCode; // 00 => 성공
		private String resultMsg;
	}
	
	@Getter
	@Setter
	public static class Body {
		private List<Item> items;
		private int numOfRows;
		private int pageNo;
		private int totalCount;
	}
	
	@Getter
	@Setter
	public static class Item {
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
		private String hvventisoayn; // 인공호흡기 조산아 가용 여부
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
		private String hv5; // 신경과입원실
		private int hv6; // [중환자실] 신경외과 인원
		private String hv7; // 약물 중환자
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
		private String hv42; // [기타] 분만실
		private String hv43; // [기타] 화상전용처치실
		private int hv60; // 외상소생실 V4 추가
		private int hv61; // 외상환자진료구역 V4 추가
		private String dutyName; // 기관명
		private String dutyTel3;
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
		private int hvs60; // 외상소생실_기준 인원
		private int hvs61; // 외상환자진료구역_기준 인원
	}
	
	public EmgcRltm toEntity() {
		
		return EmgcRltm.builder()
			    .hpId(hpid)
			    .oldHpId(phpid)
			    .updDate(hvidate)
			    .emgcErsGenBedPsn(hvec)
			    .opPsn(hvoc)
			    .icuNeuroPsn(hvcc)
			    .icuNeoPsn(hvncc)
			    .icuThorSurgPsn(hvccc)
			    .icuGenPsn(hvicc)
			    .iptGenPsn(hvgc)
			    .dutyDocNm(hvdnm)
			    .ctAvailYn(hvctayn)
			    .mriAvailYn(hvmriayn)
			    .angioAvailYn(hvangioayn)
			    .ventiAvailYn(hvventiayn)
			    .ventiPretAvailYn(hvventisoayn)
			    .incuAvailYn(hvincuayn)
			    .crrtAvailYn(hvcrrtayn)
			    .ecmoAvailYn(hvecmoayn)
			    .oxyAvailYn(hvoxyayn)
			    .hypoAvailYn(hvhypoayn)
			    .ambulAvailYn(hvamyn)
			    .dutyDocTel(hv1)
			    .icuMedPsn(hv2)
			    .icuSurgPsn(hv3)
			    .orthoIptPsn(hv4)
			    .neurolInptYn(hv5)
			    .icuNeuroSurgPsn(hv6)
			    .icuDrugYn(hv7)
			    .icuBurnPsn(hv8)
			    .icuTraumaPsn(hv9)
			    .ventiYn(hv10)
			    .incuYn(hv11)
			    .dutyPedDocTel(hv12)
			    .isolareaNegPressBedPsn(hv13)
			    .isolareaGenPressBedPsn(hv14)
			    .pedPressBedPsn(hv15)
			    .pedIsolBedPsn(hv16)
			    .emgcIcuNegPressPsn(hv17)
			    .emgcIcuGenPressPsn(hv18)
			    .emgcIptNegPressPsn(hv19)
			    .emgcIptGenPressPsn(hv21)
			    .infecDdcIcuPsn(hv22)
			    .infecDdcIcuNegPressPsn(hv23)
			    .infecSevereBedPsn(hv24)
			    .infecSemiSevereBedPsn(hv25)
			    .infecMdrateBedPsn(hv26)
			    .chrtIsolPsn(hv27)
			    .pedPsn(hv28)
			    .emgcErsNegPressBedPsn(hv29)
			    .emgcErsGenPressBedPsn(hv30)
			    .emgcIcuPsn(hv31)
			    .icuPedPsn(hv32)
			    .emgcPedIcuPsn(hv33)
			    .icuCardioPsn(hv34)
			    .icuNegPressPsn(hv35)
			    .emgcIptPsn(hv36)
			    .emgcPedIptPsn(hv37)
			    .iptTraumaPsn(hv38)
			    .etcTraumaOpPsn(hv39)
			    .iptPsyClosedPsn(hv40)
			    .iptNegPressPsn(hv41)
			    .etcDlvrRoomYn(hv42)
			    .etcBurnSpctrtYn(hv43)
			    .dutyName(dutyName)
			    .dutyTel(dutyTel3)
			    .genStddPsn(hvs01)
			    .pedStddPsn(hvs02)
			    .emgcErsNegPressStddBedPsn(hvs03)
			    .emgcErsGenPressStddBedPsn(hvs04)
			    .emgcIcuStddPsn(hvs05)
			    .icuMedStddPsn(hvs06)
			    .icuSurgStddPsn(hvs07)
			    .icuNeoStddPsn(hvs08)
			    .icuPedStddPsn(hvs09)
			    .emgcPedIcuStddPsn(hvs10)
			    .icuNeuroStddPsn(hvs11)
			    .icuNeuroSurgStddPsn(hvs12)
			    .icuBurnStddPsn(hvs13)
			    .icuTraumaStddPsn(hvs14)
			    .icuCardioStddPsn(hvs15)
			    .icuThorSurgStddPsn(hvs16)
			    .icuGenStddPsn(hvs17)
			    .icuNegPressStddPsn(hvs18)
			    .emgcIptStddPsn(hvs19)
			    .emgcPedIptStddPsn(hvs20)
			    .iptTraumaStddPsn(hvs21)
			    .etcOpStddPsn(hvs22)
			    .etcTraumaOpStddPsn(hvs23)
			    .iptPsyClosedStddPsn(hvs24)
			    .iptNegPressStddPsn(hvs25)
			    .etcDlvrRoomStddPsn(hvs26)
			    .ctStddPsn(hvs27)
			    .mriStddPsn(hvs28)
			    .angioStddPsn(hvs29)
			    .ventiGenStddPsn(hvs30)
			    .ventiPretStddPsn(hvs31)
			    .incuStddPsn(hvs32)
			    .crrtStddPsn(hvs33)
			    .ecmoStddPsn(hvs34)
			    .coreTempCtrlStddPsn(hvs35)
			    .etcBurnSpctrtRoomStddPsn(hvs36)
			    .oxyStddPsn(hvs37)
			    .iptGenStddPsn(hvs38)
			    .isolareaNegPressStddPsn(hvs46)
			    .isolareaGenPressStddPsn(hvs47)
			    .pedNegPressStddPsn(hvs48)
			    .pedGenPressStddPsn(hvs49)
			    .emgcIcuNegPressStddPsn(hvs50)
			    .emgcIcuGenPressStddPsn(hvs51)
			    .emgcIptNegPressStddPsn(hvs52)
			    .emgcIptGenPressStddPsn(hvs53)
			    .infecDdcIcuStddPsn(hvs54)
			    .infecDdcIcuNegPressStddPsn(hvs55)
			    .infecSevereBedStddPsn(hvs56)
			    .infecSemiSevereBedStddPsn(hvs57)
			    .infecMdrateBedStddPsn(hvs58)
			    .chrtIsolStddCnt(hvs59)
			    .build();
					
	}
	
	@Data
	public static class ApiResponse {
		private Header header;
		private Body body;
		
		// 리퀘스트 성공 여부 반환
		public boolean requestSuccess() {
			
			List<Item> itemList = body.getItems();
			
			if (itemList != null && itemList.size() != 0) {
				return true;
			}
			
			return false;
		}
		
		// 마지막 페이지 여부 반환
		public boolean isLastPage() {
			
			if (!requestSuccess()) {
				return true;
			}
			
			return false;
		}
	}
}
