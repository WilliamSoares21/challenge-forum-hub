-- V3: Corrigir hash BCrypt dos usuários de seed

UPDATE usuarios
SET senha = '$2b$10$3mA/wsno5gSe2NT0NbJFBuOmtD97LDqBe/SMH8jyjFNdy9R6EehAu'
WHERE email IN ('admin@forum.com', 'teste@forum.com');
