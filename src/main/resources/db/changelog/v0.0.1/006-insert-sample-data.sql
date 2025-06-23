--liquibase formatted sql

--changeset theociobanoiu:13
--comment: Insert sample clients
INSERT INTO clients (name, client_type, email, phone, registration_number, risk_level, status, notes)
VALUES ('ABC Corporation', 'COMPANY', 'contact@abccorp.com', '+1-555-0123', 'REG001', 'MEDIUM', 'ACTIVE',
        'Software development company'),
       ('John Smith', 'INDIVIDUAL', 'john.smith@email.com', '+1-555-0124', 'ID001', 'LOW', 'ACTIVE',
        'Individual client - low risk'),
       ('High Risk Holdings Ltd', 'COMPANY', 'info@highrisk.com', '+1-555-0125', 'REG002', 'HIGH', 'PENDING_APPROVAL',
        'Company in high-risk jurisdiction');
--rollback DELETE FROM clients WHERE registration_number IN ('REG001', 'ID001', 'REG002');

--changeset theociobanoiu:14
--comment: Insert sample persons
INSERT INTO persons (first_name, last_name, email, phone, date_of_birth, nationality, identification_number,
                     relationship_type, position_title, ownership_percentage, status, address, notes, client_id)
VALUES ('Jane', 'Doe', 'jane.doe@email.com', '+1-555-0201', '1980-05-15', 'US', 'ID123456', 'DIRECTOR', 'CEO', 25.0,
        'VERIFIED', '123 Main St, New York, NY', 'Chief Executive Officer', 1),
       ('Robert', 'Johnson', 'rob.johnson@email.com', '+1-555-0202', '1975-08-22', 'US', 'ID789012', 'SHAREHOLDER',
        'CTO', 30.0, 'VERIFIED', '456 Tech Ave, San Francisco, CA', 'Chief Technology Officer', 1),
       ('Maria', 'Garcia', 'maria.garcia@email.com', '+1-555-0203', '1985-03-10', 'MX', 'ID345678', 'BENEFICIAL_OWNER',
        'Investor', 45.0, 'PENDING_VERIFICATION', '789 Investment Blvd, Austin, TX', 'Major investor', 1);
--rollback DELETE FROM persons WHERE identification_number IN ('ID123456', 'ID789012', 'ID345678');