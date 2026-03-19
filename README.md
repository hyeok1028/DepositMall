# 🏦 Hanaro

> 예금/적금 상품 조회, 가입, 해지, 만기 처리까지 다루는 금융 상품 관리 프로젝트

관리자(Admin)와 일반 사용자(User)의 기능을 분리하고,  
JWT 인증/인가, Swagger(OpenAPI 3), Actuator, Validation, 파일 업로드, 로그 분리까지 포함하여  
금융 서비스 백엔드의 핵심 기능을 구현한 프로젝트입니다.

---

## 📌 1. 프로젝트 개요

### 👨‍💼 관리자 (Admin)
- 상품 등록 / 조회 / 수정 / 삭제
- 회원 목록 조회 및 검색
- 회원별 가입 내역 조회
- 만기 일괄 처리

### 👤 일반 사용자 (User)
- 회원가입 / 로그인
- 상품 목록 / 상세 조회
- 상품 가입
- 내 가입 내역 조회
- 중도 해지
- 상품 계좌 이체

💡 회원가입 완료 시 **자유입출금 통장 자동 생성**

---

## 🛠️ 2. 기술 스택

| Category | Tech |
|----------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Security | Spring Security, JWT |
| DB | MySQL 8 |
| ORM | Spring Data JPA |
| API Docs | Swagger (OpenAPI 3) |
| Monitoring | Spring Boot Actuator |
| Test | JUnit 5, MockMvc |
| Coverage | JaCoCo |

---

## ⚙️ 3. 주요 구현 내용

### 🔐 3-1. 인증 / 인가
- JWT 발급 및 검증
- ROLE_ADMIN / ROLE_USER 권한 분리
- `@PreAuthorize` 기반 접근 제어
- JWT 필터에서 공개 경로 제외 처리
  - Swagger
  - 로그인 / 회원가입
  - Actuator

---

### ✅ 3-2. Validation
- 회원가입: 이메일 / 비밀번호 / 닉네임 / 전화번호 검증
- 상품 등록/수정: Bean Validation 적용
- 계좌번호: 숫자 11자리 검증
- 예금 / 적금에 따른 납입 주기 규칙 검증

---

### 📁 3-3. 파일 업로드
- 저장 경로:  src/main/resources/static/upload/yyyyMMdd
- 파일명: `UUID + 확장자`
- 대표 이미지 1개 업로드 지원

#### 📏 제한 조건
- 파일 1개 최대: **2MB**
- 요청 전체 최대: **10MB**

#### 📌 예시
src/main/resources/static/upload/20260318/ff29965d-ce6b-40a3-88e4-65f1204592f3.png

---

### 📜 3-4. 로그 관리
- `logback-spring.xml` 기반 로그 분리

#### 생성 로그 파일
logs/application.log
logs/user.log
logs/product.log
logs/service.log
logs/subscription.log


- 날짜 기준 롤링
- `.gz` 압축 보관

---

### 📊 3-5. 문서화 / 운영 확인

#### Swagger
- API 테스트 및 명세 확인 가능
- ADMIN / USER 그룹 분리

#### Actuator
- health, beans, env, metrics 확인 가능

#### 예외 처리
- 공통 예외 처리 (`ControllerExceptionHandler`)
- 검증 실패 메시지 커스터마이징

---

## 📂 4. 패키지 구조
src/main/java/com/hana8/hanaro
├── common
├── config
├── controller
├── dto
├── entity
├── mapper
├── repository
├── security
└── service

✔ Controller / Service / Repository 계층 분리  
✔ DTO 변환 로직을 mapper 패키지로 분리

---

## 🚀 5. 실행 방법

### 5-1. DB 실행
```bash
docker compose up -d
MySQL 포트: 3308

### 5-2. 보안 설정
spring:
  config:
    import: classpath:security.yml

실제 운영 환경에서는
DB 비밀번호
JWT Secret
→ 별도 관리 필요

### 5-3. 애플리케이션 실행

JWT Secret
→ 별도 관리 필요

🔗 6. API 문서 / 운영 URL
📘 Swagger

http://localhost:8080/swagger.html

http://localhost:8080/hana8/api-docs

http://localhost:8080/swagger-ui/index.html

📡 Actuator (포트: 9001)

http://localhost:9001/actuator

http://localhost:9001/actuator/health

http://localhost:9001/actuator/beans

http://localhost:9001/actuator/env

http://localhost:9001/actuator/metrics

📡 7. 대표 API
🔐 인증
POST /api/auth/signup
POST /api/auth/login

👨‍💼 관리자
GET    /api/admin/products
POST   /api/admin/products
PUT    /api/admin/products/{productId}
DELETE /api/admin/products/{productId}

GET    /api/admin/users
GET    /api/admin/users/{userId}/subscriptions
POST   /api/admin/users/maturity

👤 사용자
GET  /api/user/products
GET  /api/user/products/{productId}

POST /api/user/subscriptions
GET  /api/user/subscriptions

POST /api/user/subscriptions/{subscriptionId}/terminate
POST /api/user/subscriptions/transfer

8. 테스트

main source: 56개

test source: 56개 (1:1 대응)

실행
./gradlew build
./gradlew test
./gradlew jacocoTestReport

9. 개선 사항

DTO 네이밍 통일 (...DTO)

Mapper 패턴 도입 (변환 책임 분리)

파일 업로드 구조 개선 (날짜 디렉토리 + UUID)

공통 예외 처리 통합

Swagger 문서 상세화

JWT 필터 제외 경로 명확화

로깅 구조 단순화 (SLF4J + Logback)

10. 아쉬운 점 / 개선 포인트

관리자 검색 기능 → 날짜 조건 미지원

multipart + validation 구조 개선 필요

Actuator 접근 제어 필요 (현재 오픈 상태)

업로드 경로 → 외부 스토리지로 분리 필요

상품 이미지 → 현재 1개 (다중 이미지 확장 필요)

11. 산출물

ERD: docs/hanaro-erd.svg

로그: logs

업로드: src/main/resources/static/upload
