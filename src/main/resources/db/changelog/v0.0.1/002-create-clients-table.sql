--liquibase formatted sql

--changeset theociobanoiu:4
--comment: Create clients table
CREATE TABLE clients
(
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    client_type VARCHAR(50) NOT NULL,
    email               VARCHAR(255),
    phone               VARCHAR(50),
    registration_number VARCHAR(100),
    risk_level  VARCHAR(50) NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE IF EXISTS clients;