# 🧪 Roteiro de Testes e Validação do Sistema (Oficina API)

Este documento contém o passo a passo completo para testar e validar todas as funcionalidades da aplicação **Oficina API**, desde a autenticação até a entrega do veículo e consulta aos relatórios de tempo médio.

Os testes podem ser executados via **Swagger UI** (`http://localhost:8080/swagger-ui.html`), **Postman** ou ferramentas como **cURL**.

---

## 🔑 1. Credenciais de Teste Pré-cadastradas

Para realizar requisições autenticadas, utilize os usuários inicializados automaticamente pelo script `init-scripts/02-seed.sql`:

| Perfil | E-mail | Senha |
| :--- | :--- | :--- |
| **Administrador** | `admin@oficina.com` | `Admin@123` |
| **Atendente** | `atendente@oficina.com` | `Admin@123` |
| **Mecânico** | `mecanico@oficina.com` | `Admin@123` |

---

## 🚀 2. Passo a Passo do Fluxo de Testes

### Step 1: Autenticação JWT (Login)
Obter o token de autenticação para incluir nos cabeçalhos das requisições protegidas (`Authorization: Bearer <TOKEN>`).

- **Endpoint:** `POST /auth/login`
- **Body:**
  ```json
  {
    "email": "admin@oficina.com",
    "senha": "Admin@123"
  }
  ```
- **Resultado Esperado:** Retorno `200 OK` com o campo `token` JWT.

---

### Step 2: Cadastrar Cliente com Validação de CPF/CNPJ

1. **Teste de Cadastro Válido:**
   - **Endpoint:** `POST /clientes`
   - **Body:**
     ```json
     {
       "nome": "Carlos Andrade",
       "cpfCnpj": "11144477735",
       "email": "carlos@email.com",
       "telefone": "11977776666"
     }
     ```
   - **Resultado Esperado:** Retorno `201 Created` com o ID do cliente gerado.

2. **Teste de Validação de CPF Inválido (Tratamento de Erro):**
   - **Endpoint:** `POST /clientes`
   - **Body:**
     ```json
     {
       "nome": "Teste Invalido",
       "cpfCnpj": "12345678900",
       "email": "erro@email.com",
       "telefone": "11900000000"
     }
     ```
   - **Resultado Esperado:** Retorno `400 Bad Request` informando `"CPF ou CNPJ inválido"`.

---

### Step 3: Cadastrar Veículo com Validação de Placa

1. **Teste de Cadastro Válido (Padrão Mercosul):**
   - **Endpoint:** `POST /veiculos`
   - **Body:**
     ```json
     {
       "placa": "BRA2E19",
       "marca": "Chevrolet",
       "modelo": "Onix",
       "ano": 2023,
       "clienteId": 1
     }
     ```
   - **Resultado Esperado:** Retorno `201 Created` vinculando o veículo ao cliente ID `1`.

2. **Teste de Validação de Placa Inválida:**
   - **Endpoint:** `POST /veiculos`
   - **Body:**
     ```json
     {
       "placa": "123-INVALIDA",
       "marca": "Fiat",
       "modelo": "Uno",
       "ano": 2020,
       "clienteId": 1
     }
     ```
   - **Resultado Esperado:** Retorno `400 Bad Request` informando o erro de validação de placa.

---

### Step 4: Fluxo da Ordem de Serviço (Ciclo de Vida Completo)

#### A) Criar Ordem de Serviço
- **Endpoint:** `POST /ordens-servico`
- **Body:**
  ```json
  {
    "veiculoId": 1,
    "descricaoProblema": "Motor falhando em marcha lenta e barulho no freio"
  }
  ```
- **Resultado Esperado:** Retorno `201 Created` com status inicial **`RECEBIDA`** e `valorTotal: 0.0`.

---

#### B) Mover para Diagnóstico
- **Endpoint:** `PATCH /ordens-servico/{id}/status`
- **Body:**
  ```json
  {
    "status": "EM_DIAGNOSTICO"
  }
  ```
- **Resultado Esperado:** Retorno `200 OK` com status atualizado para **`EM_DIAGNOSTICO`**.

---

#### C) Adicionar Serviço e Peça (Com Baixa no Estoque)

1. **Adicionar Serviço:**
   - **Endpoint:** `POST /ordens-servico/{id}/servicos`
   - **Body:** `{"servicoId": 1}` (Troca de Óleo - R$ 150,00)
   - **Resultado Esperado:** Retorno `200 OK` recalculando o `valorTotal`.

2. **Adicionar Peça:**
   - **Endpoint:** `POST /ordens-servico/{id}/pecas`
   - **Body:** `{"pecaId": 1, "quantidade": 2}` (Filtro de Óleo - R$ 45,00 cada)
   - **Resultado Esperado:** Retorno `200 OK` com acréscimo de R$ 90,00 e abate de 2 unidades do estoque da peça.

---

#### D) Enviar Orçamento para Aprovação do Cliente
- **Endpoint:** `PATCH /ordens-servico/{id}/status`
- **Body:** `{"status": "AGUARDANDO_APROVACAO"}`
- **Resultado Esperado:** Status alterado para **`AGUARDANDO_APROVACAO`**.

---

#### E) Aprovar Orçamento
- **Endpoint:** `PATCH /ordens-servico/{id}/status`
- **Body:** `{"status": "APROVADA"}`
- **Resultado Esperado:** Status alterado para **`APROVADA`**.

---

#### F) Iniciar Execução (Registro Automático de Data Início)
- **Endpoint:** `PATCH /ordens-servico/{id}/status`
- **Body:** `{"status": "EM_EXECUCAO"}`
- **Resultado Esperado:** Status alterado para **`EM_EXECUCAO`** e campo `dataInicioExecucao` preenchido com a data/hora atual.

---

#### G) Finalizar Ordem de Serviço (Registro Automático de Data Fim)
- **Endpoint:** `PATCH /ordens-servico/{id}/status`
- **Body:** `{"status": "FINALIZADA"}`
- **Resultado Esperado:** Status alterado para **`FINALIZADA`** e campo `dataFinalizacao` preenchido.

---

#### H) Entregar Veículo ao Cliente
- **Endpoint:** `PATCH /ordens-servico/{id}/status`
- **Body:** `{"status": "ENTREGUE"}`
- **Resultado Esperado:** Status alterado para **`ENTREGUE`**.

---

### Step 5: Consultar Relatório de Tempo Médio de Execução

- **Endpoint:** `GET /ordens-servico/relatorios/tempo-medio-execucao`
- **Resultado Esperado:** Retorno `200 OK` contendo os indicadores acumulados:
  ```json
  {
    "totalOrdensFinalizadas": 1,
    "tempoMedioEmMinutos": 5.5,
    "tempoMedioFormatado": "5 minuto(s)"
  }
  ```

---

### Step 6: Teste de Cancelamento e Estorno de Estoque

1. Crie uma nova OS (`POST /ordens-servico`) e mova para `EM_DIAGNOSTICO`.
2. Adicione 3 unidades da Peça ID `1` (`POST /ordens-servico/{id}/pecas`).
3. Cancele a Ordem de Serviço:
   - **Endpoint:** `PATCH /ordens-servico/{id}/status`
   - **Body:** `{"status": "CANCELADA"}`
4. Verifique a peça via `GET /pecas/1`.
   - **Resultado Esperado:** O estoque da peça deve ter sido restaurado com a devolução automática das 3 unidades.
