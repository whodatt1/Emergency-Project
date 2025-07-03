package com.emergency.web.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.emergency.web.dto.response.emgc.EmgcBsIfResponseDto;
import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;
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
				// 입력타입과 출력타입
				.<List<EmgcRltmResponseDto>, List<EmgcRltm>>chunk(1, transactionManager)
				.reader(emgcRltmItemReader)
				.processor(emgcRltmItemProcessor)
				.writer(emgcRltmItemWriter())
				.build();
	}
	
	@Bean
	Step bsIfApiStep(JobRepository jobRepository) {
		return new StepBuilder("bsIfApiStep", jobRepository)
				// 입력타입과 출력타입
				.<List<EmgcBsIfResponseDto>, List<EmgcBsIf>>chunk(1, transactionManager)
				.reader(emgcBsIfItemReader)
				.processor(egmcBsIfItemProcessor)
				.writer(emgcBsIfItemWriter())
				.build();
	}
	
	public EmgcRltmItemWriter<EmgcRltm> emgcRltmItemWriter() {
		JdbcBatchItemWriter<EmgcRltm> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setJdbcTemplate(new NamedParameterJdbcTemplate(dataSource));
		return new EmgcRltmItemWriter<>(writer, emgcMapper, applicationEventPublisher);
	}
	
	public EmgcBsIfItemWriter<EmgcBsIf> emgcBsIfItemWriter() {
		JdbcBatchItemWriter<EmgcBsIf> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setJdbcTemplate(new NamedParameterJdbcTemplate(dataSource));
		return new EmgcBsIfItemWriter<>(writer);
	}
}
