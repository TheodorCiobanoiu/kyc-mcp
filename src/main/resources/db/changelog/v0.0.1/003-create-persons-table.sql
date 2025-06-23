--liquibase formatted sql

--changeset theociobanoiu:7
--comment: Create persons table
CREATE TABLE persons
(
    id                    BIGSERIAL PRIMARY KEY,
    first_name            VARCHAR(255)      NOT NULL,
    last_name             VARCHAR(255)      NOT NULL,
    email                 VARCHAR(255),
    phone                 VARCHAR(50),
    date_of_birth         DATE,
    nationality           VARCHAR(100),
    identification_number VARCHAR(100),
    relationship_type     relationship_type NOT NULL,
    position_title        VARCHAR(255),
    ownership_percentage  DECIMAL(5, 2),
    address               TEXT,
    created_at            TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    client_id             BIGINT            NOT NULL,
    CONSTRAINT fk_persons_client FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);
--rollback DROP TABLE IF EXISTS persons;