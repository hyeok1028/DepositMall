-- 상품 샘플 데이터
INSERT INTO products (name, productType, paymentAmount, paymentCycle, periodMonths, maturityInterestRate, earlyTerminationInterestRate, description, isActive, representativeImagePath)
VALUES
  ('하나로 자유적금(월)', 'SAVINGS', 100000, 'MONTHLY', 12, 4.5, 1.5, '매월 납입하는 자유로운 적금 상품입니다.', true, null),
  ('하나로 주거래적금(주)', 'SAVINGS', 50000, 'WEEKLY', 24, 5.0, 2.0, '매주 납입하여 목돈을 만드는 상품입니다.', true, null),
  ('하나로 정기예금(6개월)', 'DEPOSIT', 1000000, null, 6, 3.8, 1.2, '6개월 단기 정기예금 상품입니다.', true, null),
  ('하나로 정기예금(12개월)', 'DEPOSIT', 5000000, null, 12, 4.0, 1.5, '12개월 고금리 정기예금 상품입니다.', true, null),
  ('하나로 청년적금', 'SAVINGS', 200000, 'MONTHLY', 36, 6.0, 2.5, '청년을 위한 우대금리 적금 상품입니다.', true, null);
