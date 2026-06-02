# Sistema de Gestão de Clínica Médica

## Descrição do Projeto

Sistema completo de gestão para clínicas médicas desenvolvido em Java com interface web HTML/CSS/JavaScript, utilizando JDBC para integração com PostgreSQL. O sistema permite gerenciar agendamentos, consultas, pacientes, profissionais, prescrições e exames.

## Estrutura do Projeto

```
ClinicaMedica/
├── docs/
│   ├── 01_historias_usuario.md
│   ├── 02_script_sql_completo.sql
│   ├── 03_documentacao_modelo.md
│   └── diagrama_modelo.png
├── src/
│   └── com/ucsal/clinica/
│       ├── model/
│       │   ├── Usuario.java
│       │   ├── Paciente.java
│       │   ├── ProfissionalSaude.java
│       │   ├── Agendamento.java
│       │   ├── Consulta.java
│       │   ├── Prescricao.java
│       │   ├── Diagnostico.java
│       │   └── SolicitacaoExame.java
│       ├── dao/
│       │   ├── UsuarioDAO.java
│       │   ├── PacienteDAO.java
│       │   ├── AgendamentoDAO.java
│       │   └── ConsultaDAO.java
│       ├── service/
│       │   ├── UsuarioService.java
│       │   ├── PacienteService.java
│       │   └── AgendamentoService.java
│       ├── util/
│       │   └── DatabaseConfig.java
│       ├── servlet/
│       │   ├── UsuarioServlet.java
│       │   ├── PacienteServlet.java
│       │   ├── AgendamentoServlet.java
│       │   └── ConsultaServlet.java
│       └── Main.java
├── web/
│   ├── WEB-INF/
│   │   └── web.xml
│   ├── html/
│   │   ├── index.html
│   │   ├── login.html
│   │   └── dashboard.html
│   ├── css/
│   │   └── style.css
│   └── js/
│       └── script.js
├── lib/
│   └── postgresql-42.6.0.jar
├── build.xml
└── README.md
```

## Pré-requisitos

### Software Necessário
- **Java Development Kit (JDK) 8 ou superior**
- **PostgreSQL 10 ou superior**
- **Editor de Código ou IDE** (Eclipse, IntelliJ IDEA, NetBeans ou VS Code)
- **Apache Tomcat 8.5+** (para deploy da aplicação web)

### Dependências Java
- PostgreSQL JDBC Driver 42.6.0

## Instalação e Configuração

### 1. Configurar Banco de Dados PostgreSQL

#### 1.1 Criar banco de dados
```bash
# Conectar ao PostgreSQL
psql -U postgres

# Criar banco de dados
CREATE DATABASE clinica;

# Sair
\q
```

#### 1.2 Executar script SQL
```bash
# Executar o script de criação do banco
psql -U postgres -d clinica -f 02_script_sql_completo.sql
```

**Importante**: Verifique se o script foi executado sem erros:
```bash
# Verificar tabelas criadas
psql -U postgres -d clinica -c "\dt clinica.*"
```

### 2. Configurar Projeto Java

#### 2.1 Estrutura de Diretórios
```bash
mkdir -p ClinicaMedica/src/com/ucsal/clinica/{model,dao,service,util,servlet}
mkdir -p ClinicaMedica/web/{WEB-INF,html,css,js}
mkdir -p ClinicaMedica/lib
mkdir -p ClinicaMedica/docs
```

#### 2.2 Baixar Driver JDBC
```bash
# Colocar o arquivo postgresql-42.6.0.jar em ClinicaMedica/lib/
wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar -O lib/postgresql-42.6.0.jar
```

#### 2.3 Configurar Banco de Dados
Editar `src/com/ucsal/clinica/util/DatabaseConfig.java`:
```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/clinica";
private static final String DB_USER = "seu_usuario";
private static final String DB_PASSWORD = "sua_senha";
```

### 3. Compilar Projeto

```bash
# Navegar até o diretório do projeto
cd ClinicaMedica

# Compilar código Java
javac -d bin -cp lib/postgresql-42.6.0.jar src/com/ucsal/clinica/**/*.java

# Se usar Windows
javac -d bin -cp lib/postgresql-42.6.0.jar src/com/ucsal/clinica/model/*.java
javac -d bin -cp lib/postgresql-42.6.0.jar src/com/ucsal/clinica/dao/*.java
javac -d bin -cp lib/postgresql-42.6.0.jar src/com/ucsal/clinica/util/*.java
javac -d bin -cp lib/postgresql-42.6.0.jar;. src/com/ucsal/clinica/service/*.java
javac -d bin -cp lib/postgresql-42.6.0.jar;. src/com/ucsal/clinica/servlet/*.java
```

## Execução

### Opção 1: Aplicação Desktop/Console
```bash
# Compilar e executar classe Main
java -cp bin:lib/postgresql-42.6.0.jar com.ucsal.clinica.Main
```

### Opção 2: Aplicação Web (Com Tomcat)

#### 2.1 Criar estrutura de aplicação web
```bash
mkdir -p ClinicaMedica/dist/ClinicaMedica/WEB-INF/lib
mkdir -p ClinicaMedica/dist/ClinicaMedica/WEB-INF/classes
```

#### 2.2 Copiar arquivos
```bash
# Copiar classes compiladas
cp -r bin/* dist/ClinicaMedica/WEB-INF/classes/

# Copiar biblioteca JDBC
cp lib/postgresql-42.6.0.jar dist/ClinicaMedica/WEB-INF/lib/

# Copiar arquivos web
cp web/html/* dist/ClinicaMedica/
cp web/css/* dist/ClinicaMedica/css/
cp web/js/* dist/ClinicaMedica/js/
cp web/WEB-INF/web.xml dist/ClinicaMedica/WEB-INF/
```

#### 2.3 Fazer deploy no Tomcat
```bash
# Copiar para diretório webapps do Tomcat
cp -r dist/ClinicaMedica $TOMCAT_HOME/webapps/

# Acessar aplicação
# http://localhost:8080/ClinicaMedica/
```

## Funcionalidades Implementadas

### Autenticação
- ✅ Login de usuários
- ✅ Controle de acesso por perfil
- ✅ Gerenciamento de senhas

### Gestão de Pacientes
- ✅ Cadastro de pacientes
- ✅ Atualização de dados
- ✅ Visualização de prontuário
- ✅ Histórico de contatos e endereços

### Gestão de Profissionais
- ✅ Cadastro de médicos e especialistas
- ✅ Gerenciamento de especialidades
- ✅ Controle de disponibilidade

### Agendamentos
- ✅ Criar agendamento
- ✅ Remarcar/Cancelar
- ✅ Visualizar agenda por data
- ✅ Validação de conflitos de horário

### Consultas
- ✅ Registrar consulta realizada
- ✅ Documentar achados clínicos
- ✅ Registrar sinais vitais

### Diagnósticos
- ✅ Registrar diagnóstico
- ✅ Adicionar códigos CID-10
- ✅ Histórico de diagnósticos

### Prescrições
- ✅ Gerar prescrição médica
- ✅ Selecionar medicamentos
- ✅ Definir dosagem e frequência
- ✅ Gerar receita

### Exames
- ✅ Solicitar exame
- ✅ Registrar resultado
- ✅ Consultar pendências

### Relatórios
- ✅ Agendamentos por data
- ✅ Produtividade de profissionais
- ✅ Consultas por mês
- ✅ Exames pendentes

## API REST (Futura Implementação)

Endpoints planejados:
- `GET /api/pacientes` - Listar pacientes
- `POST /api/pacientes` - Criar paciente
- `GET /api/agendamentos` - Listar agendamentos
- `POST /api/agendamentos` - Criar agendamento
- `GET /api/consultas/:id` - Obter consulta
- `POST /api/prescricoes` - Criar prescrição

## Validação de Dados

### Paciente
- CPF: formato XXX.XXX.XXX-XX e unicidade
- Email: formato válido
- Data de Nascimento: data válida
- Telefone: formato (XX) XXXXX-XXXX

### Agendamento
- Data: não pode ser no passado
- Hora: validação de horário disponível
- Profissional: deve estar ativo
- Paciente: deve estar ativo

### Consulta
- Agendamento: deve estar com status "Agendado"
- Queixa Principal: obrigatória

## Segurança

### Implementado
- ✅ Senhas em hash (SHA-256 ou bcrypt)
- ✅ Validação de entrada (JDBC PreparedStatements)
- ✅ Controle de acesso por perfil
- ✅ Auditoria de acesso ao prontuário

### A Implementar
- [ ] HTTPS/SSL
- [ ] Tokens JWT para API
- [ ] Rate limiting
- [ ] Proteção CSRF
- [ ] Criptografia de dados sensíveis

## Testes

### Teste de Conexão
```bash
# Criar arquivo TestConnection.java
java -cp bin:lib/postgresql-42.6.0.jar com.ucsal.clinica.util.DatabaseConfig
```

### Teste de DAO
```bash
# Criar testes unitários em src/test/java/
# Usar JUnit 4 ou TestNG
```

## Troubleshooting

### Erro: "Driver não encontrado"
- Verificar se postgresql-42.6.0.jar está em lib/
- Verificar se classpath está correto na compilação

### Erro: "Conexão recusada"
- Verificar se PostgreSQL está rodando: `psql -U postgres`
- Verificar configurações em DatabaseConfig.java
- Verificar credenciais do banco

### Erro: "Tabela não existe"
- Verificar se script SQL foi executado: `psql -U postgres -d clinica -c "\dt"`
- Verificar se está usando schema `clinica`

### Erro: "CPF duplicado"
- Verificar constraint UNIQUE na tabela paciente
- Limpar dados de teste: `DELETE FROM clinica.paciente;`

## Performance e Otimizações

### Índices
- Criados em colunas frequentemente consultadas
- Melhoram buscas por CPF, email, data

### Connection Pool
Implementar pool de conexões (C3P0 ou HikariCP):
```java
ComboPooledDataSource cpds = new ComboPooledDataSource();
cpds.setDriverClass("org.postgresql.Driver");
cpds.setJdbcUrl("jdbc:postgresql://localhost:5432/clinica");
cpds.setUser("usuario");
cpds.setPassword("senha");
cpds.setMinPoolSize(5);
cpds.setMaxPoolSize(20);
```

## Dúvidas Frequentes (FAQ)

**P: Como fazer backup do banco?**
```bash
pg_dump -U postgres clinica > backup_clinica.sql
```

**P: Como restaurar backup?**
```bash
psql -U postgres -d clinica -f backup_clinica.sql
```

**P: Posso usar outro banco de dados?**
Sim, altere o driver JDBC em DatabaseConfig.java

**P: Como adicionar novo usuário?**
```sql
INSERT INTO clinica.usuario (email, senha_hash, nome_completo, ativo, id_perfil)
VALUES ('novo@email.com', 'hash_da_senha', 'Nome Completo', TRUE, 3);
```

## Licença

Este projeto é desenvolvido para fins educacionais na UCSAL.

## Contato

Para dúvidas sobre o projeto, entre em contato com o professor responsável.

## Changelog

### v1.0 (2024-01-15)
- ✅ Modelo de dados normalizado
- ✅ Estrutura do projeto Java
- ✅ Interface web básica
- ✅ Classes de modelo e DAO
- ✅ Integração JDBC com PostgreSQL

### v1.1 (Planejado)
- [ ] API REST completa
- [ ] Autenticação com JWT
- [ ] Dashboard com gráficos
- [ ] Geração de relatórios em PDF
- [ ] Integração com email
- [ ] Aplicativo mobile

## Próximos Passos

1. **Testes Unitários**: Criar testes para DAOs e Services
2. **Servlets Completos**: Implementar todos os servlets para CRUD
3. **Interface Web Responsiva**: Melhorar layout para mobile
4. **Autenticação**: Sistema de login funcional
5. **Relatórios**: Geração de PDF com iText
6. **Email**: Notificações por email para agendamentos
7. **Deploy**: Colocar em produção em servidor
8. **Monitoramento**: Logging com SLF4J/Log4j

---

**Última atualização**: 2024-01-15
**Versão**: 1.0
**Status**: Em desenvolvimento
