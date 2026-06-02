# Estrutura do Projeto Java - Sistema de Clínica Médica

## Organização de Diretórios

```
ClinicaMedica/
├── src/
│   └── com/ucsal/clinica/
│       ├── model/              # Classes de modelo (entidades)
│       ├── dao/                # Data Access Objects (JDBC)
│       ├── service/            # Lógica de negócio
│       ├── util/               # Classes utilitárias
│       ├── servlet/            # Servlets (se usando Servlet API)
│       └── Main.java           # Classe principal
├── web/
│   ├── WEB-INF/
│   │   └── web.xml            # Configuração da aplicação web
│   └── html/
│       ├── index.html
│       ├── login.html
│       ├── dashboard.html
│       ├── agendamentos.html
│       ├── pacientes.html
│       ├── profissionais.html
│       ├── consultas.html
│       ├── prescricoes.html
│       ├── exames.html
│       └── css/
│           └── style.css
├── lib/
│   └── postgresql-42.6.0.jar   # Driver JDBC do PostgreSQL
├── docs/
│   ├── 01_historias_usuario.md
│   ├── 02_script_sql_completo.sql
│   ├── 03_documentacao_modelo.md
│   └── diagrama_modelo.png
├── build.xml                   # Arquivo Ant (opcional)
└── README.md
```

## Dependências Necessárias

### JDBC Driver
- PostgreSQL JDBC Driver 42.6.0 ou superior
- Download: https://jdbc.postgresql.org/

### Bibliotecas Recomendadas (Opcional)
- Apache Commons DbUtils (para simplificar JDBC)
- Apache Commons CLI (para argumentos de linha de comando)

## Configuração do Banco de Dados

### Arquivo: `src/com/ucsal/clinica/util/DatabaseConfig.java`

```java
public class DatabaseConfig {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/clinica";
    public static final String DB_USER = "seu_usuario";
    public static final String DB_PASSWORD = "sua_senha";
    public static final String DB_DRIVER = "org.postgresql.Driver";
    
    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
```

## Compilação e Execução

### Compilar
```bash
javac -d bin -cp lib/postgresql-42.6.0.jar src/com/ucsal/clinica/**/*.java
```

### Executar
```bash
java -cp bin:lib/postgresql-42.6.0.jar com.ucsal.clinica.Main
```

## Estrutura de Arquivos Java Principais

Ver próximos arquivos para:
- Classes de Model
- Classes de DAO
- Classes de Service
- Classes Servlet
- Páginas HTML/CSS/JS

