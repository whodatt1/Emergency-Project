# 🚑 EMERGENCY : 실시간 응급실 가용 병상 알림 서비스

### 📖 Project Overview
**EMERGENCY**는 공공데이터포털의 실시간 응급의료기관 API를 활용하여, 사용자에게 전국 응급실 및 입원실의 가용 병상 정보를 실시간에 가깝게 제공하는 웹 서비스입니다.

단순 조회에 그치지 않고, 사용자가 관심 병원을 등록하면 병상 정보 갱신 시 **FCM(Firebase Cloud Messaging)**을 통해 푸시 알림을 제공하여 신속한 대응을 돕습니다.

---

### 🛠 Tech Stack
| Category | Technology |
| --- | --- |
| **Language & Framework** | Java 17, Spring Boot 3.x, JPA |
| **Batch & Data** | **Spring Batch**, Scheduler, MySQL |
| **Message Queue** | **Apache Kafka** |
| **Notification** | Firebase Admin SDK (FCM) |
| **Frontend** | React |

---

### 🔄 System Flow (데이터 처리 흐름)
데이터의 최신성 유지와 알림 발송의 안정성을 위해 **Spring Batch**와 **Kafka**를 결합하여 아래와 같은 흐름으로 데이터를 처리합니다.

**1. 데이터 수집 및 갱신 (Spring Batch)**
* `Spring Scheduler`가 2분 간격으로 배치를 실행합니다.
* 공공데이터 API를 호출하여 최신 병상 정보를 가져와 DB에 업데이트(Bulk Update)합니다.

**2. 변동 감지 및 이벤트 발행 (Spring Event)**
* 배치 수행 중 병상 수의 변동이 감지된 병원 리스트를 추출합니다.
* 트랜잭션이 커밋된 직후, `Spring Event`를 통해 변동된 병원 정보를 발행합니다.

**3. 메시지 큐 전송 (Kafka Producer)**
* 이벤트 핸들러가 해당 정보를 받아 `Kafka`의 알림 토픽(Topic)으로 메시지를 전송합니다.
* 이로써 배치(DB 갱신) 로직과 알림(발송) 로직을 분리하여 상호 영향을 주지 않도록 처리했습니다.

**4. 알림 발송 (Kafka Consumer & FCM)**
* `Kafka Consumer`가 메시지를 수신합니다.
* 해당 병원을 즐겨찾기한 유저 중 유효한 토큰을 가진 유저를 선별하여 FCM으로 푸시 알림을 전송합니다.

---

### 💡 Key Features (핵심 기술적 의사결정)

**1. 대량 데이터 실시간 처리 (Spring Batch)**
* 전국 병원 데이터를 주기적으로 갱신하기 위해 **Chunk 지향 처리**를 적용했습니다.
* 단순 업데이트가 아닌, 변동 사항이 있는 데이터만 선별하여 DB 부하를 최소화했습니다.

**2. Kafka를 활용한 시스템 분리 (Decoupling)**
* **[문제]** 초기에는 배치 로직 내부에서 바로 알림을 보냈으나, 알림 발송이 지연되면 DB 트랜잭션까지 길어지는 문제가 있었습니다.
* **[해결]** **Kafka**를 도입하여 **'데이터 저장'**과 **'알림 발송'**의 역할을 물리적으로 분리했습니다. 덕분에 알림 서버에 부하가 걸려도 데이터 갱신은 정상적으로 이루어지며, 시스템 전체의 안정성을 확보했습니다.

**3. 디테일한 의료 정보 제공**
* 응급실/입원실 가용 현황뿐만 아니라 진료 가능 과목, 보유 의료 장비 등 실질적인 의료 자원 정보를 상세 페이지에서 제공합니다.

---

## 💻 프로젝트 화면

![리스트 페이지](./img/List.png)
![디테일 페이지 1](./img/Detail1.png)
![디테일 페이지 2](./img/Detail2.png)
![FCM](./img/FCM.png)

---

## 🧠 프로젝트 리팩토링 및 성능 개선 과정 포트폴리오

https://even-gerbil-e3c.notion.site/Emergency-Project-Portfolio-2701d86b26e380519ae4e8469caef87f?source=copy_link

---

## 🚀 로컬 Kafka 환경 (도커 사용)

```yml
version: '3.8'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.1
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:7.4.1
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
```
