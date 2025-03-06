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
	
	private String hpId; // 기관코드
	private String oldHpId; // (구) 기관코드
	private String updDate; // 입력일시
	private int emgcErsGenBedPsn; // 응급실 일반 병상 인원
	private int opPsn; // [기타] 수술실 인원
	private int icuNeuroPsn; // [중환자실] 신경과 인원
	private int icuNeoPsn; // [중환자실] 신생아 인원
	private int icuThorSurgPsn; // [중환자실] 흉부외과 인원
	private int icuGenPsn; // [중환자실] 일반 인원
	private int iptGenPsn; // [입원실] 일반 인원
	private String dutyDocNm; // 당직의
	private String ctAvailYn; // CT 가용 여부 (Y/N)
	private String mriAvailYn; // MRI 가용 여부 (Y/N)
	private String angioAvailYn; // 혈관촬영기 가용 여부 (Y/N)
	private String ventiAvailYn; // 인공호흡기 가용 여부 (Y/N)
	private String ventiPretAvailYn; // 인공호흡기 조산아 가용 여부 (Y/N)
	private String incuAvailYn; // 인큐베이터 가용 여부 (Y/N)
	private String crrtAvailYn; // CRRT 가용 여부 (Y/N)
	private String ecmoAvailYn; // ECMO 가용 여부 (Y/N)
	private String oxyAvailYn; // 고압 산소 치료기 가용 여부 (Y/N)
	private String hypoAvailYn; // 중심 체온 조절 유도기 가용 여부 (Y/N)
	private String ambulAvailYn; // 구급차 가용 여부 (Y/N)
	private String dutyDocTel; // 응급실 당직의 직통 연락처
	private int icuMedPsn; // [중환자실] 내과 인원
	private int icuSurgPsn; // [중환자실] 외과 인원
	private int orthoIptPsn; // 외과입원실 (정형외과) 인원
	private String neurolInptYn; // 신경과입원실 (Y/N)
	private int icuNeuroSurgPsn; // [중환자실] 신경외과 인원
	private String icuDrugYn; // 약물 중환자 (Y/N)
	private int icuBurnPsn; // [중환자실] 화상 인원
	private int icuTraumaPsn; // [중환자실] 외상 인원
	private String ventiYn; // VENTI(소아) 여부 (Y/N)
	private String incuYn; // 인큐베이터(보육기) 여부 (Y/N)
	private String dutyPedDocTel; // 소아당직의 직통 연락처
	private int isolareaNegPressBedPsn; // 격리진료구역 음압격리병상
	private int isolareaGenPressBedPsn; // 격리진료구역 일반격리병상
	private int pedPressBedPsn; // 소아 음압 격리 병상
	private int pedIsolBedPsn; // 소아 일반 격리 병상
	private int emgcIcuNegPressPsn; // [응급전용] 중환자실 음압격리 인원
	private int emgcIcuGenPressPsn; // [응급전용] 중환자실 일반격리 인원
	private int emgcIptNegPressPsn; // [응급전용] 입원실 음압격리 인원
	private int emgcIptGenPressPsn; // [응급전용] 입원실 일반격리 인원
	private int infecDdcIcuPsn; // 감염병 전담병상 중환자실 인원
	private int infecDdcIcuNegPressPsn; // 감염병 전담병상 중환자실 내 음압격리병상 인원
	private int infecSevereBedPsn; // [감염] 중증 병상 인원
	private int infecSemiSevereBedPsn; // [감염] 준-중증 병상 인원
	private int infecMdrateBedPsn; // [감염] 중등증 병상 인원
	private int chrtIsolPsn; // 코호트 격리 인원
	private int pedPsn; // 소아 인원
	private int emgcErsNegPressBedPsn; // 응급실 음압 격리 병상 인원
	private int emgcErsGenPressBedPsn; // 응급실 일반 격리 병상 인원
	private int emgcIcuPsn; // [응급전용] 중환자실 인원
	private int icuPedPsn; // [중환자실] 소아 인원
	private int emgcPedIcuPsn; // [응급전용] 소아중환자실 인원
	private int icuCardioPsn; // [중환자실] 심장내과 인원
	private int icuNegPressPsn; // [중환자실] 음압격리 인원
	private int emgcIptPsn; // [응급전용] 입원실 인원
	private int emgcPedIptPsn; // [응급전용] 소아입원실
	private int iptTraumaPsn; // [입원실] 외상전용 인원
	private int etcTraumaOpPsn; // [기타] 외상전용 수술실 인원
	private int iptPsyClosedPsn; // [입원실] 정신과 폐쇄병동 인원
	private int iptNegPressPsn; // [입원실] 음압격리 인원
	private String etcDlvrRoomYn; // [기타] 분만실 (Y/N)
	private String etcBurnSpctrtYn; // [기타] 화상전용처치실 (Y/N)
	private int traumaResusPsn; // 외상 소샐실 인원 V4 추가
	private int traumaAreaPsn; // 외상 환자 진료 구역 인원 V4 추가
	private String dutyName; // 기관명
	private String dutyTel; // 응급실 전화
	private int genStddPsn; // 일반_기준 인원
	private int pedStddPsn; // 소아_기준 인원
	private int emgcErsNegPressStddBedPsn; // 응급실 음압 격리 병상_기준 인원
	private int emgcErsGenPressStddBedPsn; // 응급실 일반 격리 병상_기준 인원
	private int emgcIcuStddPsn; // [응급전용] 중환자실_기준 인원
	private int icuMedStddPsn; // [중환자실] 내과_기준 인원
	private int icuSurgStddPsn; // [중환자실] 외과_기준 인원
	private int icuNeoStddPsn; // [중환자실] 신생아_기준 인원
	private int icuPedStddPsn; // [중환자실] 소아_기준 인원
	private int emgcPedIcuStddPsn; // [응급전용] 소아중환자실_기준 인원
	private int icuNeuroStddPsn; // [중환자실] 신경과_기준 인원
	private int icuNeuroSurgStddPsn; // [중환자실] 신경외과_기준 인원
	private int icuBurnStddPsn; // [중환자실] 화상_기준 인원
	private int icuTraumaStddPsn; // [중환자실] 외상_기준 인원
	private int icuCardioStddPsn; // [중환자실] 심장내과_기준 인원
	private int icuThorSurgStddPsn; // [중환자실] 흉부외과_기준 인원
	private int icuGenStddPsn; // [중환자실] 일반_기준 인원
	private int icuNegPressStddPsn; // [중환자실] 음압격리_기준 인원
	private int emgcIptStddPsn; // [응급전용] 입원실_기준 인원
	private int emgcPedIptStddPsn; // [응급전용] 소아입원실_기준 인원
	private int iptTraumaStddPsn; // [입원실] 외상전용_기준 인원
	private int etcOpStddPsn; // [기타] 수술실_기준 인원
	private int etcTraumaOpStddPsn; // [기타] 외상전용 수술실_기준 인원
	private int iptPsyClosedStddPsn; // [입원실] 정신과 폐쇄병동_기준 인원
	private int iptNegPressStddPsn; // [입원실] 음압격리_기준 인원
	private int etcDlvrRoomStddPsn; // [기타] 분만실_기준 인원
	private int ctStddPsn; // CT_기준 인원
	private int mriStddPsn; // MRI_기준 인원
	private int angioStddPsn; // 혈관촬영기_기준 인원
	private int ventiGenStddPsn; // 인공호흡기 일반_기준 인원
	private int ventiPretStddPsn; // 인공호흡기 조산아_기준
	private int incuStddPsn; // 인큐베이터_기준 인원
	private int crrtStddPsn; // CRRT_기준 인원
	private int ecmoStddPsn; // ECMO_기준 인원
	private int coreTempCtrlStddPsn; // 중심체온조절유도기_기준 인원
	private int etcBurnSpctrtRoomStddPsn; // [기타] 화상전용처치실_기준 인원
	private int oxyStddPsn; // 고압산소치료기_기준 인원
	private int iptGenStddPsn; // [입원실] 일반_기준 인원
	private int isolareaNegPressStddPsn; // 격리진료구역 음압격리_기준 인원
	private int isolareaGenPressStddPsn; // 격리진료구역 일반격리_기준 인원
	private int pedNegPressStddPsn; // 소아음압격리_기준 인원
	private int pedGenPressStddPsn; // 소아일반격리_기준 인원
	private int emgcIcuNegPressStddPsn; // [응급전용] 중환자실 음압격리_기준 인원
	private int emgcIcuGenPressStddPsn; // [응급전용] 중환자실 일반격리_기준 인원
	private int emgcIptNegPressStddPsn; // [응급전용] 입원실 음압격리_기준 인원
	private int emgcIptGenPressStddPsn; // [응급전용] 입원실 일반격리_기준 인원
	private int infecDdcIcuStddPsn; // 감염병 전담병상 중환자실_기준 인원
	private int infecDdcIcuNegPressStddPsn; // 감염병 전담병상 중환자실 내 음압격리병상_기준 인원
	private int infecSevereBedStddPsn; // [감염] 중증 병상_기준 인원
	private int infecSemiSevereBedStddPsn; // [감염] 준-중증 병상_기준 인원
	private int infecMdrateBedStddPsn; // [감염] 중등증 병상_기준 인원
	private int chrtIsolStddCnt; // 코호트 격리_기준 인원
	private int traumaResusSttdPsn; // 외상 소생실 인원 V4 추가
	private int traumaAreaSttdPsn; // 외상 환자 진료 구역 인원 V4 추가
}
