-- Insert Roles
--INSERT INTO roles (role_id, name) VALUES (1, "ROLE_ADMIN");
--INSERT INTO roles (role_id, name) VALUES (2, "ROLE_USER");

-- Insert Test User (password: password123)
INSERT INTO users (id, first_name, last_name, dni, account_id, email, password, phone) VALUES (null, 'Test', 'User', "12345678", 1, 'user@test.com', '$2a$12$PTPD.pvJAK4cJ84j4HW8muzW2EN.EmPbpaA/gN5NJrACRXRwBtvjq', "1234567890");
