--liquibase formatted sql

--changeset theociobanoiu:1
--comment: Create client_type enum
CREATE TYPE client_type AS ENUM ('INDIVIDUAL', 'COMPANY', 'TRUST', 'PARTNERSHIP', 'OTHER');
--rollback DROP TYPE IF EXISTS client_type;

--changeset theociobanoiu:2
--comment: Create risk_level enum
CREATE TYPE risk_level AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');
--rollback DROP TYPE IF EXISTS risk_level;

--changeset theociobanoiu:3
--comment: Create client_status enum
CREATE TYPE client_status AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BLOCKED', 'PENDING_APPROVAL');
--rollback DROP TYPE IF EXISTS client_status;

--changeset theociobanoiu:4
--comment: Create relationship_type enum
CREATE TYPE relationship_type AS ENUM ('OWNER', 'DIRECTOR', 'SHAREHOLDER', 'BENEFICIAL_OWNER', 'AUTHORIZED_SIGNATORY', 'LEGAL_REPRESENTATIVE', 'TRUSTEE', 'PROTECTOR', 'SETTLOR', 'BENEFICIARY', 'PARTNER', 'OTHER');
--rollback DROP TYPE IF EXISTS relationship_type;

--changeset theociobanoiu:5
--comment: Create person_status enum
CREATE TYPE person_status AS ENUM ('ACTIVE', 'INACTIVE', 'VERIFIED', 'PENDING_VERIFICATION', 'REJECTED');
--rollback DROP TYPE IF EXISTS person_status;