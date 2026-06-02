# Modelo de Dados - Sistema de Gestão de Clínica Médica
## Normalização até 3ª Forma Normal (3FN)

---

## 1. ESTRUTURA GERAL DO BANCO

### Esquema (Schema)
- **Nome**: `clinica`
- **Objetivo**: Organizar todos os objetos do banco de forma isolada

---

## 2. GRUPOS DE TABELAS

### A. TABELAS DE DOMÍNIO (Dimensões)
Tabelas que contêm valores fixos e reutilizáveis:

#### `especialidade`
- **Descrição**: Tipos de especialidades médicas
- **Atributos Principais**: 
  - `id_especialidade` (PK)
  - `nome_especialidade` (UNIQUE)
  - `descricao`, `ativo`

#### `tipo_exame`
- **Descrição**: Tipos de exames disponíveis
- **Atributos Principais**:
  - `id_tipo_exame` (PK)
  - `nome_exame` (UNIQUE)
  - `categoria` (Laboratorial, Imagem, Funcional)

#### `perfil_usuario`
- **Descrição**: Perfis de acesso ao sistema
- **Atributos Principais**:
  - `id_perfil` (PK)
  - `nome_perfil` (Admin, Médico, Enfermeiro, Atendente, Paciente)

#### `status_agendamento`
- **Descrição**: Status possíveis de agendamento
- **Valores**: Agendado, Realizado, Cancelado, Remarcado, Não Compareceu

#### `status_tratamento`
- **Descrição**: Status possíveis de tratamento
- **Valores**: Ativo, Concluído, Suspenso, Cancelado

---

### B. TABELAS PRINCIPAIS

#### `usuario` ⭐
- **Descrição**: Usuários genéricos do sistema
- **Normalização**: 1FN, 2FN, 3FN
- **Chave Primária**: `id_usuario`
- **Atributos**:
  - `email` (UNIQUE) - Identificador único
  - `senha_hash` - Segurança
  - `nome_completo`
  - `id_perfil` (FK)
  - `ativo`, `data_criacao`, `data_atualizacao`
- **Justificativa 3FN**: Cada atributo depende apenas da PK, sem dependências transitivas

#### `paciente` ⭐
- **Descrição**: Dados específicos do paciente
- **Chave Primária**: `id_paciente`
- **Chave Estrangeira**: `id_usuario` (1:1 com usuário)
- **Atributos**:
  - `cpf` (UNIQUE) - Identificação
  - `data_nascimento`, `sexo`
  - `numero_registro` (Identificação na clínica)
  - `data_primeiro_atendimento`
  - `observacoes`
- **Justificativa 3FN**: Separado de usuário para evitar redundância. Paciente é uma especialização de usuário.

#### `contato_paciente`
- **Descrição**: Múltiplos contatos por paciente
- **Chave Primária**: `id_contato`
- **Chave Estrangeira**: `id_paciente`
- **Normalização Justificada**: 
  - Se contatos fossem na tabela paciente (multivalorado) → violaria 1FN
  - Separação permite múltiplos contatos por paciente

#### `endereco_paciente`
- **Descrição**: Múltiplos endereços por paciente
- **Normalização Justificada**: 
  - Se endereços fossem na tabela paciente → violaria 1FN
  - Separação permite histórico de endereços

#### `profissional_saude` ⭐
- **Descrição**: Dados de profissionais de saúde
- **Chave Primária**: `id_profissional`
- **Chave Estrangeira**: `id_usuario` (1:1), `id_especialidade` (FK)
- **Atributos**:
  - `cpf` (UNIQUE)
  - `numero_registro` (CRM, COREN, etc.)
  - `tipo_profissional`
  - `id_especialidade`
- **Justificativa 3FN**: Dados profissionais separados de usuário

#### `horario_profissional`
- **Descrição**: Disponibilidade de agendamento
- **Normalização**: Permite flexibilidade (dia da semana OU data específica)
- **Check Constraint**: Garante que um dos dois é preenchido

#### `atendente` ⭐
- **Descrição**: Dados de funcionários atendentes
- **Chave Primária**: `id_atendente`
- **Chave Estrangeira**: `id_usuario` (1:1)

---

### C. TABELAS DE AGENDAMENTO

#### `agendamento` ⭐
- **Descrição**: Agendamentos de consultas
- **Chave Primária**: `id_agendamento`
- **Chaves Estrangeiras**: 
  - `id_paciente` (FK → paciente)
  - `id_profissional` (FK → profissional_saude)
  - `id_atendente` (FK → atendente)
  - `id_status` (FK → status_agendamento)
- **Atributos**:
  - `data_agendamento` (quando foi feito)
  - `data_consulta`, `hora_consulta` (quando será)
  - `motivo_consulta`
- **Restrição UNIQUE**: `(id_profissional, data_consulta, hora_consulta)`
  - Evita double-booking
- **Justificativa 3FN**: Cada atributo é independente, sem dependências transitivas

#### `historico_agendamento`
- **Descrição**: Rastreabilidade de mudanças
- **Normalização Justificada**: 
  - Se histórico estivesse em agendamento → violaria dependências funcionais
  - Separação permite múltiplas mudanças por agendamento

---

### D. TABELAS DE CONSULTA E PRONTUÁRIO

#### `consulta` ⭐
- **Descrição**: Registro de consultas realizadas
- **Chave Primária**: `id_consulta`
- **Chaves Estrangeiras**: 
  - `id_agendamento` (FK, UNIQUE)
  - `id_paciente` (FK)
  - `id_profissional` (FK)
- **Atributos de Exame Físico**:
  - `peso`, `altura`, `pressao_arterial`
  - `frequencia_cardiaca`, `temperatura`
- **Justificativa 3FN**: Dados clínicos normalizados, sem redundância

#### `diagnostico`
- **Descrição**: Diagnósticos identificados na consulta
- **Chave Primária**: `id_diagnostico`
- **Chaves Estrangeiras**: 
  - `id_consulta` (FK)
  - `id_paciente` (FK)
- **Atributos**:
  - `descricao_diagnostico`
  - `cid_10` (Código Internacional de Doenças)
  - `principal` (Flag para diagnóstico principal)
- **Normalização**: Separado de consulta para permitir múltiplos diagnósticos

#### `tratamento` ⭐
- **Descrição**: Planos de tratamento
- **Chave Primária**: `id_tratamento`
- **Chaves Estrangeiras**:
  - `id_paciente` (FK)
  - `id_diagnostico` (FK, opcional)
  - `id_consulta` (FK, opcional)
  - `id_status_trat` (FK)
- **Atributos**:
  - `data_inicio`, `data_prevista_fim`
  - `descricao_tratamento`
- **Justificativa 3FN**: Tratamento pode estar vinculado a diagnóstico ou consulta

---

### E. TABELAS DE MEDICAMENTOS E PRESCRIÇÕES

#### `medicamento`
- **Descrição**: Cadastro de medicamentos disponíveis
- **Chave Primária**: `id_medicamento`
- **Atributos**:
  - `nome_comercial`, `nome_generico`
  - `principio_ativo`, `dosagem`, `apresentacao`
  - `fabricante`
- **Normalização**: Medicamento é independente de prescrição

#### `prescricao` ⭐
- **Descrição**: Prescrições médicas
- **Chave Primária**: `id_prescricao`
- **Chaves Estrangeiras**:
  - `id_consulta` (FK)
  - `id_paciente` (FK)
  - `id_profissional` (FK)
- **Atributos**:
  - `numero_prescricao` (UNIQUE)
  - `data_prescricao`, `data_validade`
- **Justificativa 3FN**: Prescricao é documento que agrupa itens

#### `item_prescricao`
- **Descrição**: Itens individuais da prescrição
- **Chave Primária**: `id_item_prescricao`
- **Chaves Estrangeiras**:
  - `id_prescricao` (FK)
  - `id_medicamento` (FK)
- **Atributos**:
  - `dosagem`, `frequencia`
  - `duracao_dias`
  - `instrucoes_especiais`
- **Normalização Justificada**: 
  - Uma prescrição pode ter múltiplos medicamentos
  - Separação permite flexibilidade

#### `receita` ⭐
- **Descrição**: Receita de medicação formal
- **Chave Primária**: `id_receita`
- **Chaves Estrangeiras**:
  - `id_prescricao` (FK, UNIQUE)
  - `id_paciente` (FK)
- **Atributos**:
  - `numero_receita` (UNIQUE)
  - `data_emissao`, `data_validade`
  - `assinatura_digital`, `impressa`
- **Justificativa 3FN**: Receita é derivação de prescrição

---

### F. TABELAS DE EXAMES

#### `solicitacao_exame`
- **Descrição**: Solicitações de exames
- **Chave Primária**: `id_solicitacao`
- **Chaves Estrangeiras**:
  - `id_consulta` (FK)
  - `id_paciente` (FK)
  - `id_profissional` (FK)
  - `id_tipo_exame` (FK)
- **Atributos**:
  - `data_solicitacao`
  - `urgencia` (Rotina, Urgente, Emergencial)
  - `status_exame` (Pendente, Realizado, Cancelado)

#### `resultado_exame`
- **Descrição**: Resultados de exames
- **Chave Primária**: `id_resultado`
- **Chave Estrangeira**: `id_solicitacao` (FK, UNIQUE)
- **Atributos**:
  - `data_realizacao`
  - `resultado_texto`, `resultado_numerico`
  - `unidade_medida`, `valor_referencia`
  - `arquivo_resultado` (BYTEA para imagens/PDFs)
- **Normalização Justificada**: 
  - 1:1 com solicitação
  - UNIQUE garante um resultado por solicitação

---

### G. TABELAS DE AUDITORIA

#### `auditoria_prontuario`
- **Descrição**: Rastreabilidade de acessos ao prontuário
- **Chave Primária**: `id_auditoria`
- **Chaves Estrangeiras**:
  - `id_usuario` (FK)
  - `id_paciente` (FK)
- **Atributos**:
  - `tipo_acesso` (Visualização, Edição, Impressão)
  - `data_acesso`, `descricao`
- **Justificativa**: Segurança e conformidade LGPD

---

## 3. ANÁLISE DE NORMALIZAÇÃO

### Por que até 3ª Forma Normal (3FN)?

#### 1ª Forma Normal (1FN)
✅ **Atendida**
- Todos os atributos contêm apenas valores atômicos (indivisíveis)
- Não há atributos multivalorados na mesma tabela
- Exemplo: Contatos e Endereços estão em tabelas separadas

#### 2ª Forma Normal (2FN)
✅ **Atendida**
- Toda coluna não-chave depende funcionalmente da chave primária inteira
- Exemplo em `agendamento`:
  - `motivo_consulta` depende de `id_agendamento` (não de parte dela)
  - `data_consulta` depende de `id_agendamento` inteiro

#### 3ª Forma Normal (3FN)
✅ **Atendida**
- Nenhuma coluna não-chave depende transitivamente de outra coluna não-chave
- Exemplo que foi evitado:
  ```
  ❌ ERRADO: paciente.nome_completo (depende de usuario.id_usuario, não de paciente)
  ✅ CERTO: Separar usuário e paciente como 1:1
  ```

### Exemplos de Decisões de Normalização

#### Caso: Múltiplos Contatos por Paciente
```
❌ ERRADO (Violaria 1FN):
paciente (id_paciente, nome, telefone1, telefone2, email1, email2)

✅ CERTO (Normalizado):
contato_paciente (id_contato, id_paciente, tipo_contato, valor_contato)
```

#### Caso: Medicamentos em Prescrição
```
❌ ERRADO (Violaria 1FN):
prescricao (id, paciente, medicamento1, medicamento2, medicamento3)

✅ CERTO (Normalizado):
item_prescricao (id_item, id_prescricao, id_medicamento, dosagem, frequencia)
```

#### Caso: Exame com Resultado
```
❌ ERRADO (Redundância em 3FN):
exame (id, paciente, tipo, data_solicitacao, resultado, data_resultado)

✅ CERTO (Normalizado):
solicitacao_exame (id_sol, id_paciente, id_tipo)
resultado_exame (id_res, id_solicitacao, resultado, data_resultado)
Relacionamento: 1:1 com UNIQUE
```

---

## 4. ÍNDICES PARA PERFORMANCE

### Índices Criados
- **Busca por Paciente**: `idx_paciente_cpf`, `idx_paciente_numero_registro`
- **Busca por Usuário**: `idx_usuario_email`
- **Agendamentos**: `idx_agendamento_*` (paciente, profissional, data)
- **Consultas**: `idx_consulta_*` (paciente, profissional, data)
- **Prescrições**: `idx_prescricao_*` (paciente, data)
- **Diagnósticos**: `idx_diagnostico_paciente`
- **Exames**: `idx_solicitacao_exame_paciente`

### Estratégia
Índices em colunas frequentemente consultadas:
- Chaves estrangeiras (JOINs)
- Colunas de filtro (WHERE)
- Ordenação (ORDER BY)

---

## 5. CONSTRAINTS E INTEGRIDADE

### Constraints Implementados

1. **PRIMARY KEY**: Unicidade e identidade
2. **FOREIGN KEY**: Integridade referencial
   - ON DELETE RESTRICT: Impede exclusão se houver referências
   - ON DELETE CASCADE: Deleta registros filhos (contatos, endereços)
   - ON DELETE SET NULL: Anula referência (atendente pode ser NULL)

3. **UNIQUE**: Unicidade de valores
   - `cpf`, `email`, `numero_registro`
   - `numero_prescricao`, `numero_receita`
   - Composite: `(id_profissional, data_consulta, hora_consulta)`

4. **CHECK**: Validação de domínio
   - `horario_profissional`: Garante que ou dia_semana OU data_especifica é preenchido

5. **NOT NULL**: Campos obrigatórios

---

## 6. RELACIONAMENTOS

### Diagrama de Relacionamentos

```
usuario (1) -------- (1) paciente
    |
    └---- (1) profissional_saude
    |
    └---- (1) atendente

paciente (1) -------- (*) agendamento
paciente (1) -------- (*) consulta
paciente (1) -------- (*) diagnostico
paciente (1) -------- (*) tratamento
paciente (1) -------- (*) prescricao
paciente (1) -------- (*) solicitacao_exame

profissional_saude (1) -------- (*) especialidade
profissional_saude (1) -------- (*) agendamento
profissional_saude (1) -------- (*) horario_profissional

agendamento (1) -------- (1) consulta
consulta (1) -------- (*) diagnostico
consulta (1) -------- (*) prescricao
consulta (1) -------- (*) solicitacao_exame

prescricao (1) -------- (*) item_prescricao
prescricao (1) -------- (1) receita

item_prescricao (*) -------- (1) medicamento

solicitacao_exame (1) -------- (1) resultado_exame
```

### Cardinalidade

- **1:1**: usuario↔paciente, usuario↔profissional, agendamento↔consulta
- **1:N**: paciente→agendamento, profissional→consulta, consulta→diagnostico
- **Composite**: agendamento (paciente + profissional + data/hora)

---

## 7. VIEWS CRIADAS

### 1. `v_agendamentos_hoje`
Agendamentos de hoje com dados completos do paciente e profissional

### 2. `v_prontuario_paciente`
Visão completa do prontuário: consultas, diagnósticos, tratamentos, prescrições

### 3. `v_consultas_por_mes`
Estatísticas de consultas mensais: total de consultas, pacientes, profissionais

### 4. `v_produtividade_profissional`
Produtividade de cada profissional por mês

### 5. `v_exames_pendentes`
Exames aguardando realização, ordenados por urgência

### 6. `v_prescricoes_ativas`
Prescrições válidas com status

---

## 8. DADOS INICIAIS INSERIDOS

### Tabelas com Dados Base:
1. **perfil_usuario**: Admin, Médico, Enfermeiro, Atendente, Paciente
2. **status_agendamento**: 5 status diferentes
3. **status_tratamento**: 4 status diferentes
4. **especialidade**: 10 especialidades médicas
5. **tipo_exame**: 10 tipos de exame
6. **medicamento**: 8 medicamentos comuns

---

## 9. SEGURANÇA E CONFORMIDADE

### LGPD (Lei Geral de Proteção de Dados)
- ✅ Auditoria de acesso ao prontuário (`auditoria_prontuario`)
- ✅ Rastreabilidade de mudanças (`historico_agendamento`)
- ✅ Senhas em hash (não em texto plano)
- ✅ Controle de acesso por perfil

### Integridade
- ✅ Constraints em cascade
- ✅ Integridade referencial
- ✅ Backup de histórico

---

## 10. COMO USAR O SCRIPT

### Prerequisitos
- PostgreSQL 10+
- Acesso como superuser ou usuário com permissões de CREATE

### Execução
```sql
psql -U postgres -d seu_banco -f 02_script_sql_completo.sql
```

### Verificação
```sql
-- Listar tabelas criadas
\dt clinica.*

-- Listar views criadas
\dv clinica.*

-- Testar uma view
SELECT * FROM clinica.v_agendamentos_hoje;
```

---

## Conclusão

Este modelo de dados segue rigorosamente os princípios de normalização até 3FN, garantindo:
- ✅ Ausência de redundância
- ✅ Integridade dos dados
- ✅ Flexibilidade para mudanças futuras
- ✅ Performance através de índices estratégicos
- ✅ Segurança e rastreabilidade
- ✅ Conformidade com regulamentações (LGPD)
