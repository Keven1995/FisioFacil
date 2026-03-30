CREATE TABLE IF NOT EXISTS categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS planos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    descricao VARCHAR(255),
    preco DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    nome_usuario VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    reset_token VARCHAR(255),
    reset_token_expiration TIMESTAMP,
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT uk_usuarios_reset_token UNIQUE (reset_token)
);

CREATE TABLE IF NOT EXISTS membro (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    categoria_id BIGINT,
    CONSTRAINT fk_membro_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);

CREATE TABLE IF NOT EXISTS membro_plano (
    id BIGSERIAL PRIMARY KEY,
    membro_id BIGINT,
    plano_id BIGINT,
    CONSTRAINT fk_membro_plano_membro
        FOREIGN KEY (membro_id) REFERENCES membro (id),
    CONSTRAINT fk_membro_plano_plano
        FOREIGN KEY (plano_id) REFERENCES planos (id)
);
