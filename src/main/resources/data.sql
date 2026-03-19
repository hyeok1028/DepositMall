-- 1. Member 테이블 시드 데이터
-- Admin User
INSERT INTO members (id, email, password, nickname, isActive, createdAt, updatedAt) 
VALUES (188500000000000001, 'admin@hanabank.com', '$2a$10$7vN3l/oM1m.Xp5H5Xm/L/.V7qG4z0X0X0X0X0X0X0X0X0X0X0X0X', '관리자', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Regular User
INSERT INTO members (id, email, password, nickname, isActive, createdAt, updatedAt) 
VALUES (188500000000000002, 'user@hanabank.com', '$2a$10$7vN3l/oM1m.Xp5H5Xm/L/.V7qG4z0X0X0X0X0X0X0X0X0X0X0X0X', '한하나', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. Member Roles 데이터
INSERT INTO member_roles (member_id, roles) VALUES (188500000000000001, 'ROLE_ADMIN');
INSERT INTO member_roles (member_id, roles) VALUES (188500000000000002, 'ROLE_USER');

-- 3. Product 테이블 시드 데이터
-- DEPOSIT 상품 1
INSERT INTO products (id, name, productType, paymentAmount, periodMonths, maturityInterestRate, earlyTerminationInterestRate, description, isActive, createdAt, updatedAt) 
VALUES (188500000000000101, '하나로 정기예금(12개월)', 'DEPOSIT', 1000000, 12, 4.0, 1.5, '12개월 고금리 정기예금 상품입니다.', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- DEPOSIT 상품 2
INSERT INTO products (id, name, productType, paymentAmount, periodMonths, maturityInterestRate, earlyTerminationInterestRate, description, isActive, createdAt, updatedAt) 
VALUES (188500000000000102, '하나로 정기예금(6개월)', 'DEPOSIT', 500000, 6, 3.8, 1.2, '6개월 단기 정기예금 상품입니다.', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- SAVINGS 상품 1
INSERT INTO products (id, name, productType, paymentAmount, paymentCycle, periodMonths, maturityInterestRate, earlyTerminationInterestRate, description, isActive, createdAt, updatedAt) 
VALUES (188500000000000201, '하나로 자유적금(월)', 'SAVINGS', 100000, 'MONTHLY', 12, 4.5, 1.5, '매월 납입하는 자유로운 적금 상품입니다.', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- SAVINGS 상품 2
INSERT INTO products (id, name, productType, paymentAmount, paymentCycle, periodMonths, maturityInterestRate, earlyTerminationInterestRate, description, isActive, createdAt, updatedAt) 
VALUES (188500000000000202, '하나로 주거래적금(주)', 'SAVINGS', 50000, 'WEEKLY', 24, 5.0, 2.0, '매주 납입하여 목돈을 만드는 상품입니다.', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
