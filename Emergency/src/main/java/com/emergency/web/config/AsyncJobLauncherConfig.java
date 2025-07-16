package com.emergency.web.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 
 * @packageName     : com.emergency.web.config
 * @fileName        : AsyncJobLauncherConfig.java.java
 * @author          : KHK
 * @date            : 2025. 3. 5.
 * @description     : JOB이 시간대가 겹치는 경우가 있어 비동기처리를 위한 설정
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 3. 5.        KIMHK                최초 생성
 */

@Log4j2
@Configuration
@EnableBatchProcessing
public class AsyncJobLauncherConfig {
	
	@Bean
	@Primary // 해당 taskExecutor를 사용
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5); // 동시 실행할 스레드 개수
		executor.setMaxPoolSize(10); // 최대 실행 가능한 스레드 개수
		executor.setQueueCapacity(50); // 대기 큐 크기
		executor.setThreadNamePrefix("batch-thread-");
		executor.initialize();
		return executor;
	}
	
	@Bean
	@Primary // 해당 jobLauncher를 사용
	public JobLauncher asyncJobLauncher(JobRepository jobRepository, TaskExecutor taskExecutor) {
		TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(taskExecutor);
		try {
			jobLauncher.afterPropertiesSet();
		} catch (Exception e) {
	        log.error("JobLauncher 필수 속성 검증 실패", e);
		}
		return jobLauncher;
	}
}
