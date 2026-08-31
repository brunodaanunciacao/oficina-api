CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS veiculos (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(20) UNIQUE NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INT NOT NULL,
    cliente_id INT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS servicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT NOT NULL,
    preco NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS pecas (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    quantidade_estoque INT NOT NULL
);

CREATE TABLE IF NOT EXISTS ordens_servico (
    id SERIAL PRIMARY KEY,
    veiculo_id INT NOT NULL REFERENCES veiculos(id) ON DELETE CASCADE,
    descricao_problema TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,
    data_abertura TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_inicio_execucao TIMESTAMP WITH TIME ZONE,
    data_finalizacao TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS ordem_servico_servicos (
    id SERIAL PRIMARY KEY,
    ordem_servico_id INT NOT NULL REFERENCES ordens_servico(id) ON DELETE CASCADE,
    servico_id INT NOT NULL REFERENCES servicos(id),
    preco NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS ordem_servico_pecas (
    id SERIAL PRIMARY KEY,
    ordem_servico_id INT NOT NULL REFERENCES ordens_servico(id) ON DELETE CASCADE,
    peca_id INT NOT NULL REFERENCES pecas(id),
    quantidade INT NOT NULL,
    preco_unitario NUMERIC(10, 2) NOT NULL,
    subtotal NUMERIC(10, 2) NOT NULL
);