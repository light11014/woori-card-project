# 우리카드 소비 트렌드 분석 대시보드

- 카드 상품 기획 · 마케팅 전략 수립을 위해 고객의 분기별 소비 트렌드를 제공하는 **내부 직원 전용 대시보드**
- WAS 이중화 · DB 복제 · 세션 클러스터링을 고려한 **3-Tier Architecture** 기반으로 구현

---

## 1. 기획 배경

**원천 데이터**

- 고객별 분기 소비 내역
- 소비 관련 40개 세부 항목을 포함한 약 530만 건 규모의 데이터

**활용 방안**

40개 항목을 그대로 보여주면 너무 세분화되어 경향 파악이 어려움

→ 유사 항목을 묶어 **상위 8개 카테고리**로 집계해 소비 트렌드를 한눈에 파악

| 서비스 카테고리 | 설명 |
|---|---|
| 🍽️ **식품 (food)** | 식비, 마트, 배달 등 |
| 🚗 **자동차 (car)** | 주유, 정비, 자동차 관련 등 |
| ✈️ **여행/문화 (travelCulture)** | 숙박, 항공, 공연, 레저 등 |
| 🏥 **보험/의료 (insuranceHealth)** | 병원, 약국, 보험 등 |
| 📚 **교육/사무 (educationOffice)** | 학원, 도서, 사무용품 등 |
| 🛍️ **쇼핑 (shopping)** | 의류, 온라인 쇼핑, 유통 등 |
| 🏠 **생활 (living)** | 수리, 용역, 건물관리 등 |
| 🏡 **주거 (home)** | 가전, 가구, 주방용품, 건축자재 등 |

### 서비스 대상 및 목적

**대상: 우리카드 내부 직원** (카드 상품 기획 / 마케팅 담당자)

카드사는 고객의 소비 경향을 기반으로 **카드 혜택 / 이벤트 / 신상품**을 기획
```
여행/문화 소비가 2분기 연속 급증
    ↓
여행 특화 카드 혜택 강화 전략 수립
    ↓
마케팅 캠페인 기획
```

> 소비 트렌드 데이터 = 상품 기획의 근거 자료

---

## 2. Tech Stack

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

> MySQL Source / Replica · Nginx · Tomcat 2대 컨테이너 구성

---

## 3.  Architecture
<img width="1919" height="518" alt="image" src="https://github.com/user-attachments/assets/1ad7cb73-a970-42a2-80d7-42c92606f68e" />

**3-Tier Architecture**

| 레이어 | 구성 |
|--------|------|
| Presentation | JSP |
| Application | Servlet (WAS 이중화) |
| Data | MySQL Replication (Read/Write 분리) |
| Infrastructure | Docker (Nginx · Tomcat×2 · MySQL Source/Replica) |

```
             [우리카드 직원 브라우저]
                     ↓ 로그인 (세션 인증)
            [Nginx — 라운드로빈]
             ↙                ↘
           Tomcat1           Tomcat2    ← DeltaManager 세션 복제
               ↓                 ↓
  [Write → MySQL Source]   [Read → MySQL Replica]
            ↑    ↑                   ↑
     회원가입  집계결과            트렌드 조회
     (INSERT) (INSERT, 배치)      (SELECT)
```

---
## 4. 주요 기능

### 🔐 세션 기반 로그인 인증
<img width="1918" height="861" alt="image (1)" src="https://github.com/user-attachments/assets/c4f83414-4777-4741-87dd-74108596728d" />


- 로그인 성공 시 기존 세션 무효화 후 신규 세션 발급
- 세션 유효시간 30분 / 로그아웃 시 세션 완전 삭제

### 📊 분기별 소비 트렌드 조회
<img width="1896" height="861" alt="image (3)" src="https://github.com/user-attachments/assets/1688f242-29ed-4a66-8c23-5c92e063c700" />

- 조회 기간: 최근 1 / 4 / 8분기
- 표시 방식: 금액(AMOUNT) / 비율(RATIO)


### 📈 전분기 대비 증감률 분석
<img width="1892" height="860" alt="image (4)" src="https://github.com/user-attachments/assets/8019279b-e0fe-4988-81ad-60d7126a0ac3" />

- 카테고리별 전분기 대비 증감률 자동 계산 및 시각적 표시

### 🏆 최근 분기 Top 3 카테고리
<img width="1918" height="862" alt="image (2)" src="https://github.com/user-attachments/assets/2d462ff3-8556-4a92-93a0-216728b7c36f" />

- 금액 / 비율 기준 소비 상위 3개 카테고리 자동 추출

---

## 5. 기술적 주요 포인트

### 포인트 1 — 집계 테이블로 조회 성능 개선

- 문제 : 530만 건을 조회할 때마다 `GROUP BY + SUM` 집계 실행 시 응답 속도 저하
- 해결 : 사전 집계 테이블(`CARD_TREND_QUARTERLY`) 도입

```
원천 데이터 (530만 건)              집계 테이블 (수십 행)
┌──────────────────────┐            ┌──────────────────────────────────┐
│ user_id | category   │   배치     │ quarter | food | shopping | ...  │
│ ...     | ...        │   ───▶    │ 2024-Q1 | 120억 | 98억   | ...   │
│ 530만 행             │            │ 2024-Q2 | 135억 | 102억  | ...   │
└──────────────────────┘            └──────────────────────────────────┘
      매번 집계 ❌                        미리 계산된 수십 행 조회 ✅
```

**왜 가능한가?**

원천 데이터는 **분기에 한 번** 쌓이는 구조로 데이터 변경 빈도가 낮음

→ 분기 종료 시 **배치 프로그램**으로 집계 테이블을 1회 갱신하고, 이후 조회는 집계 테이블만 참조

---

### 포인트 2 — WAS 이중화 (Tomcat 2대 + Nginx 라운드로빈)

- 단일 서버 장애 시 서비스 전체 중단 → **단일 장애점(SPOF) 제거**
- 2대가 요청을 나눠 처리 → **부하 분산**

```
  [Nginx — 라운드로빈]
    ↙            ↘
 Tomcat1        Tomcat2
```

**라운드로빈을 선택한 이유**

- 모든 요청(대시보드 조회)의 처리 비용이 균일하므로 단순 균등 분배가 최적.


---

### 포인트 3 — MySQL Replication (Source / Replica)

- 조회(Read)는 Replica, 쓰기(Write)는 Source로 역할 분리

```
Write (회원가입 INSERT) → woori-mysql-source:3306
Read  (트렌드 조회 SELECT) → woori-mysql-replica:3306
```

**GTID 기반 복제**

- 트랜잭션 단위로 자동 위치 동기화 → **운영이 안전하고 편리**

```
# Source
gtid_mode=ON
log_bin=mysql-bin

# Replica
gtid_mode=ON
read_only=ON              # 쓰기 차단
SOURCE_AUTO_POSITION=1    # GTID 자동 위치 동기화
```


---

### 포인트 4 — 세션 클러스터링 (DeltaManager)

WAS가 2대이므로 매 요청이 다른 서버로 라우팅될 수 있어 세션 정합성 문제 발생

→ Tomcat `server.xml`에 `SimpleTcpCluster` + `DeltaManager` 구성으로 해결

**동작 방식**

```
[로그인 → Tomcat1]  세션 생성
        ↓ DeltaManager가 변경분만 복제
[Tomcat2도 동일한 세션 보유]

→ 다음 요청이 Tomcat2로 가도 로그인 유지 ✅
→ Tomcat1 장애 시에도 Tomcat2에 세션 살아있음 ✅
```

| 구성 요소 | 역할 |
| --- | --- |
| `DeltaManager` | 변경분(Delta)만 복제 → 네트워크 효율적 |
| `McastService` | 멀티캐스트로 클러스터 멤버 자동 탐색 |
| `ReplicationValve` | 요청 완료 후 세션 변경 감지 시 복제 실행 |
| `JvmRouteBinderValve` | Failover 시 세션 ID 자동 재매핑 |

> [Tomcat 공식 문서](https://tomcat.apache.org/tomcat-9.0-doc/cluster-howto.html) 기준, DeltaManager(All-to-All)는 **노드 4개 미만 소규모 클러스터에 권장**.

  

