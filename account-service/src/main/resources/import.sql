-- Insert Accounts
INSERT INTO accounts (id, cvu, alias, balance, user_id) VALUES (null, '1828142364587587492222', 'test.account1.alias',100000.0, 1);
INSERT INTO accounts (id, cvu, alias, balance, user_id) VALUES (null, '1824000364587587492222', 'test.account2.alias',100000.0, 2);

-- Insert Transactions
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 100000.0, '2025-05-10 08:30:20', 'initial transaction', '1824000364587587492222', '1828142364587587492222', 'Transfer', 2);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 100000.0, '2025-05-10 08:30:20', 'received transaction', '1824000364587587492222', '1828142364587587492222', 'Deposit', 2);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 20000.0, '2025-05-12 14:30:20', 'payment transaction', '1828142364587587492222', '1824000364587587492222', 'Transfer', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 20000.0, '2025-05-12 14:30:20', 'received transaction', '1828142364587587492222', '1824000364587587492222', 'Deposit', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 5300.0, '2025-01-01 16:35:10', 'payment transaction', '1824000364587587492222', '1828142364587587492222', 'Deposit', 2);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 17223.0, '2025-07-01 10:02:00', 'payment transaction', '1824000364587587492222', '1828142364587587492222', 'Deposit', 2);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 112000.0, '2025-01-01 17:21:09', 'payment transaction', '1824000364587587492222', '1828142364587587492222', 'Deposit', 2);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 20000.0, '2025-02-09 12:45:00', 'payment transaction', '1824000364587587492222', '1828142364587587492222', 'Deposit', 2);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 6000.0, '2025-06-01 15:27:20', 'payment transaction', '1828142364587587492222', '1824000364587587492222', 'Transfer', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 9500.0, '2025-06-02 12:55:20', 'payment transaction', '1828142364587587492222', '1824000364587587492222' , 'Transfer', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 7650.0, '2025-06-07 11:27:20', 'payment transaction', '1828142364587587492222', '1824000364587587492222', 'Transfer', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 17300.0, '2025-06-09 10:32:20', 'payment transaction', '1828142364587587492222','1824000364587587492222', 'Transfer', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 19500.0, '2025-06-15 15:11:20', 'payment transaction', '1828142364587587492222','1824000364587587492222' , 'Transfer', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 13450.0, '2025-06-19 13:00:20', 'payment transaction', '1828142364587587492222', '1824000364587587492222', 'Transfer', 1);
INSERT INTO transactions (id, amount, realization_date, description, origin, destination, transaction_type, account_id) VALUES (null, 13450.0, '2025-06-22 10:54:10', 'payment transaction', '1828142364587587492222', '1824000364587587492222', 'Transfer', 1);
--
---- Insert Card
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 5412873403403000, 'User Test', '05/2027', 480, 'Mercado Pago', 'Credit', "Mastercard", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 376455323736878, 'User Test', '03/2025', 1698, 'AMEX', 'Credit', "AMEX", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 4539793783086269, 'User Test', '09/2025', 123, 'Brubank S.A.U.', 'Debit', "Visa", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 4539270908211569, 'User Test', '02/2027', 123, 'ICBC', 'Debit', "Visa", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 4423056737256761, 'User Test', '01/2027', 123, 'ICBC', 'Credit', "Visa", 1);
INSERT INTO cards (id, number, holder, expiration_date, cvv, bank, card_type, network, account_id) VALUES (null, 5304690884036864, 'User Test 2', '10/2028', 123, 'Ualá', 'Debit', "Mastercard", 2);

