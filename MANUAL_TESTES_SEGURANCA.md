# 📖 Manual Prático para Execução de Testes de Segurança (Java / Spring Boot)

Este manual fornece o passo a passo detalhado para executar análises e testes de segurança estáticos (**SAST**) e dinâmicos (**DAST com OWASP ZAP**) na aplicação **Oficina API**.

---

## 📑 Sumário
1. [Análise Estática de Código e Dependências (SAST)](#1-análise-estática-de-código-e-dependências-sast)
2. [Análise Dinâmica com OWASP ZAP (DAST)](#2-análise-dinâmica-com-owasp-zap-dast)
3. [Testes Manuais de Segurança e Pentest por Endpoint](#3-testes-manuais-de-segurança-e-pentest-por-endpoint)
4. [Relatório e Evidências para Entregáveis](#4-relatório-e-evidências-para-entregáveis)

---

## 🛠️ 1. Análise Estática de Código e Dependências (SAST)

### A) Scan de Vulnerabilidades em Dependências Maven (OWASP Dependency-Check)

O OWASP Dependency-Check analisa o arquivo `pom.xml` e identifica bibliotecas e transitividades que possuem vulnerabilidades conhecidas (CVEs).

1. **Execução no Terminal (PowerShell / Windows):**
   > ⚠️ **Nota NVD API Key:** Recentemente, o banco oficial NVD (NIST) passou a exigir uma chave de API para atualização dos registros de CVEs. Caso não possua a chave configurada, utilize o parâmetro `-DautoUpdate=false -DfailOnError=false` para utilizar a base local sem interromper o build.

   - **Sem chave NVD (Recomendado para execução rápida):**
     ```powershell
     .\mvnw.cmd org.owasp:dependency-check-maven:check -DautoUpdate=false -DfailOnError=false
     ```

   - **Com chave NVD API Key (caso possua chave em nvd.nist.gov):**
     ```powershell
     .\mvnw.cmd org.owasp:dependency-check-maven:check -DnvdApiKey=SUA_CHAVE_AQUI
     ```

2. **Relatório Gerado:**
   O relatório HTML detalhado será salvo em:
   `target/dependency-check-report.html`

3. **Como Interpretar:**
   - Abra o arquivo `dependency-check-report.html` em qualquer navegador.
   - Verifique a coluna **CVSS Score** (vulnerabilidades com pontuação >= 7.0 devem ter suas bibliotecas atualizadas no `pom.xml`).

---

### B) Análise Estática de Código Fonte (SonarLint / SonarQube / SpotBugs)

Para verificar más práticas de codificação, injeções, senhas *hardcoded* ou falhas de concorrência no código Java:

1. **Via IDE (VS Code / IntelliJ):**
   - Instale a extensão **SonarLint**.
   - Abra a pasta do projeto. O SonarLint analisará automaticamente todas as classes em `src/main/java`.

2. **Via Maven (SpotBugs Plugin - Opcional):**
   ```powershell
   .\mvnw.cmd spotbugs:check
   ```

---

## ⚡ 2. Análise Dinâmica com OWASP ZAP (DAST)

O **OWASP ZAP (Zed Attack Proxy)** realiza uma varredura ativa simulação de ataques reais contra a aplicação em execução.

### Pré-requisito: Subir a Aplicação
Certifique-se de que a aplicação está rodando:
```powershell
docker-compose up -d --build
```
A API estará acessível em `http://localhost:8080`.

---

### Opção A: Execução via Docker (PowerShell / Windows)

No PowerShell do Windows, utilize as aspas em `"${PWD}:/zap/wrk/:rw"` para evitar o erro `invalid reference format`:

```powershell
docker run --rm -v "${PWD}:/zap/wrk/:rw" -t ghcr.io/zaproxy/zaproxy:stable zap-api-scan.py -t http://host.docker.internal:8080/v3/api-docs -f openapi -r relatorio_owasp_zap.html
```

- O arquivo `relatorio_owasp_zap.html` será gerado automaticamente na raiz do projeto.

---

### Opção B: Execução via Interface Gráfica do OWASP ZAP

1. **Baixar e Abrir o OWASP ZAP:**
   Instale o ZAP Desktop via [zaproxy.org](https://www.zaproxy.org/download/).

2. **Importar Especificação OpenAPI (Swagger):**
   - Vá no menu: `Import` ➔ `An OpenAPI definition from a URL`.
   - Insira a URL: `http://localhost:8080/v3/api-docs`
   - O ZAP identificará automaticamente todas as rotas da API (`/auth/login`, `/clientes`, `/veiculos`, `/ordens-servico`, etc.).

3. **Configurar Autenticação Bearer Token (JWT):**
   - No ZAP, vá em `HTTP Sessions` ou adicione um cabeçalho customizado na árvore do site:
   - Faça login via `POST http://localhost:8080/auth/login` (credenciais: `admin@oficina.com` / `Admin@123`).
   - Copie o token retornado e adicione o cabeçalho global: `Authorization: Bearer <SEU_TOKEN_JWT>`.

4. **Executar o Scan Ativo (Active Scan):**
   - Clique com o botão direito na nó raiz `http://localhost:8080` na aba *Sites*.
   - Selecione `Attack` ➔ `Active Scan`.
   - Clique em `Start Scan`.

5. **Exportar Relatório:**
   - Vá em `Report` ➔ `Generate Report...`
   - Escolha o formato **HTML** ou **PDF** para anexar à documentação da entrega.

---

## 🧪 3. Testes Manuais de Segurança e Pentest por Endpoint

### A) Teste de Autenticação e Controle de Acesso (Broken Authentication & Authorization)

1. **Acesso sem Token JWT:**
   - Tente realizar um `GET /ordens-servico` sem o cabeçalho `Authorization`.
   - **Resultado Esperado:** Código `401 Unauthorized`.

2. **Acesso com Token Forjado ou Expirado:**
   - Insira `Authorization: Bearer token_invalido_123`.
   - **Resultado Esperado:** Código `401 Unauthorized`.

---

### B) Teste de Injeção SQL (SQL Injection)

1. Tente cadastrar um cliente com payload SQL malicioso:
   - `POST /clientes`
   ```json
   {
     "nome": "João ' OR '1'='1",
     "cpfCnpj": "52998224725",
     "email": "teste@email.com",
     "telefone": "11999999999"
   }
   ```
   - **Resultado Esperado:** A aplicação trata o campo como string literal sanitizada via JPA/Hibernate e salva com segurança ou falha na validação, sem quebrar o SGBD.

---

### C) Teste de Validação de Dados Sensíveis (Input Sanitization)

1. **CPF/CNPJ Inválido:**
   - `POST /clientes` com `cpfCnpj: "111.111.111-11"`.
   - **Resultado Esperado:** Código `400 Bad Request` disparado pela anotação `@CpfCnpj`.

2. **Placa de Veículo Inválida:**
   - `POST /veiculos` com `placa: "PLACA-INVALIDA"`.
   - **Resultado Esperado:** Código `400 Bad Request` disparado pela anotação `@PlacaVeiculo`.

---

## 📊 4. Relatório e Evidências para Entregáveis

Para o documento final em PDF de submissão da Fase 1, recomenda-se incluir:

1. **printscreen/imagem** do resumo do scan do OWASP ZAP (ou o HTML exportado).
2. **Declaração de Conformidade:** Afirmando que 0 vulnerabilidades de nível Alto ou Crítico foram encontradas.
3. Referência ao arquivo **[RELATORIO_SEGURANCA_OWASP_ZAP.md](./RELATORIO_SEGURANCA_OWASP_ZAP.md)** do repositório.
