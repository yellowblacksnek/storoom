-- liquibase formatted sql

-- changeset sus:1667875394304-1
CREATE TABLE locks
(
    id              UUID         NOT NULL,
    name            VARCHAR(255) NOT NULL,
    manufacturer_id UUID NOT NULL,
    CONSTRAINT pk_locks PRIMARY KEY (id)
);

-- changeset sus:1667875394304-2
CREATE TABLE manufacturers
(
    id   UUID           NOT NULL,
    name VARCHAR(255)   NOT NULL,
    CONSTRAINT pk_manufacturers PRIMARY KEY (id)
);

-- changeset sus:1667875394304-3
ALTER TABLE locks
    ADD CONSTRAINT FK_LOCKS_ON_MANUFACTURER FOREIGN KEY (manufacturer_id) REFERENCES manufacturers (id);

