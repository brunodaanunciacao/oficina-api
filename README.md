# Oficina API

API REST desenvolvida como MVP para gerenciamento de uma oficina mecânica.

O sistema permite controlar clientes, veículos, serviços, peças, estoque e ordens de serviço, incluindo cálculo automático de orçamento, controle de estoque, fluxo de status da ordem de serviço, autenticação JWT e documentação Swagger/OpenAPI.

---

## Tecnologias

- Java 21
- Spring Boot 4.0.7
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- Bean Validation
- PostgreSQL
- Docker
- Docker Compose
- Maven
- Lombok
- Swagger / OpenAPI
- JUnit 5
- Mockito
- MockMvc

---

## Arquitetura

O projeto utiliza uma arquitetura monolítica organizada em camadas.

```text
src/main/java/br/com/fiap/oficina
│
├── application
│   └── services
│
├── config
│
├── domain
│   ├── cliente
│   ├── veiculo
│   ├── servico
│   ├── peca
│   ├── ordemservico
│   └── usuario
│
├── infrastructure
│   └── repositories
│
└── interfaces
    ├── controllers
    ├── dtos
    └── exceptions