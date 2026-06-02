SET session_replication_role = 'replica';
DROP SCHEMA IF EXISTS clinica CASCADE;
SET session_replication_role = 'default';


CREATE SCHEMA clinica;
SET search_path TO clinica;


CREATE TABLE especialidade (
    id_especialidade SERIAL PRIMARY KEY,
    nome_especialidade VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE tipo_exame (
    id_tipo_exame SERIAL PRIMARY KEY,
    nome_exame VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    categoria VARCHAR(50),
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE perfil_usuario (
    id_perfil SERIAL PRIMARY KEY,
    nome_perfil VARCHAR(50) NOT NULL UNIQUE,
    descricao TEXT
);


CREATE TABLE status_agendamento (
    id_status SERIAL PRIMARY KEY,
    nome_status VARCHAR(30) NOT NULL UNIQUE,
    descricao TEXT
);


CREATE TABLE status_tratamento (
    id_status_trat SERIAL PRIMARY KEY,
    nome_status VARCHAR(30) NOT NULL UNIQUE,
    descricao TEXT
);




CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    nome_completo VARCHAR(150) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    id_perfil INTEGER NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_perfil) REFERENCES perfil_usuario(id_perfil)
);

CREATE TABLE paciente (
    id_paciente SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    sexo CHAR(1),
    numero_registro VARCHAR(50) UNIQUE,
    data_primeiro_atendimento DATE,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE RESTRICT
);

CREATE TABLE contato_paciente (
    id_contato SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL,
    tipo_contato VARCHAR(20),
    valor_contato VARCHAR(100) NOT NULL,
    principal BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE CASCADE
);

CREATE TABLE endereco_paciente (
    id_endereco SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL,
    tipo_endereco VARCHAR(30),
    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(50),
    cidade VARCHAR(80),
    estado CHAR(2),
    cep VARCHAR(8),
    principal BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE CASCADE
);

CREATE TABLE profissional_saude (
    id_profissional SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    numero_registro VARCHAR(50) NOT NULL UNIQUE,
    tipo_profissional VARCHAR(50),
    id_especialidade INTEGER,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE RESTRICT,
    FOREIGN KEY (id_especialidade) REFERENCES especialidade(id_especialidade) ON DELETE SET NULL
);

CREATE TABLE horario_profissional (
    id_horario SERIAL PRIMARY KEY,
    id_profissional INTEGER NOT NULL,
    dia_semana INTEGER,
    data_especifica DATE,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    intervalo_minutos INTEGER DEFAULT 30,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_profissional) REFERENCES profissional_saude(id_profissional) ON DELETE CASCADE,
    CHECK ((dia_semana IS NOT NULL AND data_especifica IS NULL) OR 
           (dia_semana IS NULL AND data_especifica IS NOT NULL))
);

CREATE TABLE atendente (
    id_atendente SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_admissao DATE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE RESTRICT
);

CREATE TABLE agendamento (
    id_agendamento SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL,
    id_profissional INTEGER NOT NULL,
    id_atendente INTEGER,
    data_agendamento TIMESTAMP NOT NULL,
    data_consulta DATE NOT NULL,
    hora_consulta TIME NOT NULL,
    motivo_consulta TEXT NOT NULL,
    observacoes TEXT,
    id_status INTEGER NOT NULL DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT,
    FOREIGN KEY (id_profissional) REFERENCES profissional_saude(id_profissional) ON DELETE RESTRICT,
    FOREIGN KEY (id_atendente) REFERENCES atendente(id_atendente) ON DELETE SET NULL,
    FOREIGN KEY (id_status) REFERENCES status_agendamento(id_status),
    UNIQUE(id_profissional, data_consulta, hora_consulta)
);

CREATE TABLE historico_agendamento (
    id_historico SERIAL PRIMARY KEY,
    id_agendamento INTEGER NOT NULL,
    status_anterior INTEGER,
    status_novo INTEGER NOT NULL,
    motivo_alteracao TEXT,
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_agendamento) REFERENCES agendamento(id_agendamento) ON DELETE CASCADE,
    FOREIGN KEY (status_anterior) REFERENCES status_agendamento(id_status),
    FOREIGN KEY (status_novo) REFERENCES status_agendamento(id_status)
);

CREATE TABLE consulta (
    id_consulta SERIAL PRIMARY KEY,
    id_agendamento INTEGER NOT NULL UNIQUE,
    id_paciente INTEGER NOT NULL,
    id_profissional INTEGER NOT NULL,
    data_consulta TIMESTAMP NOT NULL,
    queixa_principal TEXT NOT NULL,
    anamnese TEXT,
    achados_exame_fisico TEXT,
    hipotese_diagnostica TEXT,
    notas_clinicas TEXT,
    peso DECIMAL(5,2),
    altura DECIMAL(3,2),
    pressao_arterial VARCHAR(20),
    frequencia_cardiaca INTEGER,
    temperatura DECIMAL(4,2),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_agendamento) REFERENCES agendamento(id_agendamento) ON DELETE RESTRICT,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT,
    FOREIGN KEY (id_profissional) REFERENCES profissional_saude(id_profissional) ON DELETE RESTRICT
);

CREATE TABLE diagnostico (
    id_diagnostico SERIAL PRIMARY KEY,
    id_consulta INTEGER NOT NULL,
    id_paciente INTEGER NOT NULL,
    descricao_diagnostico TEXT NOT NULL,
    cid_10 VARCHAR(10),
    principal BOOLEAN DEFAULT FALSE,
    data_diagnostico TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_consulta) REFERENCES consulta(id_consulta) ON DELETE RESTRICT,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT
);

CREATE TABLE tratamento (
    id_tratamento SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL,
    id_diagnostico INTEGER,
    id_consulta INTEGER,
    descricao_tratamento TEXT NOT NULL,
    data_inicio DATE NOT NULL,
    data_prevista_fim DATE,
    id_status_trat INTEGER NOT NULL,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT,
    FOREIGN KEY (id_diagnostico) REFERENCES diagnostico(id_diagnostico) ON DELETE SET NULL,
    FOREIGN KEY (id_consulta) REFERENCES consulta(id_consulta) ON DELETE SET NULL,
    FOREIGN KEY (id_status_trat) REFERENCES status_tratamento(id_status_trat)
);

CREATE TABLE medicamento (
    id_medicamento SERIAL PRIMARY KEY,
    nome_comercial VARCHAR(150) NOT NULL,
    nome_generico VARCHAR(150),
    principio_ativo VARCHAR(150) NOT NULL,
    dosagem VARCHAR(50),
    apresentacao VARCHAR(50),
    fabricante VARCHAR(100),
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE prescricao (
    id_prescricao SERIAL PRIMARY KEY,
    id_consulta INTEGER NOT NULL,
    id_paciente INTEGER NOT NULL,
    id_profissional INTEGER NOT NULL,
    numero_prescricao VARCHAR(50) UNIQUE,
    data_prescricao TIMESTAMP NOT NULL,
    data_validade DATE NOT NULL,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_consulta) REFERENCES consulta(id_consulta) ON DELETE RESTRICT,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT,
    FOREIGN KEY (id_profissional) REFERENCES profissional_saude(id_profissional) ON DELETE RESTRICT
);

CREATE TABLE item_prescricao (
    id_item_prescricao SERIAL PRIMARY KEY,
    id_prescricao INTEGER NOT NULL,
    id_medicamento INTEGER NOT NULL,
    dosagem VARCHAR(100) NOT NULL,
    frequencia VARCHAR(100) NOT NULL,
    duracao_dias INTEGER,
    instrucoes_especiais TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_prescricao) REFERENCES prescricao(id_prescricao) ON DELETE CASCADE,
    FOREIGN KEY (id_medicamento) REFERENCES medicamento(id_medicamento) ON DELETE RESTRICT
);

CREATE TABLE receita (
    id_receita SERIAL PRIMARY KEY,
    id_prescricao INTEGER NOT NULL UNIQUE,
    id_paciente INTEGER NOT NULL,
    numero_receita VARCHAR(50) UNIQUE,
    data_emissao TIMESTAMP NOT NULL,
    data_validade DATE NOT NULL,
    assinatura_digital BYTEA,
    impressa BOOLEAN DEFAULT FALSE,
    data_impressao TIMESTAMP,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_prescricao) REFERENCES prescricao(id_prescricao) ON DELETE RESTRICT,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT
);


CREATE TABLE solicitacao_exame (
    id_solicitacao SERIAL PRIMARY KEY,
    id_consulta INTEGER NOT NULL,
    id_paciente INTEGER NOT NULL,
    id_profissional INTEGER NOT NULL,
    id_tipo_exame INTEGER NOT NULL,
    data_solicitacao TIMESTAMP NOT NULL,
    motivo_exame TEXT,
    urgencia VARCHAR(20),
    status_exame VARCHAR(30),
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_consulta) REFERENCES consulta(id_consulta) ON DELETE RESTRICT,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT,
    FOREIGN KEY (id_profissional) REFERENCES profissional_saude(id_profissional) ON DELETE RESTRICT,
    FOREIGN KEY (id_tipo_exame) REFERENCES tipo_exame(id_tipo_exame) ON DELETE RESTRICT
);

CREATE TABLE resultado_exame (
    id_resultado SERIAL PRIMARY KEY,
    id_solicitacao INTEGER NOT NULL UNIQUE,
    data_realizacao TIMESTAMP NOT NULL,
    resultado_texto TEXT,
    resultado_numerico DECIMAL(10,2),
    unidade_medida VARCHAR(50),
    valor_referencia VARCHAR(100),
    arquivo_resultado BYTEA,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_solicitacao) REFERENCES solicitacao_exame(id_solicitacao) ON DELETE RESTRICT
);


CREATE TABLE auditoria_prontuario (
    id_auditoria SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    id_paciente INTEGER NOT NULL,
    tipo_acesso VARCHAR(50),
    data_acesso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descricao TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE RESTRICT,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente) ON DELETE RESTRICT
);

CREATE INDEX idx_paciente_cpf ON paciente(cpf);
CREATE INDEX idx_paciente_numero_registro ON paciente(numero_registro);
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_agendamento_paciente ON agendamento(id_paciente);
CREATE INDEX idx_agendamento_profissional ON agendamento(id_profissional);
CREATE INDEX idx_agendamento_data ON agendamento(data_consulta);
CREATE INDEX idx_consulta_paciente ON consulta(id_paciente);
CREATE INDEX idx_consulta_profissional ON consulta(id_profissional);
CREATE INDEX idx_consulta_data ON consulta(data_consulta);
CREATE INDEX idx_prescricao_paciente ON prescricao(id_paciente);
CREATE INDEX idx_prescricao_data ON prescricao(data_prescricao);
CREATE INDEX idx_diagnostico_paciente ON diagnostico(id_paciente);
CREATE INDEX idx_solicitacao_exame_paciente ON solicitacao_exame(id_paciente);
CREATE INDEX idx_contato_paciente ON contato_paciente(id_paciente);
CREATE INDEX idx_endereco_paciente ON endereco_paciente(id_paciente);

INSERT INTO perfil_usuario (nome_perfil, descricao) VALUES
('Administrador', 'Acesso total ao sistema'),
('Médico', 'Acesso para consultas, diagnósticos e prescrições'),
('Enfermeiro', 'Acesso para acompanhamento de pacientes'),
('Atendente', 'Acesso para agendamentos e cadastro'),
('Paciente', 'Acesso restrito ao próprio prontuário');

INSERT INTO status_agendamento (nome_status, descricao) VALUES
('Agendado', 'Agendamento realizado, aguardando consulta'),
('Realizado', 'Consulta foi realizada'),
('Cancelado', 'Agendamento foi cancelado'),
('Remarcado', 'Agendamento foi remarcado para outra data'),
('Não Compareceu', 'Paciente não compareceu na data marcada');

INSERT INTO status_tratamento (nome_status, descricao) VALUES
('Ativo', 'Tratamento em andamento'),
('Concluído', 'Tratamento foi concluído com sucesso'),
('Suspenso', 'Tratamento suspenso temporariamente'),
('Cancelado', 'Tratamento foi cancelado');

INSERT INTO especialidade (nome_especialidade, descricao, ativo) VALUES
('Cardiologia', 'Especialidade do coração e vasos sanguíneos', TRUE),
('Pediatria', 'Medicina para crianças', TRUE),
('Dermatologia', 'Especialidade da pele', TRUE),
('Oftalmologia', 'Especialidade dos olhos', TRUE),
('Ortopedia', 'Especialidade de ossos e articulações', TRUE),
('Ginecologia', 'Especialidade das mulheres', TRUE),
('Psicologia', 'Saúde mental', TRUE),
('Clínica Geral', 'Atendimento geral', TRUE),
('Neurologia', 'Especialidade do sistema nervoso', TRUE),
('Gastroenterologia', 'Especialidade do sistema digestório', TRUE);

INSERT INTO tipo_exame (nome_exame, descricao, categoria, ativo) VALUES
('Hemograma Completo', 'Análise completa do sangue', 'Laboratorial', TRUE),
('Glicemia', 'Medição de açúcar no sangue', 'Laboratorial', TRUE),
('Colesterol Total', 'Teste de colesterol', 'Laboratorial', TRUE),
('Uroanálise', 'Análise de urina', 'Laboratorial', TRUE),
('Raio-X de Tórax', 'Imagem do tórax', 'Imagem', TRUE),
('Ultrassom Abdominal', 'Imagem do abdômen', 'Imagem', TRUE),
('Eletrocardiograma', 'Teste do coração', 'Funcional', TRUE),
('Teste de Pressão Arterial', 'Medição da pressão', 'Funcional', TRUE),
('Ressonância Magnética', 'Imagem detalhada', 'Imagem', TRUE),
('Tomografia Computadorizada', 'Imagem em 3D', 'Imagem', TRUE);

INSERT INTO medicamento (nome_comercial, nome_generico, principio_ativo, dosagem, apresentacao, fabricante, ativo) VALUES
('Dipirona', 'Dipirona', 'Dipirona', '500mg', 'Comprimido', 'Diversos', TRUE),
('Amoxicilina', 'Amoxicilina', 'Amoxicilina', '500mg', 'Comprimido', 'Diversos', TRUE),
('Ibuprofen', 'Ibuprofeno', 'Ibuprofeno', '200mg', 'Comprimido', 'Diversos', TRUE),
('Omeprazol', 'Omeprazol', 'Omeprazol', '20mg', 'Cápsula', 'Diversos', TRUE),
('Atorvastatina', 'Atorvastatina', 'Atorvastatina', '20mg', 'Comprimido', 'Diversos', TRUE),
('Losartana', 'Losartana', 'Losartana', '50mg', 'Comprimido', 'Diversos', TRUE),
('Metformina', 'Metformina', 'Metformina', '500mg', 'Comprimido', 'Diversos', TRUE),
('Paracetamol', 'Paracetamol', 'Paracetamol', '500mg', 'Comprimido', 'Diversos', TRUE);


CREATE VIEW v_agendamentos_hoje AS
SELECT 
    a.id_agendamento,
    p.numero_registro,
    u_pac.nome_completo AS paciente,
    prof.numero_registro AS registro_profissional,
    u_prof.nome_completo AS profissional,
    e.nome_especialidade,
    a.data_consulta,
    a.hora_consulta,
    a.motivo_consulta,
    sa.nome_status
FROM agendamento a
JOIN paciente p ON a.id_paciente = p.id_paciente
JOIN usuario u_pac ON p.id_usuario = u_pac.id_usuario
JOIN profissional_saude prof ON a.id_profissional = prof.id_profissional
JOIN usuario u_prof ON prof.id_usuario = u_prof.id_usuario
JOIN especialidade e ON prof.id_especialidade = e.id_especialidade
JOIN status_agendamento sa ON a.id_status = sa.id_status
WHERE DATE(a.data_consulta) = CURRENT_DATE
ORDER BY a.hora_consulta;

CREATE VIEW v_prontuario_paciente AS
SELECT 
    p.id_paciente,
    u.nome_completo AS paciente,
    p.cpf,
    p.numero_registro,
    p.data_nascimento,
    c.data_consulta,
    u_prof.nome_completo AS profissional,
    e.nome_especialidade,
    c.queixa_principal,
    d.descricao_diagnostico,
    d.cid_10,
    t.descricao_tratamento,
    t.data_inicio,
    pres.numero_prescricao
FROM paciente p
JOIN usuario u ON p.id_usuario = u.id_usuario
LEFT JOIN consulta c ON p.id_paciente = c.id_paciente
LEFT JOIN diagnostico d ON c.id_consulta = d.id_consulta
LEFT JOIN tratamento t ON p.id_paciente = t.id_paciente
LEFT JOIN prescricao pres ON c.id_consulta = pres.id_consulta
LEFT JOIN profissional_saude prof ON c.id_profissional = prof.id_profissional
LEFT JOIN usuario u_prof ON prof.id_usuario = u_prof.id_usuario
LEFT JOIN especialidade e ON prof.id_especialidade = e.id_especialidade
ORDER BY c.data_consulta DESC;

CREATE VIEW v_consultas_por_mes AS
SELECT 
    DATE_TRUNC('month', c.data_consulta)::DATE AS mes,
    COUNT(c.id_consulta) AS total_consultas,
    COUNT(DISTINCT c.id_paciente) AS total_pacientes,
    COUNT(DISTINCT c.id_profissional) AS total_profissionais
FROM consulta c
GROUP BY DATE_TRUNC('month', c.data_consulta)
ORDER BY mes DESC;

CREATE VIEW v_produtividade_profissional AS
SELECT 
    prof.numero_registro,
    u.nome_completo AS profissional,
    e.nome_especialidade,
    COUNT(c.id_consulta) AS total_consultas,
    DATE_TRUNC('month', c.data_consulta)::DATE AS mes
FROM profissional_saude prof
JOIN usuario u ON prof.id_usuario = u.id_usuario
LEFT JOIN especialidade e ON prof.id_especialidade = e.id_especialidade
LEFT JOIN consulta c ON prof.id_profissional = c.id_profissional
WHERE prof.ativo = TRUE
GROUP BY prof.id_profissional, prof.numero_registro, u.nome_completo, e.nome_especialidade, 
         DATE_TRUNC('month', c.data_consulta)
ORDER BY mes DESC, total_consultas DESC;

CREATE VIEW v_exames_pendentes AS
SELECT 
    se.id_solicitacao,
    u.nome_completo AS paciente,
    te.nome_exame,
    se.data_solicitacao,
    u_prof.nome_completo AS profissional_solicitante,
    se.urgencia,
    se.status_exame
FROM solicitacao_exame se
JOIN paciente p ON se.id_paciente = p.id_paciente
JOIN usuario u ON p.id_usuario = u.id_usuario
JOIN tipo_exame te ON se.id_tipo_exame = te.id_tipo_exame
JOIN profissional_saude prof ON se.id_profissional = prof.id_profissional
JOIN usuario u_prof ON prof.id_usuario = u_prof.id_usuario
WHERE se.status_exame = 'Pendente'
ORDER BY se.urgencia DESC, se.data_solicitacao ASC;

CREATE VIEW v_prescricoes_ativas AS
SELECT 
    pr.id_prescricao,
    u.nome_completo AS paciente,
    u_prof.nome_completo AS profissional,
    m.nome_comercial AS medicamento,
    ip.dosagem,
    ip.frequencia,
    pr.data_prescricao,
    pr.data_validade,
    CASE WHEN pr.data_validade >= CURRENT_DATE THEN 'Válida' ELSE 'Expirada' END AS status_prescricao
FROM prescricao pr
JOIN paciente p ON pr.id_paciente = p.id_paciente
JOIN usuario u ON p.id_usuario = u.id_usuario
JOIN profissional_saude prof ON pr.id_profissional = prof.id_profissional
JOIN usuario u_prof ON prof.id_usuario = u_prof.id_usuario
JOIN item_prescricao ip ON pr.id_prescricao = ip.id_prescricao
JOIN medicamento m ON ip.id_medicamento = m.id_medicamento
ORDER BY pr.data_validade DESC;


SELECT setval('usuario_id_usuario_seq',
    (SELECT MAX(id_usuario) FROM usuario), TRUE);
SELECT setval('paciente_id_paciente_seq', 
    (SELECT MAX(id_paciente) FROM paciente), TRUE);

COMMIT;

