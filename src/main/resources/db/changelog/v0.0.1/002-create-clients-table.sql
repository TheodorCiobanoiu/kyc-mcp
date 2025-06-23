--liquibase formatted sql

--changeset theociobanoiu:6
--comment: Create clients table
CREATE TABLE clients
(
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    client_type         client_type  NOT NULL,
    email               VARCHAR(255),
    phone               VARCHAR(50),
    registration_number VARCHAR(100),
    risk_level          risk_level   NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE IF EXISTS clients;