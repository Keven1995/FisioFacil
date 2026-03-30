CREATE TABLE IF NOT EXISTS usuario_assinatura (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    plano_id BIGINT NOT NULL,
    membro_id BIGINT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_usuario_assinatura_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT fk_usuario_assinatura_plano
        FOREIGN KEY (plano_id) REFERENCES planos (id),
    CONSTRAINT fk_usuario_assinatura_membro
        FOREIGN KEY (membro_id) REFERENCES membro (id),
    CONSTRAINT uk_usuario_assinatura_usuario UNIQUE (usuario_id)
);

CREATE TABLE IF NOT EXISTS usuario_exercicio_progresso (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    exercicio_key VARCHAR(120) NOT NULL,
    exercicio_titulo VARCHAR(255) NOT NULL,
    iniciado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_usuario_exercicio_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT uk_usuario_exercicio UNIQUE (usuario_id, exercicio_key)
);
