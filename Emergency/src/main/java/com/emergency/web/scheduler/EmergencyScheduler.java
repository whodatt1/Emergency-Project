package com.emergency.web.scheduler;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emergency.web.config.EmgcApiJobConfig;
import com.emergency.web.mapper.token.TokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
* @packageName     : com.emergency.web.scheduler
* @fileName        : EmergencyScheduler.java
* @author          : KHK
* @date            : 2024.12.28
* @description     : 스케쥴러 작업 객체
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.12.28        KHK                최초 생성
 */

@Log4j2
@Component
@RequiredArgsConstructor
public class EmergencyScheduler {
	
	private final TokenMapper tokenMapper;
	
	private final JobLauncher jobLauncher;
	private final EmgcApiJobConfig emgcApiJob;
	
	// 로그인 후 로그인 혹은 로그아웃 과정을 거치지 않아 DB에 남아있는 리프레쉬 토큰 삭제처리
	@Scheduled(cron = "0 0 3 * * ?")
	//@Scheduled(cron = "0 */2 * * * ?") // 테스트
	public void expiredRefreshTokensCleaner() {
		log.info("expiredRefreshTokensCleaner start");
		
		tokenMapper.deleteExpiredRefreshToken();
		
		log.info("expiredRefreshTokensCleaner complete");
	}
	
	// @Scheduled(cron = "0 */1 * * * ?")
	public void excuteEmergencyRealTimeJob() {
		try {
			log.info("excuteEmergencyRealTimeJob start");
			
			jobLauncher.run(emgcApiJob.rltmApiJob()
					,new JobParametersBuilder() // 실행 추적
					.addString("datetime", LocalDateTime.now().toString()) // 배치 처리 기준 시간
					.toJobParameters()
					);
			
			log.info("excuteEmergencyRealTimeJob complete");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Scheduled(cron = "0 */1 * * * ?")
	public void excuteEmergencyBaseInfoJob() {
		
	}
}
