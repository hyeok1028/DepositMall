Hanaro

예금/적금 상품 조회부터 가입, 해지, 만기 처리까지 전 과정을 다루는 금융 상품 관리 백엔드 시스템입니다.
관리자(Admin)와 일반 사용자(User)의 역할을 명확히 분리하고, JWT 기반 인증/인가, Validation, 파일 업로드, 로그 관리, 테스트 커버리지까지 포함하여 실제 서비스 수준 구조로 설계 및 구현했습니다.

1. 프로젝트 개요
🔹 관리자 (Admin)

상품 등록 / 조회 / 수정 / 삭제

회원 목록 조회 및 검색

회원별 가입 내역 조회

만기 일괄 처리

🔹 일반 사용자 (User)

회원가입 / 로그인

상품 목록 / 상세 조회

상품 가입

내 가입 내역 조회

중도 해지

상품 계좌 이체

🔹 핵심 특징

회원가입 시 자유입출금 계좌 자동 생성

가입(Subscription)을 중심으로 상품/계좌/회원 관계 설계

권한 기반 API 접근 제어

2. 기술 스택
구분	기술
Language	Java 21
Framework	Spring Boot 4.0.3
Security	Spring Security, JWT
ORM	Spring Data JPA, QueryDSL
DB	MySQL 8
Docs	Swagger (OpenAPI 3)
Monitoring	Spring Boot Actuator
Test	JUnit5, MockMvc
Coverage	JaCoCo
3. 주요 구현 내용
3-1. 인증 / 인가

JWT Access Token 기반 인증

ROLE_ADMIN, ROLE_USER 권한 분리

@PreAuthorize 기반 접근 제어

🔐 인증 흐름

로그인 → JWT 발급

요청 시 JWT 필터에서 검증

SecurityContext에 사용자 정보 저장

Controller 접근 시 권한 검사

🔓 인증 제외 경로

Swagger

로그인 / 회원가입

Actuator

3-2. Validation

Bean Validation 기반 입력값 검증

✔ 회원

이메일 형식 검증 (@Email)

비밀번호 길이 제한 (8~20자)

닉네임 공백 금지

전화번호 커스텀 검증

✔ 상품

금리, 기간 등 필수값 검증

예금 / 적금 타입별 입력 조건 분기

✔ 계좌

11자리 숫자 계좌번호 검증 (@AccountNumber)

3-3. 파일 업로드

저장 경로: src/main/resources/static/upload/yyyyMMdd

파일명: UUID 기반 생성

src/main/resources/static/upload/20260318/ff29965d-ce6b-40a3-88e4-65f1204592f3.png
제한

파일당 최대 2MB

요청 전체 최대 10MB

대표 이미지 1개

3-4. 로그 관리

logback-spring.xml 기반 로그 분리

로그 파일
logs/application.log
logs/user.log
logs/product.log
logs/service.log
logs/subscription.log

날짜 기준 Rolling

.gz 압축 보관

3-5. 문서화 / 운영 점검
Swagger

API 테스트 및 명세 확인

ADMIN / USER 그룹 분리

Actuator

health

beans

env

metrics

4. 도메인 구조 (핵심 설계)
🔥 핵심 엔티티 관계

Member (회원)

Product (상품)

Account (계좌)

Subscription (가입)

👉 Subscription 중심 설계

Member 1 : N Subscription
Product 1 : N Subscription
Account 1 : N Subscription
💡 핵심 설계 의도

"가입"을 기준으로 모든 금융 흐름 관리

계좌/상품을 직접 연결하지 않고 Subscription을 통해 연결

확장성 (이자, 만기, 해지 처리)에 유리한 구조

5. 패키지 구조
src/main/java/com/hana8/hanaro
├── common        # 공통 유틸, 예외, 상수
├── config        # 설정 (Security, Swagger 등)
├── controller    # API 계층
├── dto           # 요청/응답 DTO
├── entity        # JPA Entity
├── mapper        # Entity ↔ DTO 변환
├── repository    # DB 접근
├── security      # JWT, 인증/인가
└── service       # 비즈니스 로직
6. 실행 방법
6-1. DB 실행
docker compose up -d

MySQL 포트: 3308

6-2. 보안 설정
spring:
  config:
    import: classpath:security.yml

DB 정보 및 JWT Secret 관리

6-3. 애플리케이션 실행
./gradlew bootRun
7. API 문서 / 운영 URL
Swagger

http://localhost:8080/swagger.html

http://localhost:8080/hana8/api-docs

http://localhost:8080/swagger-ui/index.html

Actuator

http://localhost:9001/actuator

http://localhost:9001/actuator/health

http://localhost:9001/actuator/metrics

8. 대표 API
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
GET    /api/user/products
GET    /api/user/products/{productId}

POST   /api/user/subscriptions
GET    /api/user/subscriptions

POST   /api/user/subscriptions/{subscriptionId}/terminate
POST   /api/user/subscriptions/transfer
9. 테스트

main / test 파일 1:1 매칭 구조

main source file: 56개
test source file: 56개
실행
./gradlew build
./gradlew test
./gradlew jacocoTestReport
10. 개선 및 리팩토링

DTO 네이밍 통일 (~DTO)

mapper 계층 분리 (수동 매핑 최소화)

파일 업로드 구조 개선 (날짜 + UUID)

공통 예외 처리 통합

Swagger 문서화 강화

JWT 필터 예외 경로 명확화

로그 구조 단순화

11. 아쉬운 점 / 개선 포인트

관리자 검색 → 날짜 조건 없음

multipart + JSON Validation 구조 개선 필요

Actuator 보안 강화 필요

파일 업로드 → 외부 스토리지 분리 필요

상품 이미지 → 다중 이미지 지원 필요

12. 산출물

ERD: docs/hanaro-erd.svg

로그: logs/

업로드: src/main/resources/static/upload

🔥 최종 한줄 요약

“금융 상품 가입(Subscription)을 중심으로 설계된, 인증/인가/검증/운영까지 고려한 실전형 Spring Boot 백엔드 프로젝트”
