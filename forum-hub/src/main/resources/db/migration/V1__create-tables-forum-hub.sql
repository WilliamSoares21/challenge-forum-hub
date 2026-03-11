
CREATE TABLE perfis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE usuario_perfis (
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT fk_usuario_perfis_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT fk_usuario_perfis_perfil FOREIGN KEY (perfil_id) REFERENCES perfis (id)
);

CREATE TABLE cursos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL UNIQUE,
    mensagem TEXT NOT NULL,
    data_criacao DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_topicos_autor FOREIGN KEY (autor_id) REFERENCES usuarios (id),
    CONSTRAINT fk_topicos_curso FOREIGN KEY (curso_id) REFERENCES cursos (id)
);

CREATE TABLE respostas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensagem TEXT NOT NULL,
    topico_id BIGINT NOT NULL,
    data_criacao DATETIME NOT NULL,
    autor_id BIGINT NOT NULL,
    solucao TINYINT(1) DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_respostas_topico FOREIGN KEY (topico_id) REFERENCES topicos (id),
    CONSTRAINT fk_respostas_autor FOREIGN KEY (autor_id) REFERENCES usuarios (id)
);
