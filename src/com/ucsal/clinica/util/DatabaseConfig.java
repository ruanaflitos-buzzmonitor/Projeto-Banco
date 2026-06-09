package com.ucsal.clinica.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/clinica";
    private static final String DEFAULT_DB_USER = "postgres";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    private static final String DB_URL = config("CLINICA_DB_URL", "clinica.db.url", DEFAULT_DB_URL);
    private static final String DB_USER = config("CLINICA_DB_USER", "clinica.db.user", DEFAULT_DB_USER);
    private static final String DB_PASSWORD = requiredConfig("CLINICA_DB_PASSWORD", "clinica.db.password");

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver PostgreSQL nao encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexao: " + e.getMessage());
            }
        }
    }

    private static String config(String envName, String propertyName, String defaultValue) {
        String property = System.getProperty(propertyName);
        if (property != null && !property.trim().isEmpty()) {
            return property;
        }

        String env = System.getenv(envName);
        if (env != null && !env.trim().isEmpty()) {
            return env;
        }

        return defaultValue;
    }

    private static String requiredConfig(String envName, String propertyName) {
        String value = config(envName, propertyName, null);
        if (value == null || value.trim().isEmpty()) {
            throw new ExceptionInInitializerError(
                    "Configure a senha do banco em " + envName + " ou -D" + propertyName);
        }

        return value;
    }
}
