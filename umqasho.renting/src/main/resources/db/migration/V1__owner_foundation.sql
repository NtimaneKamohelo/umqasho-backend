CREATE TABLE app_user(
    id UUID PRIMARY KEY,
    firebase_uid VARCHAR(128) NOT NULL,
    email VARCHAR(320) NOT NULL,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,

    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(32),
    -- phone_number_verified BOOLEAN NOT NULL DEFAULT FALSE,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uq_app_user_firebase_uid
        UNIQUE (firebase_uid),

    CONSTRAINT uq_app_user_email
        UNIQUE (email),

    CONSTRAINT chk_app_user_status
        CHECK (
            status IN (
                'ACTIVE',
                'SUSPENDED',
                'DEACTIVATED'
            )
        )
);

CREATE TABLE owner_profile(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,

    business_name VARCHAR(255),
    phone_number VARCHAR(32),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_owner_profile_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id),

    CONSTRAINT uq_owner_profile_user
        UNIQUE (user_id)
);

CREATE TABLE property(
    id UUID PRIMARY KEY,

    owner_id UUID NOT NULL,

    property_name VARCHAR(255) NOT NULL,

    street_address VARCHAR(255) NOT NULL,
    suburb VARCHAR(150),
    city VARCHAR(150) NOT NULL,
    province VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20),
    country VARCHAR(100) NOT NULL DEFAULT 'South Africa',

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_property_owner
        FOREIGN KEY (owner_id)
        REFERENCES app_user(id),

    CONSTRAINT chk_property_status
        CHECK (
            status IN (
                'ACTIVE',
                'INACTIVE',
                'ARCHIVED'
            )
        )
);

CREATE INDEX idx_property_owner
    ON property(owner_id);

 CREATE TABLE unit (
    id UUID PRIMARY KEY,

    property_id UUID NOT NULL,

    unit_number VARCHAR(50) NOT NULL,
    floor INTEGER,

    bedrooms INTEGER NOT NULL DEFAULT 0,
    bathrooms INTEGER NOT NULL DEFAULT 0,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,


    CONSTRAINT fk_unit_property
        FOREIGN KEY (property_id)
        REFERENCES property(id),

    CONSTRAINT uq_unit_property_number
        UNIQUE (property_id, unit_number),

    CONSTRAINT chk_unit_status
       CHECK (
           status IN (
                'VACANT',
                'OCCUPIED',
                'MAINTENANCE',
                'UNAVAILABLE'
           )
    ),

    CONSTRAINT chk_unit_bedrooms
        CHECK (bedrooms >= 0),

    CONSTRAINT chk_unit_bathrooms
        CHECK (bathrooms >= 0)
 );

CREATE INDEX idx_unit_property
    ON unit(property_id);

CREATE TABLE tenant_invitation(
    id UUID PRIMARY KEY,

    property_id UUID NOT NULL,
    phone_number VARCHAR(32)

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    lease_start_date DATE NOT NULL,
    lease_end_date DATE NOT NULL,

    monthly_rent NUMERIC(12,2) NOT NULL,
    deposit_amount NUMERIC(12,2) NOT NULL,

    due_day INTEGER NOT NULL,

    token_has VARCHAR(128) NOT NULL,

    status VARCHAR(30) NOT NULL,

    expires_at TIMESTAMP NOT NULL,
    accepted_at TIMESTAMP,

    created_by UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_invitation_property
        FOREIGN KEY (property_id)
        REFERENCES property(id),

    CONSTRAINT fk_invitation_unit
        FOREIGN KEY (unit_id)
        REFERENCES unit(id),

    CONSTRAINT fk_invitation_created_by
        FOREIGN KEY (created_by)
        REFERENCES app_user(id),

    CONSTRAINT uq_tenant_invitation_token
        UNIQUE (token_hash),

    CONSTRAINT chk_invitation_status
        CHECK (
            status IN (
                'PENDING',
                'ACCEPTED',
                'EXPIRED',
                'REVOKED'
            )
        ),

    CONSTRAINT chk_invitation_dates
        CHECK (lease_end_date > lease_start_date),

    CONSTRAINT chk_invitation_monthly_rent
        CHECK (monthly_rent > 0),

    CONSTRAINT chk_invitation_deposit
        CHECK (deposit_amount >= 0),

    CONSTRAINT chk_invitation_due_day
        CHECK (due_day BETWEEN 1 AND 28),

);

CREATE INDEX idx_tenant_invitation_unit
    ON tenant_invitation(unit_id);

CREATE INDEX idx_tenant_invitation_email
    ON tenant_invitation(email);

CREATE INDEX idx_tenant_invitation_status
    ON tenant_invitation(status);

CREATE TABLE audit_log(
    id UUID PRIMARY KEY,

    actor_user_id UUID,

    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,

    metadata JSONB,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_audit_actor
        FOREIGN KEY (actor_user_id)
        REFERENCES app_user(id)
);




















