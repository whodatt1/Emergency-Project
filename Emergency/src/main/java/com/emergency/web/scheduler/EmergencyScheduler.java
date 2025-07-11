package com.emergency.web.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.kafka.common.Uuid;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emergency.web.config.ApiJobConfig;
import com.emergency.web.mapper.fcm.FcmMapper;
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
	private final FcmMapper fcmMapper;
	
	private final JobLauncher jobLauncher;
	private final JobExplorer jobExplorer;
	
	private final ApiJobConfig apiJobConfig;
	
	// 로그인 후 로그인 혹은 로그아웃 과정을 거치지 않아 DB에 남아있는 리프레쉬 토큰 삭제처리
	@Scheduled(cron = "0 0 3 * * ?")
	//@Scheduled(cron = "0 */2 * * * ?") // 테스트
	public void expiredRefreshTokensCleaner() {
		log.info("expiredRefreshTokensCleaner start");
		
		// 만료된 유저 아이디
		List<String> expiredUserIds = tokenMapper.getUserIdsWithExpiredRefreshToken();
		
		tokenMapper.deleteExpiredRefreshToken();
		
		// 만료된 유저의 fcm 토큰도 삭제
		for (String userId : expiredUserIds) {
			fcmMapper.deleteFcmInfoByUserId(userId);
		}
		
		log.info("expiredRefreshTokensCleaner complete");
	}
	
	@Async
	@Scheduled(fixedRate = 60000)
//	@Scheduled(cron = "0 5 23 * * ?")
	public void excuteEmergencyRealTimeJob() {
		if (isJobRunning("rltmApiJob")) {
			log.warn("이전 배치가 실행 중입니다. 현재 배치를 건너뜁니다.");
			return;
		}
		
		try {
			log.info("excuteEmergencyRealTimeJob start");
			
			// 한 배치 통합 id 생성
	        String batchId = Uuid.randomUuid().toString();
			
			jobLauncher.run(apiJobConfig.rltmApiJob()
					,new JobParametersBuilder() // 실행 추적
					.addString("batchId", batchId)
					.addString("datetime", LocalDateTime.now().toString()) // 배치 처리 기준 시간
					.addString("jobName", "rltmApiJob") // jobName 추가
					.toJobParameters()
					);
			
			log.info("excuteEmergencyRealTimeJob complete");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Async
//	@Scheduled(fixedRate = 60000)
//	@Scheduled(cron = "0 0 1 * * ?")
	public void excuteEmergencyBaseInfoJob() {
		
		try {
			log.info("excuteEmergencyBaseInfoJob start");
			
			jobLauncher.run(apiJobConfig.bsIfApiJob()
					,new JobParametersBuilder() // 실행 추적
					.addString("datetime", LocalDateTime.now().toString()) // 배치 처리 기준 시간
					.addString("jobName", "bsIfApiJob") // jobName 추가
					.toJobParameters()
					);
			
			log.info("excuteEmergencyBaseInfoJob complete");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	private boolean isJobRunning(String jobName) {
		return jobExplorer.findRunningJobExecutions(jobName).size() > 0; // BATCH_JOB_EXECUTION 테이블에서 STATUS = 'STARTED' 또는 STATUS = 'STARTING'인 데이터를 조회
	}
}
