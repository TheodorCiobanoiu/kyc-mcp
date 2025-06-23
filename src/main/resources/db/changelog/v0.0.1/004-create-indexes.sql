--liquibase formatted sql

--changeset theociobanoiu:8
--comment: Create indexes for clients table
CREATE INDEX idx_clients_risk_level ON clients (risk_level);
CREATE INDEX idx_clients_status ON clients (status);
CREATE INDEX idx_clients_client_type ON clients (client_type);
CREATE INDEX idx_clients_registration_number ON clients (registration_number);
CREATE INDEX idx_clients_email ON clients (email);
CREATE INDEX idx_clients_created_at ON clients (created_at);
--rollback DROP INDEX IF EXISTS idx_clients_risk_level, idx_clients_status, idx_clients_client_type, idx_clients_registration_number, idx_clients_email, idx_clients_created_at;

--changeset theociobanoiu:9
--comment: Create indexes for persons table
CREATE INDEX idx_persons_client_id ON persons (client_id);
CREATE INDEX idx_persons_relationship_type ON persons (relationship_type);
CREATE INDEX idx_persons_status ON persons (status);
CREATE INDEX idx_persons_identification_number ON persons (identification_number);
CREATE INDEX idx_persons_email ON persons (email);
CREATE INDEX idx_persons_nationality ON persons (nationality);
CREATE INDEX idx_persons_full_name ON persons (first_name, last_name);
--rollback DROP INDEX IF EXISTS idx_persons_client_id, idx_persons_relationship_type, idx_persons_status, idx_persons_identification_number, idx_persons_email, idx_persons_nationality, idx_persons_full_name;