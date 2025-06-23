--liquibase formatted sql

--changeset theociobanoiu:1
CREATE SCHEMA IF NOT EXISTS kyc_data;
--rollback DROP SCHEMA IF EXISTS kyc_data CASCADE;
