-- Insert test data
INSERT INTO ACCOUNT (iban, user_id, version, created_date, last_modified_date, created_by, last_modified_by) VALUES
('US64SVBKUS6S3300958879', 'user-001-uuid-12345', 0, '2024-01-15 10:30:00', '2024-01-15 10:30:00', 'admin', 'admin'),
('US64SVBKUS6S3300958881', 'user-001-uuid-12345', 0, '2024-09-05 13:55:10', '2024-09-05 13:55:10', 'registration', 'registration'),
('DE89370400440532013000', 'user-001-uuid-12345', 0, '2024-02-20 14:45:30', '2024-02-25 09:15:20', 'system', 'john_doe'),
('GB29NWBK60161331926819', 'user-003-uuid-11111', 1, '2024-03-10 08:20:15', '2024-03-12 16:40:55', 'admin', 'jane_smith'),
('CH9300762011623852957', 'user-004-uuid-22222', 0, '2024-04-05 11:55:40', '2024-04-05 11:55:40', 'migrate_tool', 'migrate_tool'),
('US64SVBKUS6S3300958880', 'user-005-uuid-33333', 2, '2024-05-18 07:10:25', '2024-06-02 13:25:10', 'api_service', 'bob_wilson'),
('FR1420041010050500013M02606', 'user-006-uuid-44444', 0, '2024-06-22 15:30:45', '2024-06-22 15:30:45', 'admin', 'admin'),
('JP1234567890123456789012', 'user-007-uuid-55555', 1, '2024-07-08 12:15:30', '2024-07-15 10:45:20', 'bulk_import', 'alice_brown'),
('CA123456789012345678901234567890', 'user-008-uuid-66666', 0, '2024-08-12 09:40:15', '2024-08-12 09:40:15', 'system', 'system'),
('AU1234567890123456', 'user-009-uuid-77777', 3, '2024-08-25 16:20:00', '2024-09-01 14:30:25', 'admin', 'charlie_davis');
