-- ================================================
-- SEED DATA: Usuários de Teste, Clientes, Veículos, Serviços e Peças
-- ================================================

-- 1. Usuários de Teste (Senha criptografada dinamicamente via pgcrypto / BCrypt para "Admin@123")
INSERT INTO usuarios (nome, email, senha, perfil, ativo) VALUES
('Administrador', 'admin@oficina.com', crypt('Admin@123', gen_salt('bf', 10)), 'ADMIN', true),
('Atendente Teste', 'atendente@oficina.com', crypt('Admin@123', gen_salt('bf', 10)), 'ATENDENTE', true),
('Mecânico Teste', 'mecanico@oficina.com', crypt('Admin@123', gen_salt('bf', 10)), 'MECANICO', true)
ON CONFLICT (email) DO NOTHING;

-- 2. Clientes
INSERT INTO clientes (nome, cpf_cnpj, email, telefone) VALUES
('João Silva', '52998224725', 'joao@email.com', '11999998888'),
('Maria Santos', '98765432100', 'maria@email.com', '11988887777')
ON CONFLICT (cpf_cnpj) DO NOTHING;

-- 3. Veículos
INSERT INTO veiculos (placa, marca, modelo, ano, cliente_id) VALUES
('ABC1D23', 'Toyota', 'Corolla', 2022, 1),
('XYZ9876', 'Honda', 'Civic', 2021, 2)
ON CONFLICT (placa) DO NOTHING;

-- 4. Serviços
INSERT INTO servicos (nome, descricao, preco) VALUES
('Troca de Óleo e Filtro', 'Substituição do óleo do motor e elemento filtrante', 150.00),
('Alinhamento e Balanceamento', 'Alinhamento de direção 3D e balanceamento das 4 rodas', 120.00),
('Substituição de Pastilhas de Freio', 'Troca das pastilhas de freio dianteiras', 200.00)
ON CONFLICT (nome) DO NOTHING;

-- 5. Peças e Insumos
INSERT INTO pecas (codigo, nome, descricao, preco, quantidade_estoque) VALUES
('FLT-001', 'Filtro de Óleo', 'Filtro sintético para motor 2.0', 45.00, 20),
('OLE-5W30', 'Óleo Sintético 5W30 (1L)', 'Óleo lubrificante 100% sintético', 55.00, 50),
('PST-FR01', 'Jogo Pastilha Freio', 'Jogo de pastilhas de freio cerâmica', 180.00, 15)
ON CONFLICT (codigo) DO NOTHING;

-- 6. Atualização das sequências (sequences) para evitar colisão de IDs no JPA
SELECT setval('clientes_id_seq', (SELECT COALESCE(MAX(id), 1) FROM clientes));
SELECT setval('veiculos_id_seq', (SELECT COALESCE(MAX(id), 1) FROM veiculos));
SELECT setval('usuarios_id_seq', (SELECT COALESCE(MAX(id), 1) FROM usuarios));
SELECT setval('servicos_id_seq', (SELECT COALESCE(MAX(id), 1) FROM servicos));
SELECT setval('pecas_id_seq', (SELECT COALESCE(MAX(id), 1) FROM pecas));
SELECT setval('ordens_servico_id_seq', (SELECT COALESCE(MAX(id), 1) FROM ordens_servico));