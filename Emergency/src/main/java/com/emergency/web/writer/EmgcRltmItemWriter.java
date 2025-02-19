package com.emergency.web.writer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

/**
 * 
* @packageName     : com.emergency.web.config
* @fileName        : EmgcRltmItemWriter.java
* @author          : KHK
* @date            : 2025.02.09
* @description     : 배치 작업에서 읽어온 데이터를 저장 및 업데이트
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2025.02.09        KHK                최초 생성
 */

public class EmgcRltmItemWriter<T> implements ItemWriter<List<T>> {
	
	private JdbcBatchItemWriter<T> jdbcBatchItemWriter;
	
	public EmgcRltmItemWriter(JdbcBatchItemWriter<T> jdbcBatchItemWriter) {
		this.jdbcBatchItemWriter = jdbcBatchItemWriter;
	}

	@Override
	public void write(Chunk<? extends List<T>> chunk) throws Exception {
		
		List<T> flattenedItems = chunk.getItems().stream()
                .flatMap(List::stream)  // List<List<T>>를 List<T>로 평탄화
                .collect(Collectors.toList());
		
		String sql = "INSERT INTO tb_emgc_rltm_detail "
				+ "( "
				+ " hp_id, old_hp_id, upd_date, emgc_ers_gen_bed_psn, op_psn, icu_neuro_psn, "
				+ " icu_neo_psn, icu_thor_surg_psn, icu_gen_psn, ipt_gen_psn, duty_doc_nm, "
				+ " ct_avail_yn, mri_avail_yn, angio_avail_yn, venti_avail_yn, venti_pret_avail_yn, "
				+ " incu_avail_yn, crrt_avail_yn, ecmo_avail_yn, oxy_avail_yn, hypo_avail_yn, ambul_avail_yn, "
				+ " duty_doc_tel, icu_med_psn, icu_surg_psn, ortho_ipt_psn, neurol_inpt_yn, icu_neuro_surg_psn, "
				+ " icu_drug_yn, icu_burn_psn, icu_trauma_psn, venti_yn, incu_yn, duty_ped_doc_tel, isolarea_neg_press_bed_psn, "
				+ " isolarea_gen_press_bed_psn, ped_press_bed_psn, ped_isol_bed_psn, emgc_icu_neg_press_psn, "
				+ " emgc_icu_gen_press_psn, emgc_ipt_neg_press_psn, emgc_ipt_gen_press_psn, infec_ddc_icu_psn, "
				+ " infec_ddc_icu_neg_press_psn, infec_severe_bed_psn, infec_semi_severe_bed_psn, infec_mdrate_bed_psn, "
				+ " chrt_isol_psn, ped_psn, emgc_ers_neg_press_bed_psn, emgc_ers_gen_press_bed_psn, emgc_icu_psn, icu_ped_psn, "
				+ " emgc_ped_icu_psn, icu_cardio_psn, icu_neg_press_psn, emgc_ipt_psn, emgc_ped_ipt_psn, ipt_trauma_psn, "
				+ " etc_trauma_op_psn, ipt_psy_closed_psn, ipt_neg_press_psn, etc_dlvr_room_yn, etc_burn_spctrt_yn, duty_name, "
				+ " duty_tel, gen_stdd_psn, ped_stdd_psn, emgc_ers_neg_press_stdd_bed_psn, emgc_ers_gen_press_stdd_bed_psn, "
				+ " emgc_icu_stdd_psn, icu_med_stdd_psn, icu_surg_stdd_psn, icu_neo_stdd_psn, icu_ped_stdd_psn, "
				+ " emgc_ped_icu_stdd_psn, icu_neuro_stdd_psn, icu_neuro_surg_stdd_psn, icu_burn_stdd_psn, icu_trauma_stdd_psn, "
				+ " icu_cardio_stdd_psn, icu_thor_surg_stdd_psn, icu_gen_stdd_psn, icu_neg_press_stdd_psn, emgc_ipt_stdd_psn, "
				+ " emgc_ped_ipt_stdd_psn, ipt_trauma_stdd_psn, etc_op_stdd_psn, etc_trauma_op_stdd_psn, ipt_psy_closed_stdd_psn, "
				+ " ipt_neg_press_stdd_psn, etc_dlvr_room_stdd_psn, ct_stdd_psn, mri_stdd_psn, angio_stdd_psn, venti_gen_stdd_psn, "
				+ " venti_pret_stdd_psn, incu_stdd_psn, crrt_stdd_psn, ecmo_stdd_psn, core_temp_ctrl_stdd_psn, "
				+ " etc_burn_spctrt_room_stdd_psn, oxy_stdd_psn, ipt_gen_stdd_psn, isolarea_neg_press_stdd_psn, "
				+ " isolarea_gen_press_stdd_psn, ped_neg_press_stdd_psn, ped_gen_press_stdd_psn, emgc_icu_neg_press_stdd_psn, "
				+ " emgc_icu_gen_press_stdd_psn, emgc_ipt_neg_press_stdd_psn, emgc_ipt_gen_press_stdd_psn, infec_ddc_icu_stdd_psn, "
				+ " infec_ddc_icu_neg_press_stdd_psn, infec_severe_bed_stdd_psn, infec_semi_severe_bed_stdd_psn, "
				+ " infec_mdrate_bed_stdd_psn, chrt_isol_stdd_cnt "
				+ ") "
				+ "VALUES "
				+ "( "
				+ " :hpId, :oldHpId, :updDate, :emgcErsGenBedPsn, :opPsn, :icuNeuroPsn, "
				+ " :icuNeoPsn, :icuThorSurgPsn, :icuGenPsn, :iptGenPsn, :dutyDocNm, "
				+ " :ctAvailYn, :mriAvailYn, :angioAvailYn, :ventiAvailYn, :ventiPretAvailYn, "
				+ " :incuAvailYn, :crrtAvailYn, :ecmoAvailYn, :oxyAvailYn, :hypoAvailYn, :ambulAvailYn, "
				+ " :dutyDocTel, :icuMedPsn, :icuSurgPsn, :orthoIptPsn, :neurolInptYn, :icuNeuroSurgPsn, "
				+ " :icuDrugYn, :icuBurnPsn, :icuTraumaPsn, :ventiYn, :incuYn, :dutyPedDocTel, :isolareaNegPressBedPsn, "
				+ " :isolareaGenPressBedPsn, :pedPressBedPsn, :pedIsolBedPsn, :emgcIcuNegPressPsn, "
				+ " :emgcIcuGenPressPsn, :emgcIptNegPressPsn, :emgcIptGenPressPsn, :infecDdcIcuPsn, "
				+ " :infecDdcIcuNegPressPsn, :infecSevereBedPsn, :infecSemiSevereBedPsn, :infecMdrateBedPsn, "
				+ " :chrtIsolPsn, :pedPsn, :emgcErsNegPressBedPsn, :emgcErsGenPressBedPsn, :emgcIcuPsn, :icuPedPsn, "
				+ " :emgcPedIcuPsn, :icuCardioPsn, :icuNegPressPsn, :emgcIptPsn, :emgcPedIptPsn, :iptTraumaPsn, "
				+ " :etcTraumaOpPsn, :iptPsyClosedPsn, :iptNegPressPsn, :etcDlvrRoomYn, :etcBurnSpctrtYn, :dutyName, "
				+ " :dutyTel, :genStddPsn, :pedStddPsn, :emgcErsNegPressStddBedPsn, :emgcErsGenPressStddBedPsn, "
				+ " :emgcIcuStddPsn, :icuMedStddPsn, :icuSurgStddPsn, :icuNeoStddPsn, :icuPedStddPsn, "
				+ " :emgcPedIcuStddPsn, :icuNeuroStddPsn, :icuNeuroSurgStddPsn, :icuBurnStddPsn, :icuTraumaStddPsn, "
				+ " :icuCardioStddPsn, :icuThorSurgStddPsn, :icuGenStddPsn, :icuNegPressStddPsn, :emgcIptStddPsn, "
				+ " :emgcPedIptStddPsn, :iptTraumaStddPsn, :etcOpStddPsn, :etcTraumaOpStddPsn, :iptPsyClosedStddPsn, "
				+ " :iptNegPressStddPsn, :etcDlvrRoomStddPsn, :ctStddPsn, :mriStddPsn, :angioStddPsn, :ventiGenStddPsn, "
				+ " :ventiPretStddPsn, :incuStddPsn, :crrtStddPsn, :ecmoStddPsn, :coreTempCtrlStddPsn, "
				+ " :etcBurnSpctrtRoomStddPsn, :oxyStddPsn, :iptGenStddPsn, :isolareaNegPressStddPsn, "
				+ " :isolareaGenPressStddPsn, :pedNegPressStddPsn, :pedGenPressStddPsn, :emgcIcuNegPressStddPsn, "
				+ " :emgcIcuGenPressStddPsn, :emgcIptNegPressStddPsn, :emgcIptGenPressStddPsn, :infecDdcIcuStddPsn, "
				+ " :infecDdcIcuNegPressStddPsn, :infecSevereBedStddPsn, :infecSemiSevereBedStddPsn, "
				+ " :infecMdrateBedStddPsn, :chrtIsolStddCnt "
				+ ") "
				+ "ON DUPLICATE KEY UPDATE "
				+ " old_hp_id = :oldHpId, upd_date = :updDate, emgc_ers_gen_bed_psn = :emgcErsGenBedPsn, "
				+ " op_psn = :opPsn, icu_neuro_psn = :icuNeuroPsn, icu_neo_psn = :icuNeoPsn, "
				+ " icu_thor_surg_psn = :icuThorSurgPsn, icu_gen_psn = :icuGenPsn, ipt_gen_psn = :iptGenPsn, "
				+ " duty_doc_nm = :dutyDocNm, ct_avail_yn = :ctAvailYn, mri_avail_yn = :mriAvailYn, "
				+ " angio_avail_yn = :angioAvailYn, venti_avail_yn = :ventiAvailYn, venti_pret_avail_yn = :ventiPretAvailYn, "
				+ " incu_avail_yn = :incuAvailYn, crrt_avail_yn = :crrtAvailYn, ecmo_avail_yn = :ecmoAvailYn, "
				+ " oxy_avail_yn = :oxyAvailYn, hypo_avail_yn = :hypoAvailYn, ambul_avail_yn = :ambulAvailYn, "
				+ " duty_doc_tel = :dutyDocTel, icu_med_psn = :icuMedPsn, icu_surg_psn = :icuSurgPsn, "
				+ " ortho_ipt_psn = :orthoIptPsn, neurol_inpt_yn = :neurolInptYn, icu_neuro_surg_psn = :icuNeuroSurgPsn, "
				+ " icu_drug_yn = :icuDrugYn, icu_burn_psn = :icuBurnPsn, icu_trauma_psn = :icuTraumaPsn, "
				+ " venti_yn = :ventiYn, incu_yn = :incuYn, duty_ped_doc_tel = :dutyPedDocTel, "
				+ " isolarea_neg_press_bed_psn = :isolareaNegPressBedPsn, isolarea_gen_press_bed_psn = :isolareaGenPressBedPsn, "
				+ " ped_press_bed_psn = :pedPressBedPsn, ped_isol_bed_psn = :pedIsolBedPsn, "
				+ " emgc_icu_neg_press_psn = :emgcIcuNegPressPsn, emgc_icu_gen_press_psn = :emgcIcuGenPressPsn, "
				+ " emgc_ipt_neg_press_psn = :emgcIptNegPressPsn, emgc_ipt_gen_press_psn = :emgcIptGenPressPsn, "
				+ " infec_ddc_icu_psn = :infecDdcIcuPsn, infec_ddc_icu_neg_press_psn = :infecDdcIcuNegPressPsn, "
				+ " infec_severe_bed_psn = :infecSevereBedPsn, infec_semi_severe_bed_psn = :infecSemiSevereBedPsn, "
				+ " infec_mdrate_bed_psn = :infecMdrateBedPsn, chrt_isol_psn = :chrtIsolPsn, ped_psn = :pedPsn, "
				+ " emgc_ers_neg_press_bed_psn = :emgcErsNegPressBedPsn, emgc_ers_gen_press_bed_psn = :emgcErsGenPressBedPsn, "
				+ " emgc_icu_psn = :emgcIcuPsn, icu_ped_psn = :icuPedPsn, emgc_ped_icu_psn = :emgcPedIcuPsn, "
				+ " icu_cardio_psn = :icuCardioPsn, icu_neg_press_psn = :icuNegPressPsn, emgc_ipt_psn = :emgcIptPsn, "
				+ " emgc_ped_ipt_psn = :emgcPedIptPsn, ipt_trauma_psn = :iptTraumaPsn, etc_trauma_op_psn = :etcTraumaOpPsn, "
				+ " ipt_psy_closed_psn = :iptPsyClosedPsn, ipt_neg_press_psn = :iptNegPressPsn, "
				+ " etc_dlvr_room_yn = :etcDlvrRoomYn, etc_burn_spctrt_yn = :etcBurnSpctrtYn, duty_name = :dutyName, "
				+ " duty_tel = :dutyTel, gen_stdd_psn = :genStddPsn, ped_stdd_psn = :pedStddPsn, "
				+ " emgc_ers_neg_press_stdd_bed_psn = :emgcErsNegPressStddBedPsn, emgc_ers_gen_press_stdd_bed_psn = :emgcErsGenPressStddBedPsn, "
				+ " emgc_icu_stdd_psn = :emgcIcuStddPsn, icu_med_stdd_psn = :icuMedStddPsn, icu_surg_stdd_psn = :icuSurgStddPsn, "
				+ " icu_neo_stdd_psn = :icuNeoStddPsn, icu_ped_stdd_psn = :icuPedStddPsn, "
				+ " emgc_ped_icu_stdd_psn = :emgcPedIcuStddPsn, icu_neuro_stdd_psn = :icuNeuroStddPsn, "
				+ " icu_neuro_surg_stdd_psn = :icuNeuroSurgStddPsn, icu_burn_stdd_psn = :icuBurnStddPsn, "
				+ " icu_trauma_stdd_psn = :icuTraumaStddPsn, icu_cardio_stdd_psn = :icuCardioStddPsn, "
				+ " icu_thor_surg_stdd_psn = :icuThorSurgStddPsn, icu_gen_stdd_psn = :icuGenStddPsn, "
				+ " icu_neg_press_stdd_psn = :icuNegPressStddPsn, emgc_ipt_stdd_psn = :emgcIptStddPsn, "
				+ " emgc_ped_ipt_stdd_psn = :emgcPedIptStddPsn, ipt_trauma_stdd_psn = :iptTraumaStddPsn, "
				+ " etc_op_stdd_psn = :etcOpStddPsn, etc_trauma_op_stdd_psn = :etcTraumaOpStddPsn, "
				+ " ipt_psy_closed_stdd_psn = :iptPsyClosedStddPsn, ipt_neg_press_stdd_psn = :iptNegPressStddPsn, "
				+ " etc_dlvr_room_stdd_psn = :etcDlvrRoomStddPsn, ct_stdd_psn = :ctStddPsn, "
				+ " mri_stdd_psn = :mriStddPsn, angio_stdd_psn = :angioStddPsn, venti_gen_stdd_psn = :ventiGenStddPsn, "
				+ " venti_pret_stdd_psn = :ventiPretStddPsn, incu_stdd_psn = :incuStddPsn, "
				+ " crrt_stdd_psn = :crrtStddPsn, ecmo_stdd_psn = :ecmoStddPsn, core_temp_ctrl_stdd_psn = :coreTempCtrlStddPsn, "
				+ " etc_burn_spctrt_room_stdd_psn = :etcBurnSpctrtRoomStddPsn, oxy_stdd_psn = :oxyStddPsn, "
				+ " ipt_gen_stdd_psn = :iptGenStddPsn, isolarea_neg_press_stdd_psn = :isolareaNegPressStddPsn, "
				+ " isolarea_gen_press_stdd_psn = :isolareaGenPressStddPsn, ped_neg_press_stdd_psn = :pedNegPressStddPsn, "
				+ " ped_gen_press_stdd_psn = :pedGenPressStddPsn, emgc_icu_neg_press_stdd_psn = :emgcIcuNegPressStddPsn, "
				+ " emgc_icu_gen_press_stdd_psn = :emgcIcuGenPressStddPsn, emgc_ipt_neg_press_stdd_psn = :emgcIptNegPressStddPsn, "
				+ " emgc_ipt_gen_press_stdd_psn = :emgcIptGenPressStddPsn, infec_ddc_icu_stdd_psn = :infecDdcIcuStddPsn, "
				+ " infec_ddc_icu_neg_press_stdd_psn = :infecDdcIcuNegPressStddPsn, infec_severe_bed_stdd_psn = :infecSevereBedStddPsn, "
				+ " infec_semi_severe_bed_stdd_psn = :infecSemiSevereBedStddPsn, infec_mdrate_bed_stdd_psn = :infecMdrateBedStddPsn, "
				+ " chrt_isol_stdd_cnt = :chrtIsolStddCnt";

		
		jdbcBatchItemWriter.setSql(sql);
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		jdbcBatchItemWriter.afterPropertiesSet();
		jdbcBatchItemWriter.write(new Chunk<>(flattenedItems));
	}
	
}
