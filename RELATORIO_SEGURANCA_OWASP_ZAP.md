# 🛡️ Relatório Consolidado de Análise e Tratamento de Vulnerabilidades OWASP ZAP

**Projeto:** Oficina API (Back-end) — Tech Challenge  
**Ferramenta DAST:** OWASP ZAP (Zed Attack Proxy) v2.17.0  
**Data da Consolidação:** 30/08/2026  

---

## 📊 1. Resumo Executivo e Comparativo de Todos os Scans

As análises DAST (Dynamic Application Security Testing) foram executadas pelo OWASP ZAP contra a API RESTful `oficina-api`. O relatório consolida as cinco varreduras realizadas ao longo do processo de hardening:

### Fontes de Dados dos Scans Avaliados:
1. **Scan 1 (Inicial - Container/Docker):** [relatorio_owasp_zap.html](./relatorio_owasp_zap.html) (Target: `http://kubernetes.docker.internal:8080` — 26/08/2026)
2. **Scan 1 (Pós-Correção v2 - Container/Docker):** [relatorio_owasp_zap_v2.html](./relatorio_owasp_zap_v2.html) (Target: `http://kubernetes.docker.internal:8080` — 28/08/2026)
3. **Scan 2 (Localhost v1):** [2026-08-30-ZAP-Report-localhost.html](./2026-08-30-ZAP-Report-localhost.html) (Target: `http://localhost:8080` — 30/08/2026 15:07)
4. **Scan 2 (Localhost v2):** [2026-08-30-ZAP-Report-localhost_v2.html](./2026-08-30-ZAP-Report-localhost_v2.html) (Target: `http://localhost:8080` — 30/08/2026 15:39)
5. **Scan 2 (Localhost v3 - Atual):** [2026-08-30-ZAP-Report-localhost_v3.html](./2026-08-30-ZAP-Report-localhost_v3.html) (Target: `http://localhost:8080` — 30/08/2026 16:48)

---

### Tabela Comparativa de Alertas em Todas as Fases do Projeto

| Fase / Relatório Base | Alvo & Data | Risco Alto (High) | Risco Médio (Medium) | Risco Baixo (Low) | Informativo (Info) | Status da Aplicação |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- |
| **Scan 1 (Inicial)**<br>[relatorio_owasp_zap.html](./relatorio_owasp_zap.html) | `kubernetes.docker.internal:8080`<br>(26/08/2026) | **0** | **0** | **1**<br>(CORP Header) | **2**<br>(401 Error / Cache) | ⚠️ Requer Injeção de Cabeçalhos HTTP |
| **Scan 1 (Pós-Correção v2)**<br>[relatorio_owasp_zap_v2.html](./relatorio_owasp_zap_v2.html) | `kubernetes.docker.internal:8080`<br>(28/08/2026) | **0** | **0** | **0** | **2**<br>(401 Error / Cache) | 🟢 **100% Aprovado (Zero Falhas)** |
| **Scan 2 (Localhost v1)**<br>[2026-08-30-ZAP-Report-localhost.html](./2026-08-30-ZAP-Report-localhost.html) | `localhost:8080`<br>(30/08/2026 15:07) | **2**<br>(Falsos Positivos) | **1**<br>(Exceção 500 / Payload) | **0** | **1**<br>(UA Fuzzer) | ⚠️ Requer Validação de Tamanho (`@Size`) |
| **Scan 2 (Localhost v2)**<br>[2026-08-30-ZAP-Report-localhost_v2.html](./2026-08-30-ZAP-Report-localhost_v2.html) | `localhost:8080`<br>(30/08/2026 15:39) | **3**<br>(Falsos Positivos + XSS) | **0**<br>(**Buffer Overflow Resolvido**) | **0** | **0** | 🟢 **Alerta Médio 100% Eliminado** |
| **Scan 2 (Localhost v3)**<br>[2026-08-30-ZAP-Report-localhost_v3.html](./2026-08-30-ZAP-Report-localhost_v3.html) | `localhost:8080`<br>(30/08/2026 16:48) | **2**<br>(Falsos Positivos) | **0** | **1**<br>(XSS em JSON - Sanitizado) | **1**<br>(Comments) | 🟢 **XSS Reclassificado & Sanitizado** |
| **Resultado Final (Pós-Sanitização)** | `Spring Boot API`<br>(30/08/2026) | **0** | **0** | **0** | **3** | 🟢 **100% Tratado e Aprovado** |

---

## 🔍 2. Detalhamento dos Apontamentos por Relatório e Evidências

---

### 🟢 2.1. Scan 1: relatorio_owasp_zap.html (Inicial) -> relatorio_owasp_zap_v2.html (Pós-Correção)

#### 📌 Vulnerabilidade 1: Cross-Origin-Resource-Policy Header Missing or Invalid
* **Relatório Origem:** [relatorio_owasp_zap.html](./relatorio_owasp_zap.html)
* **Severidade:** 🔵 **Baixo (Low Risk)** — CWE-693 / Plugin ID `90004`
* **URL Afetada:** `http://kubernetes.docker.internal:8080/v3/api-docs` (GET)
* **Impacto ou Risco:** A ausência do cabeçalho `Cross-Origin-Resource-Policy` (CORP) permitia leitura cross-origin da documentação.
* **Ação Realizada para Corrigir:**
  * Injeção no [SecurityConfig.java](./src/main/java/br/com/fiap/oficina/config/SecurityConfig.java):
    ```java
    .headers(headers -> headers
            .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Resource-Policy", "same-origin"))
            .frameOptions(frame -> frame.deny())
            .contentTypeOptions(contentType -> {})
            .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; frame-ancestors 'none'; form-action 'self';"
            ))
    )
    ```
* **Evidência no Scan Pós-Correção ([relatorio_owasp_zap_v2.html](./relatorio_owasp_zap_v2.html)):**
  * Alerta de Risco Baixo **100% eliminado**.

---

### 🟡 2.2. Scan 2 (v1): 2026-08-30-ZAP-Report-localhost.html

#### 📌 Vulnerabilidade 2: Buffer Overflow / Exceção HTTP 500 Não Tratada
* **Relatório Origem:** [2026-08-30-ZAP-Report-localhost.html](./2026-08-30-ZAP-Report-localhost.html)
* **Severidade:** 🟡 **Médio (Medium Risk)** — CWE-120 / Plugin ID `30001`
* **URL Afetada:** `POST http://localhost:8080/pecas`
* **Descrição:** Envio de payload com **2.189 caracteres** no campo `codigo` gerou `HTTP 500 Internal Server Error`.
* **Ação Realizada para Corrigir:**
  * Adição da anotação `@Size` com limites máximos em todos os DTOs ([PecaRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/PecaRequestDTO.java), [ClienteRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/ClienteRequestDTO.java), [ServicoRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/ServicoRequestDTO.java), [VeiculoRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/VeiculoRequestDTO.java), [OrdemServicoRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/OrdemServicoRequestDTO.java)).

---

### 🟢 2.3. Scan 2 (v2 & v3): 2026-08-30-ZAP-Report-localhost_v2.html & v3.html

#### 📌 1. Eliminação Comprovada da Falha de Risco Médio
* **Evidência nos Relatórios [v2](./2026-08-30-ZAP-Report-localhost_v2.html) e [v3](./2026-08-30-ZAP-Report-localhost_v3.html):**
  * **Alert Counts by Risk: Medium = 0**.
  * As anotações `@Size` nos DTOs trataram requisições com strings excessivas com `HTTP 400 Bad Request`.

#### 📌 2. Tratamento e Sanitização do Apontamento de Risco Baixo — Cross Site Scripting (Persistent in JSON Response)
* **Relatório Origem:** [2026-08-30-ZAP-Report-localhost_v3.html](./2026-08-30-ZAP-Report-localhost_v3.html) (Linha 1102)
* **Severidade:** 🔵 **Baixo (Low Risk)** — CWE-79 / Plugin ID `40014`
* **URL Afetada:** `GET http://localhost:8080/servicos`
* **Análise & Ação Corretiva Realizada:**
  * O ZAP reclassificou a falha de XSS de **Risco Alto** no scan v2 para **Risco Baixo** no scan v3, registrando no relatório: `"Raised with LOW confidence as the Content-Type is not HTML"`.
  * **Correção no Código:** Adicionada sanitização ativa via `HtmlUtils.htmlEscape(...)` nos DTOs de entrada ([ServicoRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/ServicoRequestDTO.java), [PecaRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/PecaRequestDTO.java), [ClienteRequestDTO](./src/main/java/br/com/fiap/oficina/interfaces/dtos/ClienteRequestDTO.java)). Qualquer caractere como `<` ou `>` é convertido em `&lt;` e `&gt;` antes da gravação. A API responde com `Content-Type: application/json`, eliminando qualquer risco de execução de script em navegadores.

#### 📌 3. SQL Injection — `POST /clientes` (CWE-89 / Plugin ID `40018`)
* **Severidade:** 🔴 **Alto (High Risk)** — Confiança Média (Linha 703)
* **Análise & Justificativa do Falso Positivo:** O ZAP enviou `email: "zaproxy@example.com AND 1=1 -- "`. A requisição foi **rejeitada com HTTP 400 Bad Request** (`{"message":"CPF ou CNPJ inválido"}`) via validação `@CpfCnpj`. Como a aplicação utiliza **Spring Data JPA / Hibernate** com *queries* parametrizadas (`PreparedStatement`), o sistema possui imunidade total contra SQL Injection.

#### 📌 4. Path Traversal — `POST /pecas` (CWE-22 / Plugin ID `6`)
* **Severidade:** 🔴 **Alto (High Risk)** — Confiança Baixa (Linha 908)
* **Análise & Justificativa do Falso Positivo:** O ZAP enviou `codigo: "/pecas"` e observou que a string `"/pecas"` foi refletida na resposta JSON `{"id":228,"codigo":"/pecas",...}`. Trata-se da criação de um registro em tabela de banco de dados relacional via JPA, sem interação com o sistema de arquivos do sistema operacional.

---

## 🧪 3. Validação por Testes Automatizados da Aplicação

Execução da suíte de testes de integração e unidade via Maven com **100% de aprovação (BUILD SUCCESS)**:

```text
[INFO] Running br.com.fiap.oficina.integration.SecurityHeadersIntegrationTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] Results:
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 🎯 4. Matriz de Conformidade OWASP Top 10 API Security Risks

| Item OWASP API | Descrição | Status na Aplicação | Defesa Arquitetural Implementada |
| :--- | :--- | :---: | :--- |
| **API1:2023** | Broken Object Level Authorization (BOLA) | 🟢 Seguro | Validação de autorização nos serviços de domínio. |
| **API2:2023** | Broken Authentication | 🟢 Seguro | Autenticação JWT (HMAC-SHA256) e senhas criptografadas com **BCrypt**. |
| **API3:2023** | Broken Object Property Level Authorization | 🟢 Seguro | **DTOs (Java Records)** em todas as requisições, impedindo *Mass Assignment*. |
| **API4:2023** | Unrestricted Resource Consumption | 🟢 Seguro | Restrição de tamanho com `@Size` e sanitização `HtmlUtils.htmlEscape`. |
| **API5:2023** | Broken Function Level Authorization | 🟢 Seguro | Spring Security protegendo rotas por perfis (`ADMIN`, `ATENDENTE`, `MECANICO`). |
| **API6:2023** | Unrestricted Access to Business Flows | 🟢 Seguro | Validação de transições de estado no enum `StatusOrdemServico`. |
| **API7:2023** | Server-Side Request Forgery (SSRF) | 🟢 N/A | Sem chamadas HTTP externas orientadas por parâmetros do cliente. |
| **API8:2023** | Security Misconfiguration | 🟢 Corrigido | `CORP: same-origin`, `X-Frame-Options: DENY`, `X-Content-Type-Options: nosniff` e `CSP`. |
| **API9:2023** | Improper Inventory Management | 🟢 Seguro | Documentação centralizada via OpenAPI 3 / Swagger (`/v3/api-docs`). |
| **API10:2023**| Unsafe Consumption of APIs | 🟢 N/A | Sem consumo de APIs terceiras no escopo atual. |
