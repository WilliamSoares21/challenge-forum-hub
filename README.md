# FórumHub API

API REST para gerenciamento de um fórum de discussões, desenvolvida como desafio da formação **Java e Spring Boot** da Alura.

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.x |
| Spring Security | JWT (HMAC256) |
| Spring Data JPA | Hibernate 6 |
| MySQL | 8+ |
| Flyway | Migrations automáticas |
| Lombok | Redução de boilerplate |

---

## Pré-requisitos

- Java 21+
- Maven 3.9+
- MySQL 8+ rodando localmente (ou via Docker)

---

## Configuração

### 1. Banco de Dados

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE forum_hub;
```

### 2. Variáveis de Ambiente

Defina as variáveis antes de rodar a aplicação:

```bash
export DB_FORUM_HUB_URL=jdbc:mysql://localhost:3306/forum_hub
export DB_FORUM_HUB_USER=root
export DB_FORUM_HUB_PASSWORD=sua_senha
export JWT_FORUM_HUB_SECRET=sua_chave_secreta_jwt_muito_longa_e_segura
```

> **Dica**: Para um ambiente de desenvolvimento, você pode criar um arquivo `.env` e usar uma ferramenta como `direnv`, ou exportar diretamente no terminal.

### 3. Executar a aplicação

```bash
cd forum-hub
./mvnw spring-boot:run
```

O Flyway executará automaticamente o arquivo `V1__create-tables-forum-hub.sql`, criando todas as tabelas no banco de dados.

---

## Endpoints

### Autenticação

#### `POST /login`

Obtém um token JWT. Este é o único endpoint público.

**Request:**
```json
{
  "login": "usuario@email.com",
  "senha": "123456"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

> Todos os demais endpoints exigem o header `Authorization: Bearer <token>`.

---

### Tópicos

#### `POST /topicos` — Cadastrar tópico

**Header:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "titulo": "Dúvida sobre Spring Security",
  "mensagem": "Como configurar autenticação JWT no Spring Boot 3?",
  "idCurso": 1
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "titulo": "Dúvida sobre Spring Security",
  "mensagem": "Como configurar autenticação JWT no Spring Boot 3?",
  "dataCriacao": "2026-03-10T21:00:00",
  "status": "ABERTO",
  "nomeAutor": "João Silva",
  "nomeCurso": "Spring Boot"
}
```

> **Regras de negócio:**
> - Todos os campos são obrigatórios (`titulo`, `mensagem`, `idCurso`).
> - Tópicos com o mesmo título **e** mensagem são rejeitados (`422 Unprocessable Entity`).

---

#### `GET /topicos` — Listar tópicos (paginado)

**Header:** `Authorization: Bearer <token>`

**Query params opcionais:** `?page=0&size=10&sort=dataCriacao`

**Response `200 OK`:**
```json
{
  "content": [
    {
      "id": 1,
      "titulo": "Dúvida sobre Spring Security",
      "mensagem": "...",
      "dataCriacao": "2026-03-10T21:00:00",
      "status": "ABERTO",
      "nomeAutor": "João Silva",
      "nomeCurso": "Spring Boot"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0
}
```

---

#### `GET /topicos/{id}` — Detalhar tópico

**Header:** `Authorization: Bearer <token>`

**Response `200 OK`:** objeto do tópico | `404 Not Found` se não existir.

---

#### `PUT /topicos/{id}` — Atualizar tópico

**Header:** `Authorization: Bearer <token>`

**Request** (todos os campos são opcionais):
```json
{
  "titulo": "Novo título",
  "mensagem": "Mensagem atualizada",
  "status": "SOLUCIONADO"
}
```

**Response `200 OK`:** objeto atualizado | `404 Not Found` se não existir.

> **Status disponíveis:** `ABERTO`, `FECHADO`, `SOLUCIONADO`, `NAO_RESPONDIDO`

---

#### `DELETE /topicos/{id}` — Excluir tópico

**Header:** `Authorization: Bearer <token>`

**Response `204 No Content`** | `404 Not Found` se não existir.

---

## Controle de Acesso

A API utiliza **autenticação stateless com JWT**:

1. O usuário faz `POST /login` com e-mail e senha.
2. A API retorna um token JWT assinado com HMAC256, válido por **2 horas**.
3. O cliente envia o token no header `Authorization: Bearer <token>` em toda requisição protegida.
4. O `SecurityFilter` (filtro `OncePerRequestFilter`) intercepta cada requisição, valida o token e autentica o usuário no `SecurityContextHolder`.
5. Nenhuma sessão HTTP é mantida no servidor (`SessionCreationPolicy.STATELESS`).

---

## Seed de Dados (Desenvolvimento)

A migration **V2** já insere automaticamente os dados iniciais ao subir a aplicação:

| Usuário | E-mail | Senha |
|---|---|---|
| Admin | `admin@forum.com` | `123456` |
| Usuário Teste | `teste@forum.com` | `123456` |

**Cursos disponíveis:** `Spring Boot 3` (id: 1), `Java com Spring` (id: 2)

> Os hashes BCrypt são gerados com custo 10 e corrigidos pela migration **V3**.

---

## Estrutura do Projeto

```
src/main/java/br/forum/forum_hub/
├── controller/
│   ├── AutenticacaoController.java   # POST /login
│   └── TopicoController.java         # CRUD /topicos
├── domain/
│   ├── curso/    Curso, CursoRepository
│   ├── perfil/   Perfil, PerfilRepository
│   ├── resposta/ Resposta, RespostaRepository
│   ├── topico/   Topico, TopicoRepository, DTOs, StatusTopico
│   └── usuario/  Usuario, UsuarioRepository, AutenticacaoService
└── infra/
    ├── exception/ TratadorDeErros (@RestControllerAdvice)
    └── security/  SecurityConfigurations, SecurityFilter, TokenService
```
