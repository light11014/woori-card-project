# woori-card-project

---

## 서비스 소개
카드 거래 데이터를 분기 단위로 집계해 소비 카테고리 트렌드(금액/비율/증감률)를 빠르게 조회하고 운영 환경(이중화/복제/세션)을 고려한 3-Tier Architecture 대시보드

---

## :hammer_and_wrench: Tech Stack

### :desktop_computer: Backend
![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Servlet](https://img.shields.io/badge/Servlet%20%26%20JSP-007396?style=for-the-badge&logo=java&logoColor=white)
![Tomcat](https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black)
![JDBC](https://img.shields.io/badge/JDBC-003545?style=for-the-badge&logo=databricks&logoColor=white)
![HikariCP](https://img.shields.io/badge/HikariCP-000000?style=for-the-badge&logo=databricks&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white)
![Logback](https://img.shields.io/badge/Logback-1A1A2E?style=for-the-badge&logo=logstash&logoColor=white)

### :globe_with_meridians: Web / Load Balancer
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)

> Reverse Proxy · Load Balancing

### :file_cabinet: Database
![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

> Source (Write/Read) · Replica (Read Only) · Replication 구성
> 분기별 사전 집계 테이블 `CARD_TREND_QUARTERLY`

### :whale: Infrastructure
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

> MySQL Source / Replica · Nginx 컨테이너 구성

---

## :building_construction: Architecture

| 레이어 | 구성 |
|--------|------|
| :art: Presentation | JSP |
| :gear: Application | Servlet (WAS 이중화) |
| :file_cabinet: Data | MySQL Replication (Read/Write 분리) |

- :repeat: **WAS 이중화** — Apache Tomcat 2대 운영
- :book: **Read / Write 분리** — HikariCP Connection Pool 기반
- :closed_lock_with_key: **Session 기반 인증**

---

## 🚀 주요 기능 (Key Features)

### 🔐 1. 세션 기반 인증
- 로그인 / 로그아웃 기능 제공
- `JSESSIONID` 기반 세션 관리
- **Session Fixation 공격 방지 처리**
- Nginx Session Replication 적용을 통한  
  **WAS 이중화 환경에서도 로그인 세션 유지**

---

### 📊 2. 분기별 소비 트렌드 분석
- 분기 단위 소비 데이터 조회
- 조회 기간 선택
  - 최근 **1 / 4 / 8개 분기**
- 표시 방식 선택
  - 금액 (Absolute)
  - 비율 (%)

---

### 📈 3. 전분기 대비 증감률 분석
- 카테고리별 전분기 대비 증감률 자동 계산
- 소비 증가 / 감소에 따른 시각적 표시
- 전체 소비 금액 기준 증감률 제공

---

### 🏆 4. 최근 분기 Top 3 카테고리
- 최근 분기 기준 소비 상위 3개 카테고리 자동 추출
- 표시 모드에 따라 동적 계산
  - 금액 기준
  - 비율 기준

---

### ⚡ 5. 성능 최적화 (Aggregation Table)
- 약 **530만 건 원천 데이터** 직접 조회 방식 제거
- 사전 집계 테이블(`CARD_TREND_QUARTERLY`) 활용
- 대용량 데이터 환경에서도 **빠른 응답 속도 보장**

---

### 🗄 6. Read / Write 분리 구조
- 조회(Read) 요청 → **Replica DB**
- 쓰기(Write) 요청 → **Source DB**
- HikariCP 기반 DataSource 분리 구성

---

### 🛡 7. 장애 대응 설계
- MySQL Replication 구성
- Source 장애 발생 시
  - Replica 승격(Failover) 시나리오 설계
  - 서비스 연속성 확보
