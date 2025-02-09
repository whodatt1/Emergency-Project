package com.emergency.web.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;
import com.emergency.web.model.EmgcRltm;
import com.emergency.web.processor.EmgcRltmItemProcessor;
import com.emergency.web.reader.EmgcRltmItemReader;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class EmgcApiJobConfig {
	
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	
	private final EmgcRltmItemReader emgcRltmItemReader;
	private final EmgcRltmItemProcessor emgcRltmItemProcessor;
	
	@Bean
	public Job rltmApiJob() {
		
		return new JobBuilder("rltmApiJob", jobRepository)
				.start(rltmApiStep(jobRepository))
				.build();
		
	}
	
	@Bean
	public Job bslfApiJob() {
		
		return null;
		
	}
	
	@Bean
	Step rltmApiStep(JobRepository jobRepository) {
		return new StepBuilder("rltmApiStep", jobRepository)
				.<List<EmgcRltmResponseDto>, List<EmgcRltm>>chunk(1, transactionManager)
				.reader(emgcRltmItemReader)
				.processor(emgcRltmItemProcessor)
				.writer(items -> items.forEach(item -> System.out.println("테스트!! : " + item.toString())))
				.build();
	}
	
}
