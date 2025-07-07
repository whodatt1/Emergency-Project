-- 응급 가용인원 디테일/마스터 테이블 정의

CREATE TABLE tb_Emgc_Rltm_Master (
    hp_id VARCHAR(50) PRIMARY KEY, -- 기관 ID (Primary Key)
    duty_name VARCHAR(255) NOT NULL, -- 기관명
    duty_ha_yn VARCHAR(2), -- 입원실 가용 여부
    duty_er_yn VARCHAR(2), -- 응급실 운용 여부
    post_cdn1 VARCHAR(10), -- 우편번호1
    post_cdn2 VARCHAR(10), -- 우편번호2
    duty_addr VARCHAR(255), -- 주소
    duty_tel VARCHAR(50), -- 대표전화1
    duty_inf TEXT, -- 기관설명상세
    duty_time_1c VARCHAR(5), -- 월요일 close 진료시간
    duty_time_2c VARCHAR(5), -- 화요일 close 진료시간
    duty_time_3c VARCHAR(5), -- 수요일 close 진료시간
    duty_time_4c VARCHAR(5), -- 목요일 close 진료시간
    duty_time_5c VARCHAR(5), -- 금요일 close 진료시간
    duty_time_6c VARCHAR(5), -- 토요일 close 진료시간
    duty_time_7c VARCHAR(5), -- 일요일 close 진료시간
    duty_time_8c VARCHAR(5), -- 공휴일 close 진료시간
    duty_time_1s VARCHAR(5), -- 월요일 start 진료시간
    duty_time_2s VARCHAR(5), -- 화요일 start 진료시간
    duty_time_3s VARCHAR(5), -- 수요일 start 진료시간
    duty_time_4s VARCHAR(5), -- 목요일 start 진료시간
    duty_time_5s VARCHAR(5), -- 금요일 start 진료시간
    duty_time_6s VARCHAR(5), -- 토요일 start 진료시간
    duty_time_7s VARCHAR(5), -- 일요일 start 진료시간
    duty_time_8s VARCHAR(5), -- 공휴일 start 진료시간
    duty_lon DECIMAL(10, 7), -- 병원 경도
    duty_lat DECIMAL(10, 7), -- 병원 위도
    dgid_id_name VARCHAR(255) -- 진료 과목
);

CREATE TABLE tb_Emgc_Rltm_Detail (
    hp_id VARCHAR(50) PRIMARY key not null, -- 기관코드 (Primary Key)
    old_hp_id VARCHAR(50), -- (구) 기관코드
    upd_date VARCHAR(50), -- 입력일시
    emgc_ers_gen_bed_psn INT, -- 응급실 일반 병상 인원
    op_psn INT, -- [기타] 수술실 인원
    icu_neuro_psn INT, -- [중환자실] 신경과 인원
    icu_neo_psn INT, -- [중환자실] 신생아 인원
    icu_thor_surg_psn INT, -- [중환자실] 흉부외과 인원
    icu_gen_psn INT, -- [중환자실] 일반 인원
    ipt_gen_psn INT, -- [입원실] 일반 인원
    duty_doc_nm VARCHAR(50), -- 당직의
    ct_avail_yn CHAR(2), -- CT 가용 여부 (Y/N)
    mri_avail_yn CHAR(2), -- MRI 가용 여부 (Y/N)
    angio_avail_yn CHAR(2), -- 혈관촬영기 가용 여부 (Y/N)
    venti_avail_yn CHAR(2), -- 인공호흡기 가용 여부 (Y/N)
    venti_pret_avail_yn CHAR(2), -- 인공호흡기 조산아 가용 여부 (Y/N)
    incu_avail_yn CHAR(2), -- 인큐베이터 가용 여부 (Y/N)
    crrt_avail_yn CHAR(2), -- CRRT 가용 여부 (Y/N)
    ecmo_avail_yn CHAR(2), -- ECMO 가용 여부 (Y/N)
    oxy_avail_yn CHAR(2), -- 고압 산소 치료기 가용 여부 (Y/N)
    hypo_avail_yn CHAR(2), -- 중심 체온 조절 유도기 가용 여부 (Y/N)
    ambul_avail_yn CHAR(2), -- 구급차 가용 여부 (Y/N)
    duty_doc_tel VARCHAR(50), -- 응급실 당직의 직통 연락처
    icu_med_psn INT, -- [중환자실] 내과 인원
    icu_surg_psn INT, -- [중환자실] 외과 인원
    ortho_ipt_psn INT, -- 외과입원실 (정형외과) 인원
    neurol_inpt_yn CHAR(2), -- 신경과입원실 (Y/N)
    icu_neuro_surg_psn INT, -- [중환자실] 신경외과 인원
    icu_drug_yn CHAR(2), -- 약물 중환자 (Y/N)
    icu_burn_psn INT, -- [중환자실] 화상 인원
    icu_trauma_psn INT, -- [중환자실] 외상 인원
    venti_yn CHAR(2), -- VENTI(소아) 여부 (Y/N)
    incu_yn CHAR(2), -- 인큐베이터(보육기) 여부 (Y/N)
    duty_ped_doc_tel VARCHAR(50), -- 소아당직의 직통 연락처
    isolarea_neg_press_bed_psn INT, -- 격리진료구역 음압격리병상
    isolarea_gen_press_bed_psn INT, -- 격리진료구역 일반격리병상
    ped_press_bed_psn INT, -- 소아 음압 격리 병상
    ped_isol_bed_psn INT, -- 소아 일반 격리 병상
    emgc_icu_neg_press_psn INT, -- [응급전용] 중환자실 음압격리 인원
    emgc_icu_gen_press_psn INT, -- [응급전용] 중환자실 일반격리 인원
    emgc_ipt_neg_press_psn INT, -- [응급전용] 입원실 음압격리 인원
    emgc_ipt_gen_press_psn INT, -- [응급전용] 입원실 일반격리 인원
    infec_ddc_icu_psn INT, -- 감염병 전담병상 중환자실 인원
    infec_ddc_icu_neg_press_psn INT, -- 감염병 전담병상 중환자실 내 음압격리병상 인원
    infec_severe_bed_psn INT, -- [감염] 중증 병상 인원
    infec_semi_severe_bed_psn INT, -- [감염] 준-중증 병상 인원
    infec_mdrate_bed_psn INT, -- [감염] 중등증 병상 인원
    chrt_isol_psn INT, -- 코호트 격리 인원
    ped_psn INT, -- 소아 인원
    emgc_ers_neg_press_bed_psn INT, -- 응급실 음압 격리 병상 인원
    emgc_ers_gen_press_bed_psn INT, -- 응급실 일반 격리 병상 인원
    emgc_icu_psn INT, -- [응급전용] 중환자실 인원
    icu_ped_psn INT, -- [중환자실] 소아 인원
    emgc_ped_icu_psn INT, -- [응급전용] 소아중환자실 인원
    icu_cardio_psn INT, -- [중환자실] 심장내과 인원
    icu_neg_press_psn INT, -- [중환자실] 음압격리 인원
    emgc_ipt_psn INT, -- [응급전용] 입원실 인원
    emgc_ped_ipt_psn INT, -- [응급전용] 소아입원실
    ipt_trauma_psn INT, -- [입원실] 외상전용 인원
    etc_trauma_op_psn INT, -- [기타] 외상전용 수술실 인원
    ipt_psy_closed_psn INT, -- [입원실] 정신과 폐쇄병동 인원
    ipt_neg_press_psn INT, -- [입원실] 음압격리 인원
    etc_dlvr_room_yn CHAR(2),                        -- [기타] 분만실 (Y/N)
    etc_burn_spctrt_yn CHAR(2),                        -- [기타] 화상전용처치실 (Y/N)
    trauma_resus_psn INT, -- 외상 소샐실 인원 V4 추가
    trauma_area_psn INT, -- 외상 환자 진료 구역 인원 V4 추가
    duty_name VARCHAR(100),                    -- 기관명
    duty_tel VARCHAR(50),                    -- 응급실 전화
    gen_stdd_psn  INT,                                -- 일반_기준 인원
    ped_stdd_psn  INT,                                -- 소아_기준 인원
    emgc_ers_neg_press_stdd_bed_psn INT,  -- 응급실 음압 격리 병상_기준 인원
    emgc_ers_gen_press_stdd_bed_psn INT,  -- 응급실 일반 격리 병상_기준 인원
    emgc_icu_stdd_psn INT,   -- [응급전용] 중환자실_기준 인원
    icu_med_stdd_psn  INT,                                -- [중환자실] 내과_기준 인원
    icu_surg_stdd_psn  INT,                                -- [중환자실] 외과_기준 인원
    icu_neo_stdd_psn INT,                                -- [중환자실] 신생아_기준 인원
    icu_ped_stdd_psn  INT,                                -- [중환자실] 소아_기준 인원
    emgc_ped_icu_stdd_psn  INT,                                -- [응급전용] 소아중환자실_기준 인원
    icu_neuro_stdd_psn INT,                                -- [중환자실] 신경과_기준 인원
    icu_neuro_surg_stdd_psn  INT,                                -- [중환자실] 신경외과_기준 인원
    icu_burn_stdd_psn  INT,                                -- [중환자실] 화상_기준 인원
    icu_trauma_stdd_psn  INT,                                -- [중환자실] 외상_기준 인원
    icu_cardio_stdd_psn  INT,                                -- [중환자실] 심장내과_기준 인원
    icu_thor_surg_stdd_psn  INT,                                -- [중환자실] 흉부외과_기준 인원
    icu_gen_stdd_psn  INT,                                -- [중환자실] 일반_기준 인원
    icu_neg_press_stdd_psn  INT,                                -- [중환자실] 음압격리_기준 인원
    emgc_ipt_stdd_psn  INT,                                -- [응급전용] 입원실_기준 인원
    emgc_ped_ipt_stdd_psn  INT,                                -- [응급전용] 소아입원실_기준 인원
    ipt_trauma_stdd_psn  INT,                                -- [입원실] 외상전용_기준 인원
    etc_op_stdd_psn  INT,                                -- [기타] 수술실_기준 인원
    etc_trauma_op_stdd_psn  INT,                                -- [기타] 외상전용 수술실_기준 인원
    ipt_psy_closed_stdd_psn  INT,                                -- [입원실] 정신과 폐쇄병동_기준 인원
    ipt_neg_press_stdd_psn  INT,                                -- [입원실] 음압격리_기준 인원
    etc_dlvr_room_stdd_psn  INT,                                -- [기타] 분만실_기준 인원
    ct_stdd_psn  INT,                                -- CT_기준 인원
    mri_stdd_psn  INT,                                -- MRI_기준 인원
    angio_stdd_psn  INT,                                -- 혈관촬영기_기준 인원
    venti_gen_stdd_psn  INT,                                -- 인공호흡기 일반_기준 인원
    venti_pret_stdd_psn  INT,                                -- 인공호흡기 조산아_기준
    incu_stdd_psn  INT,                                -- 인큐베이터_기준 인원
    crrt_stdd_psn  INT,                                -- CRRT_기준 인원
    ecmo_stdd_psn  INT,                                -- ECMO_기준 인원
    core_temp_ctrl_stdd_psn  INT,                                -- 중심체온조절유도기_기준 인원
    etc_burn_spctrt_room_stdd_psn  INT,                                -- [기타] 화상전용처치실_기준 인원
    oxy_stdd_psn  INT,                                -- 고압산소치료기_기준 인원
    ipt_gen_stdd_psn  INT,                                -- [입원실] 일반_기준 인원
    isolarea_neg_press_stdd_psn  INT,                                -- 격리진료구역 음압격리_기준 인원
    isolarea_gen_press_stdd_psn  INT,                                -- 격리진료구역 일반격리_기준 인원
    ped_neg_press_stdd_psn  INT,                                -- 소아음압격리_기준 인원
    ped_gen_press_stdd_psn  INT,                                -- 소아일반격리_기준 인원
    emgc_icu_neg_press_stdd_psn  INT,                                -- [응급전용] 중환자실 음압격리_기준 인원
    emgc_icu_gen_press_stdd_psn  INT,                                -- [응급전용] 중환자실 일반격리_기준 인원
    emgc_ipt_neg_press_stdd_psn  INT,                                -- [응급전용] 입원실 음압격리_기준 인원
    emgc_ipt_gen_press_stdd_psn  INT,                                -- [응급전용] 입원실 일반격리_기준 인원
    infec_ddc_icu_stdd_psn  INT,                                -- 감염병 전담병상 중환자실_기준 인원
    infec_ddc_icu_neg_press_stdd_psn  INT,                                -- 감염병 전담병상 중환자실 내 음압격리병상_기준 인원
    infec_severe_bed_stdd_psn  INT,                                -- [감염] 중증 병상_기준 인원
    infec_semi_severe_bed_stdd_psn  INT,                                -- [감염] 준-중증 병상_기준 인원
    infec_mdrate_bed_stdd_psn  INT,                                -- [감염] 중등증 병상_기준 인원
    chrt_isol_stdd_cnt INT  ,                               -- 코호트 격리_기준 인원
    trauma_resus_sttd_psn INT, -- 외상 소생실 기준 인원 V4 추가
    trauma_area_sttd_psn INT -- 외상 환자 진료 구역 기준 인원 V4 추가
);

CREATE TABLE `tb_bjd` (
  `past_cd` varchar(10) DEFAULT NULL,
  `ri_nm` varchar(50) DEFAULT NULL,
  `law_cd` varchar(10) NOT NULL,
  `deleted_date` DATE DEFAULT NULL,
  `created_date` DATE DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `si_nm` varchar(50) DEFAULT NULL,
  `sido_nm` varchar(50) DEFAULT NULL,
  `dong_nm` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`law_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tb_bookmark` (
  `user_id` varchar(10) NOT NULL,
  `hp_id` varchar(50) NOT NULL,
  `reg_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `mod_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `hp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tb_fcm (
  user_id VARCHAR(10) NOT NULL,
  fcm_token VARCHAR(512) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- emergency.tb_outbox definition

CREATE TABLE `tb_outbox` (
  `batch_id` varchar(255) NOT NULL,
  `aggregate_type` varchar(255) NOT NULL,
  `aggregate_id` varchar(255) NOT NULL,
  `event_type` varchar(255) NOT NULL,
  `payload` text NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(50) NOT NULL DEFAULT 'READY_TO_PUBLISH',
  PRIMARY KEY (`batch_id`, `aggregate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2063 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;