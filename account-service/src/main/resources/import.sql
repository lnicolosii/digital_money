-- Insert Accounts
INSERT INTO accounts (id, cvu, alias, balance, user_id) VALUES (null, '1828142364587587492222', 'test.account1.alias',100000.0, 1);
INSERT INTO accounts (id, cvu, alias, balance, user_id) VALUES (null, '1824000364587587492222', 'test.account2.alias',100000.0, 2);

-- Insert Transactions
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 100000.0, '2025-05-10 08:30:20', 'received transfer', '1828142364587587492222', '1824000364587587492222', 'TransferOUT', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 100000.0, '2025-05-10 08:30:20', 'received transfer', '1828142364587587492222', '1824000364587587492222', 'TransferIN', 2);

INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 15673.0, '2025-05-10 08:30:20', 'received transfer', '1824000364587587492222', '1828142364587587492222', 'TransferIN', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 15673.0, '2025-05-10 08:30:20', 'received transfer', '1824000364587587492222', '1828142364587587492222', 'TransferOUT', 2);

INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 88943.0, '2025-05-10 08:30:20', 'received transfer', '1828142364587587492222', '1824000364587587492222', 'TransferOUT', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 88943.0, '2025-05-10 08:30:20', 'received transfer', '1828142364587587492222', '1824000364587587492222', 'TransferIN', 2);

INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 98732.0, '2025-05-10 08:30:20', 'received transfer', '1824000364587587492222', '1828142364587587492222', 'TransferIN', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 98732.0, '2025-05-10 08:30:20', 'received transfer', '1824000364587587492222', '1828142364587587492222', 'TransferOUT', 2);

INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 9000.0, '2025-05-10 08:30:20', 'received transfer', '1828142364587587492222', '1824000364587587492222', 'TransferOUT', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 9000.0, '2025-05-10 08:30:20', 'received transfer', '1828142364587587492222', '1824000364587587492222', 'TransferIN', 2);

INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 25000.0, '2025-05-10 08:30:20', 'received transfer', '1824000364587587492222', '1828142364587587492222', 'TransferIN', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 25000.0, '2025-05-10 08:30:20', 'received transfer', '1824000364587587492222', '1828142364587587492222', 'TransferOUT', 2);

--
---- Insert Card
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 5412873403403000, 'User Test', '05/2027', 480, 'Mercado Pago', 'Credit', "Mastercard", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 376455323736878, 'User Test', '03/2025', 1698, 'AMEX', 'Credit', "AMEX", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 4539793783086269, 'User Test', '09/2025', 123, 'Brubank S.A.U.', 'Debit', "Visa", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 4539270908211569, 'User Test', '02/2027', 123, 'ICBC', 'Debit', "Visa", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 4423056737256761, 'User Test', '01/2027', 123, 'ICBC', 'Credit', "Visa", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 5304690884036864, 'User Test 2', '10/2028', 123, 'Ualá', 'Debit', "Mastercard", 2);

