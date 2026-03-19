INSERT INTO products (name, productType, paymentAmount, paymentCycle, periodMonths, maturityInterestRate, earlyTerminationInterestRate, description, isActive, representativeImagePath)
VALUES
  ('Hanaro Savings 12', 'SAVINGS', 100000, 'MONTHLY', 12, 3.5, 1.2, 'Monthly savings product', true, null),
  ('Hanaro Deposit 6', 'DEPOSIT', 1000000, null, 6, 3.0, 1.0, 'Term deposit product', true, null);
