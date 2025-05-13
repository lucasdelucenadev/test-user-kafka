# Serviço de Notificações com Kafka

Este projeto implementa um sistema simples de notificações utilizando Spring Boot, Apache Kafka, PostgreSQL e autenticação JWT.

## Requisitos

- Java 21 ou superior
- Docker e Docker Compose
- Maven
- Postman (opcional, para testes)

## Estrutura do Projeto

O projeto é composto por dois componentes principais:

1. API REST (Spring Boot)
   - Endpoints para gerenciamento de notificações
   - Integração com Kafka para envio de mensagens

2. Worker Kafka
   - Consumidor de mensagens do Kafka
   - Processamento de notificações

## Como Executar

1. **Suba os containers:**
   ```bash
   docker-compose up -d
   ```
2. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

## Autenticação

A API utiliza autenticação JWT.
**Usuário padrão:**
- username: `admin`
- password: `admin123`

---

## Endpoints da API

### 1. Login (obter token JWT)
- **POST** `/auth/login`
- **Body:**
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- **Resposta:**
  ```json
  {
    "token": "SEU_TOKEN_JWT"
  }
  ```

---

### 2. Criar notificação
- **POST** `/api/notifications`
- **Headers:**
  - `Content-Type: application/json`
  - `Authorization: Bearer SEU_TOKEN_JWT`
- **Body:**
  ```json
  {
    "message": "Teste de notificação JWT",
    "recipient": "usuario@exemplo.com"
  }
  ```

---

### 3. Listar notificações
- **GET** `/api/notifications`
- **Headers:**
  - `Authorization: Bearer SEU_TOKEN_JWT`

---

### 4. Buscar notificação por ID
- **GET** `/api/notifications/{id}`
- **Headers:**
  - `Authorization: Bearer SEU_TOKEN_JWT`

---

### 5. Atualizar notificação
- **PUT** `/api/notifications/{id}`
- **Headers:**
  - `Content-Type: application/json`
  - `Authorization: Bearer SEU_TOKEN_JWT`
- **Body:**
  ```json
  {
    "message": "Mensagem atualizada",
    "recipient": "novo@email.com"
  }
  ```

---

### 6. Excluir notificação
- **DELETE** `/api/notifications/{id}`
- **Headers:**
  - `Authorization: Bearer SEU_TOKEN_JWT`

---

## Exemplo de Teste com cURL

```bash
# Login e obtenção do token
curl -X POST http://localhost:8081/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"admin123"}'

# Criar notificação
curl -X POST http://localhost:8081/api/notifications \
-H "Content-Type: application/json" \
-H "Authorization: Bearer SEU_TOKEN_JWT" \
-d '{"message":"Teste JWT","recipient":"usuario@exemplo.com"}'

# Listar notificações
curl -X GET http://localhost:8081/api/notifications \
-H "Authorization: Bearer SEU_TOKEN_JWT"
```

---

## Observações

- **Todos os endpoints (exceto `/auth/login`) exigem o header Authorization com o token JWT.**
- O worker Kafka processa as notificações e atualiza o status para `PROCESSED`.
- Os dados são persistidos no PostgreSQL.
- O projeto já está pronto para deploy em qualquer ambiente Java com Docker e Maven.

---

Se quiser importar uma coleção para o Postman, basta criar as requisições conforme os exemplos acima.