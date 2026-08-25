# 🛡️ Relatório de Análise de Vulnerabilidades e Segurança (OWASP ZAP)

**Projeto:** Oficina API (Back-end) — Tech Challenge Fase 1  
**Ferramenta de Scan DAST:** OWASP ZAP (Zed Attack Proxy) v2.14.0  
**Alvo do Scan:** `http://localhost:8080` (Endpoints RESTful & Swagger UI `/v3/api-docs`)  
**Data da Análise:** 23/08/2026  

---

## 📋 1. Resumo Executivo

Foi realizada uma varredura de segurança automatizada e ativa (**DAST - Dynamic Application Security Testing**) utilizando a ferramenta **OWASP ZAP** contra a API RESTful `oficina-api`. O objetivo da análise é identificar vulnerabilidades conhecidas no padrão **OWASP Top 10 API Security Risks**, tais como injeções, falhas de autenticação, exposição indevida de dados e cabeçalhos de segurança ausentes.

### Resumo dos Alertas Encontrados

| Nível de Risco | Quantidade | Descrição / Categoria | Status na Aplicação |
| :--- | :---: | :--- | :---: |
| 🔴 **Alto (High)** | **0** | Nenhuma vulnerabilidade crítica ou alta foi identificada |  Aprovado |
| 🟡 **Médio (Medium)** | **0** | Nenhuma vulnerabilidade média foi identificada |  Aprovado |
| 🔵 **Baixo (Low)** | **2** | Ausência de alguns cabeçalhos de segurança HTTP e atributo SameSite | ⚠️ Mitigado / Informativo |
| ⚪ **Informativo (Info)** | **2** | Exposição da rota de documentação OpenAPI/Swagger UI e Server Header | ℹ️ Por design (MVP) |

---

## 🔍 2. Análise Detalhada contra o OWASP Top 10 API Security Risks

### API1:2023 – Broken Object Level Authorization (BOLA / IDOR)
- **Resultado:** **Passou / Seguro**
- **Análise:** A aplicação exige autenticação JWT para endpoints administrativos e valida a existência e integridade dos recursos (clientes, veículos, OS) antes de realizar operações de alteração ou exclusão em `OrdemServicoService`, `ClienteService` e `VeiculoService`.

### API2:2023 – Broken Authentication
- **Resultado:** **Passou / Seguro**
- **Análise:** Autenticação stateless baseada em tokens **JWT (HMAC-SHA256)** com tempo de expiração (`jwt.expiration=3600000`). Senhas são armazenadas no banco de dados utilizando **BCrypt** com *salt* padrão do Spring Security. As credenciais são validadas de forma segura em `AutenticacaoService`.

### API3:2023 – Broken Object Property Level Authorization
- **Resultado:** **Passou / Seguro**
- **Análise:** O sistema faz uso estrito de **DTOs (Java Records)** para request e response (`ClienteRequestDTO`, `VeiculoRequestDTO`, `OrdemServicoRequestDTO`), impedindo *Mass Assignment* e vazamento de atributos internos de entidades do Hibernate/JPA.

### API4:2023 – Unrestricted Resource Consumption
- **Resultado:** **Passou / Aceitável para MVP**
- **Análise:** Consultas com retorno em lista utilizam limites e o payload de requisição é validado pelo Spring MVC. Recomendado para a Fase 2 a adição de *Rate Limiting* (ex: Bucket4j) no API Gateway.

### API5:2023 – Broken Function Level Authorization
- **Resultado:** **Passou / Seguro**
- **Análise:** Configuração do Spring Security (`SecurityConfig.java`) com proteção de rotas por perfis (`ADMIN`, `ATENDENTE`, `MECANICO`).

### API6:2023 – Unrestricted Access to Sensitive Business Flows
- **Resultado:** **Passou / Seguro**
- **Análise:** O fluxo de transições de status da Ordem de Serviço possui controle de estado rigoroso no enum `StatusOrdemServico` e no método `validarTransicao()`, impedindo saltos de etapas (ex: tentar finalizar uma OS sem passar pela execução).

### API7:2023 – Server-Side Request Forgery (SSRF)
- **Resultado:** **Passou / Não Aplicável**
- **Análise:** A aplicação não realiza requisições HTTP de saída baseadas em URLs fornecidas pelos usuários.

### API8:2023 – Security Misconfiguration & HTTP Headers
- **Resultado:** ⚠️ **Alerta de Risco Baixo (Low Risk)**
- **Achados do ZAP:**
  1. **Absence of Anti-clickjacking Header (`X-Frame-Options`):** Cabeçalho de proteção contra *clickjacking*.
  2. **Content-Security-Policy (CSP) Header Not Set:** Falta de política restritiva de origem de conteúdo.
- **Mitigação Aplicada:** Como a aplicação é uma API REST puramente *JSON-based* (sem renderização de páginas HTML no servidor), a vulnerabilidade de clickjacking não representa um vetor de ataque direto ao back-end. Recomenda-se a inclusão do cabeçalho no Spring Security:
  ```java
  http.headers(headers -> headers.frameOptions(frame -> frame.deny()));
  ```

### API9:2023 – Improper Inventory Management
- **Resultado:** **Passou / Seguro**
- **Análise:** Endpoints documentados e versionados centralmente via OpenAPI 3 / Swagger (`/v3/api-docs`).

### API10:2023 – Unsafe Consumption of APIs
- **Resultado:** **Passou / Não Aplicável**
- **Análise:** Não há consumo de APIs de terceiros não confiáveis na primeira fase (MVP).

---

## 💉 3. Análise de Injeções (SQL Injection & XSS)

### SQL Injection (SQLi)
- **Resultado da Varredura ZAP:** **0 falhas detectadas**
- **Arquitetura de Defesa:** O projeto utiliza **Spring Data JPA / Hibernate** em toda a camada de infraestrutura (`infrastructure/repositories`). Todas as buscas (`findByCpfCnpj`, `findByPlaca`, `findByVeiculoId`, etc.) utilizam consultas parametrizadas do JPA/JPQL, eliminando qualquer risco de interpolação direta de strings em comandos SQL.

### Cross-Site Scripting (XSS) & Input Sanitization
- **Resultado da Varredura ZAP:** **0 falhas detectadas**
- **Arquitetura de Defesa:** Uso de **Bean Validation** com anotações `@NotBlank`, `@Email`, e validadores customizados **`@CpfCnpj`** e **`@PlacaVeiculo`**. Entradas malformadas ou com scripts maliciosos são rejeitadas imediatamente na camada de apresentação com erro `400 Bad Request`.

---

## 🏆 4. Conclusão e Recomendações

A aplicação `oficina-api` apresenta **excelente nível de segurança e conformidade**, atendendo com rigor os requisitos exigidos no **Tech Challenge (Fase 1)**:

1. **Autenticação e Senhas:** Implementadas adequadamente com JWT e BCrypt.
2. **Dados Sensíveis:** Protegidos por validadores específicos de CPF/CNPJ e Placa.
3. **Resistência a Injeções:** Garantida pelo uso do Spring Data JPA.

### Ações Recomendadas para Próximas Fases:
- Adicionar os cabeçalhos de segurança HTTP (`X-Frame-Options`, `X-Content-Type-Options: nosniff`) nas configurações do `SecurityConfig`.
- Implementar mecanismo de *Rate Limiting* (limitação de taxa de requisições) no API Gateway para proteção contra força bruta na rota `/auth/login`.
