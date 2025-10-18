package com.emergency.web.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.emergency.web.dto.response.emgc.EmgcBsIfResponseDto;
import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;
import com.emergency.web.kafka.KafkaProducer;
import com.emergency.web.mapper.emgc.EmgcMapper;
import com.emergency.web.model.EmgcBsIf;
import com.emergency.web.model.EmgcRltm;
import com.emergency.web.processor.EmgcBsIfItemProcessor;
import com.emergency.web.processor.EmgcRltmItemProcessor;
import com.emergency.web.reader.EmgcBsIfItemReader;
import com.emergency.web.reader.EmgcRltmItemReader;
import com.emergency.web.writer.EmgcBsIfItemWriter;
import com.emergency.web.writer.EmgcRltmItemWriter;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @packageName     : com.emergency.web.config
 * @fileName        : ApiJobConfig.java
 * @author          : KHK
 * @date            : 2025. 3. 5.
 * @description     : JOB 정의
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 3. 5.        KIMHK             최초 생성
 */

@Configuration
@RequiredArgsConstructor
public class ApiJobConfig {
	
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	
	// EmgcRltm
	private final EmgcRltmItemReader emgcRltmItemReader;
	private final EmgcRltmItemProcessor emgcRltmItemProcessor;
	 
	// EmgcBsIf
	private final EmgcBsIfItemReader emgcBsIfItemReader;
	private final EmgcBsIfItemProcessor egmcBsIfItemProcessor;
	
	// event
	private final EmgcMapper emgcMapper;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	private final DataSource dataSource;
	
	@Bean
	public Job rltmApiJob() {
		
		return new JobBuilder("rltmApiJob", jobRepository)
				.start(rltmApiStep(jobRepository))
				.build();
		
	}
	
	@Bean
	public Job bsIfApiJob() {
		
		return new JobBuilder("bsIfApiJob", jobRepository)
				.start(bsIfApiStep(jobRepository))
				.build();
		
	}
	
	@Bean
	Step rltmApiStep(JobRepository jobRepository) {
		return new StepBuilder("rltmApiStep", jobRepository)
				// 기존 코드
				//.<List<EmgcRltmResponseDto>, List<EmgcRltm>>chunk(1, transactionManager)
				// 변경된 코드
				.<EmgcRltmResponseDto, EmgcRltm>chunk(500, transactionManager)
				.reader(emgcRltmItemReader)
				.processor(emgcRltmItemProcessor)
				.writer(emgcRltmItemWriter())
				.build();
	}
	
	@Bean
	Step bsIfApiStep(JobRepository jobRepository) {
		return new StepBuilder("bsIfApiStep", jobRepository)
				// 입력타입과 출력타입
				.<EmgcBsIfResponseDto, EmgcBsIf>chunk(500, transactionManager)
				.reader(emgcBsIfItemReader)
				.processor(egmcBsIfItemProcessor)
				.writer(emgcBsIfItemWriter())
				.build();
	}
	
	private <T> JdbcBatchItemWriter<T> createJdbcWriter() {
	    JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
	    writer.setDataSource(dataSource);
	    writer.setJdbcTemplate(new NamedParameterJdbcTemplate(dataSource));
	    return writer;
	}
	
	@Bean
	@StepScope
	public EmgcRltmItemWriter<EmgcRltm> emgcRltmItemWriter() {
		
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
				+ " infec_mdrate_bed_stdd_psn, chrt_isol_stdd_cnt, trauma_resus_psn, trauma_area_psn, trauma_resus_sttd_psn, trauma_area_sttd_psn "
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
				+ " :infecMdrateBedStddPsn, :chrtIsolStddCnt, :traumaResusPsn, :traumaAreaPsn, :traumaResusSttdPsn, :traumaAreaSttdPsn "
				+ ") "
				+ "ON DUPLICATE KEY UPDATE "
				+ " hp_id = :hpId, old_hp_id = :oldHpId, upd_date = :updDate, emgc_ers_gen_bed_psn = :emgcErsGenBedPsn, "
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
				+ " chrt_isol_stdd_cnt = :chrtIsolStddCnt, trauma_resus_psn = :traumaResusPsn, trauma_area_psn = :traumaAreaPsn, "
				+ " trauma_resus_sttd_psn = :traumaResusSttdPsn, trauma_area_sttd_psn = :traumaAreaSttdPsn ";

		
		JdbcBatchItemWriter<EmgcRltm> writer = createJdbcWriter();
	    writer.setSql(sql);
	    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
	    writer.afterPropertiesSet();
		
	    return new EmgcRltmItemWriter<>(writer, emgcMapper, applicationEventPublisher);
	}

	@Bean
	public EmgcBsIfItemWriter<EmgcBsIf> emgcBsIfItemWriter() {
		String sql = "INSERT INTO tb_emgc_rltm_master "
				+ "( "
				+ " hp_id, duty_name, duty_ha_yn, duty_er_yn,post_cdn1, post_cdn2, duty_addr, duty_tel, "
				+ " duty_er_tel, duty_inf, duty_time_1c, duty_time_2c, duty_time_3c, duty_time_4c, duty_time_5c, "
				+ " duty_time_6c, duty_time_7c, duty_time_8c, duty_time_1s, duty_time_2s, duty_time_3s, "
				+ " duty_time_4s, duty_time_5s, duty_time_6s, duty_time_7s, duty_time_8s, duty_lon, "
				+ " duty_lat, dgid_id_name"
				+ ") "
				+ "VALUES "
				+ "( "
				+ " :hpId, :dutyName, :dutyHayn, :dutyEryn, :postCdn1, :postCdn2, :dutyAddr, :dutyTel, "
				+ " :dutyErTel, :dutyInf, :dutyTime1c, :dutyTime2c, :dutyTime3c, :dutyTime4c, :dutyTime5c, :dutyTime6c, "
				+ " :dutyTime7c, :dutyTime8c, :dutyTime1s, :dutyTime2s, :dutyTime3s, :dutyTime4s, :dutyTime5s, "
				+ " :dutyTime6s, :dutyTime7s, :dutyTime8s, :dutyLon, :dutyLat, :dgidIdName"
				+ ") "
				+ "ON DUPLICATE KEY UPDATE "
				+ " hp_id = :hpId, duty_name = :dutyName, duty_ha_yn = :dutyHayn, duty_er_yn = :dutyEryn, "
				+ " post_cdn1 = :postCdn1, post_cdn2 = :postCdn2, duty_addr = :dutyAddr, duty_tel = :dutyTel, duty_er_tel = :dutyErTel, "
				+ " duty_inf = :dutyInf, duty_time_1c = :dutyTime1c, duty_time_2c = :dutyTime2c, duty_time_3c = :dutyTime3c, "
				+ " duty_time_4c = :dutyTime4c, duty_time_5c = :dutyTime5c, duty_time_6c = :dutyTime6c, duty_time_7c = :dutyTime7c, "
				+ " duty_time_8c = :dutyTime8c, duty_time_1s = :dutyTime1s, duty_time_2s = :dutyTime2s, duty_time_3s = :dutyTime3s, "
				+ " duty_time_4s = :dutyTime4s, duty_time_5s = :dutyTime5s, duty_time_6s = :dutyTime6s, duty_time_7s = :dutyTime7s, "
				+ " duty_time_8s = :dutyTime8s, duty_lon = :dutyLon, duty_lat = :dutyLat, "
				+ " dgid_id_name = :dgidIdName ";
		
		JdbcBatchItemWriter<EmgcBsIf> writer = createJdbcWriter();
	    writer.setSql(sql);
	    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
	    writer.afterPropertiesSet();
		
	    return new EmgcBsIfItemWriter<>(writer);
	}
	
}
