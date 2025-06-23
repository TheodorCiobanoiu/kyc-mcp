--liquibase formatted sql

--changeset theociobanoiu:10
--comment: Add unique constraints for clients table
ALTER TABLE clients
    ADD CONSTRAINT uk_clients_registration_number UNIQUE (registration_number);
ALTER TABLE clients
    ADD CONSTRAINT uk_clients_email UNIQUE (email);
--rollback ALTER TABLE clients DROP CONSTRAINT IF EXISTS uk_clients_registration_number, uk_clients_email;

--changeset theociobanoiu:11
--comment: Add unique constraints for persons table
ALTER TABLE persons
    ADD CONSTRAINT uk_persons_identification_number UNIQUE (identification_number);
ALTER TABLE persons
    ADD CONSTRAINT uk_persons_email UNIQUE (email);
--rollback ALTER TABLE persons DROP CONSTRAINT IF EXISTS uk_persons_identification_number, uk_persons_email;

--changeset theociobanoiu:12
--comment: Add business rule constraints
ALTER TABLE persons
    ADD CONSTRAINT chk_ownership_percentage CHECK (ownership_percentage >= 0 AND ownership_percentage <= 100);
--rollback ALTER TABLE persons DROP CONSTRAINT IF EXISTS chk_ownership_percentage;