ALTER TABLE usuarios
    ADD COLUMN IF NOT EXISTS reset_token VARCHAR(255);

ALTER TABLE usuarios
    ADD COLUMN IF NOT EXISTS reset_token_expiration TIMESTAMP;

CREATE UNIQUE INDEX IF NOT EXISTS uk_usuarios_reset_token
    ON usuarios (reset_token);
