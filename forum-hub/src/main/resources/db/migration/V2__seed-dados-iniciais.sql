-- V2: Dados iniciais para desenvolvimento e testes

-- Perfil padrão
INSERT INTO perfis (nome) VALUES ('ROLE_USER');

-- Curso de exemplo
INSERT INTO cursos (nome, categoria) VALUES ('Spring Boot 3', 'Back-end');
INSERT INTO cursos (nome, categoria) VALUES ('Java com Spring', 'Back-end');

-- Usuário admin (senha: 123456)
-- Hash BCrypt gerado com BCryptPasswordEncoder.encode("123456")
INSERT INTO usuarios (nome, email, senha) VALUES (
    'Admin',
    'admin@forum.com',
    '$2a$12$a7.9pSRY7zEzajJjNPOqQeQ9JElC04GOz5Mx8qvuuXRJGqVe/BGTW'
);

-- Usuário de teste (senha: 123456)
INSERT INTO usuarios (nome, email, senha) VALUES (
    'Usuário Teste',
    'teste@forum.com',
    '$2a$12$a7.9pSRY7zEzajJjNPOqQeQ9JElC04GOz5Mx8qvuuXRJGqVe/BGTW'
);

-- Associar perfil aos usuários
INSERT INTO usuario_perfis (usuario_id, perfil_id) VALUES (1, 1);
INSERT INTO usuario_perfis (usuario_id, perfil_id) VALUES (2, 1);
