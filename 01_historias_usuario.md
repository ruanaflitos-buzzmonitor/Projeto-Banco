# Histórias de Usuário - Sistema de Gestão de Clínica Médica

## 1. Gestão de Pacientes

### HU-001: Cadastrar Novo Paciente
**Como:** Atendente  
**Quero:** Cadastrar um novo paciente no sistema  
**Para que:** O paciente tenha seu registro criado e possa agendar consultas

**Critérios de Aceitação:**
- O sistema deve validar CPF único
- Campos obrigatórios: Nome completo, CPF, Data de nascimento, Telefone, Email, Endereço
- O sistema deve armazenar histórico de contatos
- Deve ser possível atualizar dados cadastrais

---

### HU-002: Consultar Dados do Paciente
**Como:** Profissional de saúde ou atendente  
**Quero:** Visualizar os dados pessoais e histórico de um paciente  
**Para que:** Tenha acesso às informações necessárias para atendimento

**Critérios de Aceitação:**
- Busca por CPF, nome ou número de registro
- Exibição de dados pessoais, contactos e histórico

---

## 2. Gestão de Profissionais e Colaboradores

### HU-003: Cadastrar Profissional de Saúde
**Como:** Administrador  
**Quero:** Registrar médicos, enfermeiros e outros profissionais de saúde  
**Para que:** Eles possam ser alocados para agendamentos

**Critérios de Aceitação:**
- Campos: Nome, CPF, CRM/COREN, Especialidade, Telefone, Email
- Controle de especialidades médicas
- Status ativo/inativo para profissionais

---

### HU-004: Cadastrar Atendente
**Como:** Administrador  
**Quero:** Registrar funcionários que realizam atendimento  
**Para que:** Possam executar tarefas de agendamento e gestão

**Critérios de Aceitação:**
- Campos: Nome, CPF, Telefone, Email, Data admissão
- Status ativo/inativo

---

## 3. Gestão de Agendamentos

### HU-005: Agendar Consulta
**Como:** Atendente  
**Quero:** Agendar uma consulta entre um paciente e um profissional de saúde  
**Para que:** O paciente tenha acesso a atendimento médico

**Critérios de Aceitação:**
- Validação de disponibilidade do profissional
- Campos: Paciente, Profissional, Data, Hora, Motivo da consulta
- Sistema deve evitar agendamentos duplicados no mesmo horário
- Envio de confirmação com data, hora e profissional

---

### HU-006: Consultar Agendamentos
**Como:** Atendente ou Profissional  
**Quero:** Visualizar os agendamentos do dia/semana/mês  
**Para que:** Tenha controle das atividades programadas

**Critérios de Aceitação:**
- Filtro por data, profissional ou paciente
- Exibição em formato de calendário ou lista
- Destaque para agendamentos próximos

---

### HU-007: Remarcar ou Cancelar Agendamento
**Como:** Atendente  
**Quero:** Remarcar ou cancelar um agendamento  
**Para que:** Ajuste a agenda conforme necessário

**Critérios de Aceitação:**
- Permite mudança de data/hora com disponibilidade
- Registra motivo do cancelamento
- Notificação ao paciente

---

## 4. Gestão de Consultas e Prontuário

### HU-008: Registrar Consulta Realizada
**Como:** Médico  
**Quero:** Registrar os detalhes da consulta realizada  
**Para que:** Tenha documentação completa do atendimento

**Critérios de Aceitação:**
- Campos: Data/Hora consulta, Queixa principal, Achados do exame físico, Hipótese diagnóstica
- Histórico de medicações atuais do paciente
- Possibilidade de descrever achados clínicos
- Data/Hora do registro automática

---

### HU-009: Visualizar Prontuário do Paciente
**Como:** Profissional de saúde  
**Quero:** Acessar o prontuário completo do paciente  
**Para que:** Tenha histórico de atendimentos, diagnósticos e tratamentos

**Critérios de Aceitação:**
- Exibição em ordem cronológica inversa (mais recente primeiro)
- Inclui: Consultas, Diagnósticos, Tratamentos, Exames, Prescrições
- Acesso restrito a profissionais autorizados
- Auditoria de acessos

---

## 5. Diagnóstico e Tratamento

### HU-010: Registrar Diagnóstico
**Como:** Médico  
**Quero:** Registrar o diagnóstico de uma consulta  
**Para que:** Tenha documentação formal do diagnóstico

**Critérios de Aceitação:**
- Associação a uma consulta específica
- Descrição do diagnóstico
- CID (Classificação Internacional de Doenças) se aplicável
- Data do registro

---

### HU-011: Registrar Tratamento
**Como:** Médico  
**Quero:** Descrever o plano de tratamento recomendado  
**Para que:** O paciente tenha orientações claras sobre cuidados

**Critérios de Aceitação:**
- Associação a diagnóstico ou consulta
- Descrição detalhada do tratamento
- Data de início e previsão de término
- Status do tratamento (ativo, concluído, suspenso)

---

## 6. Prescrição Médica

### HU-012: Emitir Prescrição Médica
**Como:** Médico  
**Quero:** Gerar uma prescrição de medicamentos para o paciente  
**Para que:** O paciente tenha documento formal para adquirir medicamentos

**Critérios de Aceitação:**
- Associação a uma consulta
- Campos: Medicamento, Dosagem, Frequência, Duração, Instruções especiais
- Data de emissão e validade
- Assinatura digital ou identificação do médico
- Possibilidade de gerar PDF para impressão

---

### HU-013: Visualizar Prescrições Anteriores
**Como:** Paciente ou Profissional  
**Quero:** Acessar o histórico de prescrições do paciente  
**Para que:** Tenha referência de medicamentos já prescritos

**Critérios de Aceitação:**
- Filtro por data ou medicamento
- Exibição das prescrições em ordem cronológica
- Indicação de prescrições ativas

---

## 7. Resultados de Exames

### HU-014: Registrar Exame Solicitado
**Como:** Médico  
**Quero:** Solicitar um exame para o paciente  
**Para que:** Tenha documentação da solicitação

**Critérios de Aceitação:**
- Associação a uma consulta
- Tipo de exame (laboratorial, imagem, etc.)
- Data da solicitação
- Status: Pendente, Realizado, Cancelado

---

### HU-015: Registrar Resultado de Exame
**Como:** Profissional de laboratório ou administrador  
**Quero:** Lançar o resultado de um exame no sistema  
**Para que:** O médico tenha acesso aos resultados para análise

**Critérios de Aceitação:**
- Associação ao exame solicitado
- Campos para resultado (texto, valores numéricos, imagens)
- Data de realização
- Data de entrada no sistema

---

### HU-016: Visualizar Resultados de Exames
**Como:** Médico ou Paciente  
**Quero:** Consultar os resultados de exames já realizados  
**Para que:** Tenha histórico de resultados para diagnóstico

**Critérios de Aceitação:**
- Filtro por tipo de exame ou data
- Exibição em formato legível
- Possibilidade de download/impressão

---

## 8. Relatórios e Consultas

### HU-017: Gerar Relatório de Consultas do Mês
**Como:** Gerente ou Administrador  
**Quero:** Visualizar todas as consultas realizadas em um mês  
**Para que:** Tenha controle sobre atividades da clínica

**Critérios de Aceitação:**
- Filtro por mês e ano
- Exibição com: Data, Paciente, Profissional, Motivo
- Contagem total de consultas
- Possibilidade de exportar em PDF/Excel

---

### HU-018: Gerar Relatório de Profissional por Período
**Como:** Administrador  
**Quero:** Visualizar as consultas realizadas por cada profissional  
**Para que:** Tenha controle de produtividade

**Critérios de Aceitação:**
- Filtro por profissional, período
- Total de consultas por profissional
- Especialidade do profissional

---

## 9. Segurança e Acesso

### HU-019: Autenticar Usuário
**Como:** Qualquer usuário  
**Quero:** Fazer login no sistema  
**Para que:** Acesse apenas as funcionalidades permitidas ao meu perfil

**Critérios de Aceitação:**
- Login com email e senha
- Diferentes perfis: Administrador, Médico, Enfermeiro, Atendente, Paciente
- Mensagem de erro para credenciais inválidas

---

### HU-020: Controlar Acesso por Perfil
**Como:** Sistema  
**Quero:** Restringir funcionalidades conforme perfil do usuário  
**Para que:** Garanta segurança dos dados

**Critérios de Aceitação:**
- Médicos: Registro de consultas, diagnósticos, prescrições
- Atendentes: Agendamento, cadastro de pacientes
- Pacientes: Visualizar prontuário, agendamentos, prescrições
- Administrador: Acesso total

---

## 10. Receita de Medicação

### HU-021: Gerar Receita de Medicação
**Como:** Médico  
**Quero:** Gerar uma receita formal a partir da prescrição  
**Para que:** O paciente tenha documento oficial para obtenção de medicamentos

**Critérios de Aceitação:**
- Baseada em prescrição existente
- Inclui: Cabeçalho da clínica, dados do paciente, medicamentos, assinatura
- Numeração sequencial
- Possibilidade de impressão
- Validade de 30 dias

---

## Resumo de Funcionalidades Principais

| Funcionalidade | Ator | Prioridade |
|---|---|---|
| Cadastro de Pacientes | Atendente | Alta |
| Cadastro de Profissionais | Administrador | Alta |
| Agendamento de Consultas | Atendente | Alta |
| Registro de Consulta | Médico | Alta |
| Prontuário do Paciente | Profissional | Alta |
| Diagnóstico | Médico | Alta |
| Prescrição Médica | Médico | Alta |
| Resultado de Exames | Sistema | Média |
| Relatórios | Administrador | Média |
| Receita de Medicação | Médico | Média |

