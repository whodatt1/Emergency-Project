package com.emergency.web.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.common.Uuid;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emergency.web.config.ApiJobConfig;
import com.emergency.web.mapper.fcm.FcmMapper;
import com.emergency.web.mapper.outbox.OutboxMapper;
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
	private final OutboxMapper outboxMapper;
	
	private final JobLauncher jobLauncher;
	private final JobExplorer jobExplorer;
	
	private final ApiJobConfig apiJobConfig;
	
	// Job을 제외한 작업의 실행상태를 추적하기 위해 추가
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	
	// 로그인 후 로그인 혹은 로그아웃 과정을 거치지 않아 DB에 남아있는 만료기간이 지난 리프레쉬 토큰 삭제처리
	@Async
	@Scheduled(fixedRate = 60000)
	public void expiredRefreshTokensCleaner() {
		// false일 경우 true로 변경 후 true 반환 if문 패스
		// true일 경우 false 반환하며 if문 내부 실행하며 return
		// 1분 간격으로 실행되므로 해당 밸리데이션 추가
		if (!isRunning.compareAndSet(false, true)) {
			log.warn("expiredRefreshTokensCleaner already started");
			return;
		}
		
		try {
			log.info("expiredRefreshTokensCleaner start");
			
			// 만료된 유저 아이디
			List<String> expiredUserIds = tokenMapper.getUserIdsWithExpiredRefreshToken();
			
			tokenMapper.deleteExpiredRefreshToken();
			
			// 만료된 유저의 fcm 토큰도 삭제
			for (String userId : expiredUserIds) {
				fcmMapper.deleteFcmInfoByUserId(userId);
			}
			
			log.info("expiredRefreshTokensCleaner complete");
		} finally {
			isRunning.set(false);
		}
		
	}
	
	// 12 시간마다 실행 
	@Scheduled(fixedRate = 60000)
	@Scheduled(cron = "0 0 6,18 * * ?")
	public void staleEmgcRltmOutboxCleaner() {
		log.info("staleEmgcRltmOutboxCleaner start");
		
		outboxMapper.staleEmgcRltmOutboxCleaner();
		
		log.info("staleEmgcRltmOutboxCleaner complete");
	}
	
	@Async
	@Scheduled(fixedRate = 60000)
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
	@Scheduled(cron = "0 0 1 * * ?")
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
