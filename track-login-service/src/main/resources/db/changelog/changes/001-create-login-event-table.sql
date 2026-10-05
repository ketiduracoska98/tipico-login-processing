--liquibase formatted sql

--changeset keti:001

CREATE TABLE IF NOT EXISTS login_event (
                             message_id UUID PRIMARY KEY,
                             customer_id UUID NOT NULL,
                             username VARCHAR(255) NOT NULL,
                             client VARCHAR(20) NOT NULL,
                             timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
                             customer_ip VARCHAR(45) NOT NULL
);

CREATE INDEX IF NOT EXISTS login_event_customer_id_idx
    ON login_event (customer_id);