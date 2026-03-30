INSERT INTO categoria (nome)
SELECT 'Membros Superiores'
WHERE NOT EXISTS (
    SELECT 1
    FROM categoria
    WHERE LOWER(nome) LIKE '%superior%'
);

INSERT INTO categoria (nome)
SELECT 'Membros Inferiores'
WHERE NOT EXISTS (
    SELECT 1
    FROM categoria
    WHERE LOWER(nome) LIKE '%inferior%'
);

INSERT INTO planos (nome, descricao, preco)
SELECT 'Basico', 'Plano inicial', 49.90
WHERE NOT EXISTS (
    SELECT 1
    FROM planos
    WHERE LOWER(nome) = 'basico'
);

INSERT INTO planos (nome, descricao, preco)
SELECT 'Plus', 'Plano com cobertura estendida', 89.90
WHERE NOT EXISTS (
    SELECT 1
    FROM planos
    WHERE LOWER(nome) = 'plus'
);
