// ============================================================
// ARQUIVO: src/com/ucsal/clinica/util/DatabaseConfig.java
// ============================================================

package com.ucsal.clinica.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/clinica";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "sua_senha_aqui";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar driver PostgreSQL");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados");
            throw e;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão");
                e.printStackTrace();
            }
        }
    }
}

// ============================================================
// ARQUIVO: src/com/ucsal/clinica/dao/UsuarioDAO.java
// ============================================================

package com.ucsal.clinica.dao;

import com.ucsal.clinica.model.Usuario;
import com.ucsal.clinica.util.DatabaseConfig;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO clinica.usuario " +
                     "(email, senha_hash, nome_completo, ativo, id_perfil, data_criacao, data_atualizacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, usuario.getEmail());
            pstmt.setString(2, usuario.getSenhaHash());
            pstmt.setString(3, usuario.getNomeCompleto());
            pstmt.setBoolean(4, usuario.isAtivo());
            pstmt.setInt(5, usuario.getIdPerfil());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            
            int linhasAfetadas = pstmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setIdUsuario(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM clinica.usuario WHERE email = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetParaUsuario(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarPorId(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM clinica.usuario WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetParaUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clinica.usuario ORDER BY nome_completo";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapResultSetParaUsuario(rs));
            }
        }
        return usuarios;
    }

    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE clinica.usuario SET email = ?, nome_completo = ?, " +
                     "ativo = ?, id_perfil = ?, data_atualizacao = ? WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getEmail());
            pstmt.setString(2, usuario.getNomeCompleto());
            pstmt.setBoolean(3, usuario.isAtivo());
            pstmt.setInt(4, usuario.getIdPerfil());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(6, usuario.getIdUsuario());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int idUsuario) throws SQLException {
        String sql = "DELETE FROM clinica.usuario WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Usuario mapResultSetParaUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenhaHash(rs.getString("senha_hash"));
        usuario.setNomeCompleto(rs.getString("nome_completo"));
        usuario.setAtivo(rs.getBoolean("ativo"));
        usuario.setIdPerfil(rs.getInt("id_perfil"));
        usuario.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        usuario.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
        return usuario;
    }
}

// ============================================================
// ARQUIVO: src/com/ucsal/clinica/dao/PacienteDAO.java
// ============================================================

package com.ucsal.clinica.dao;

import com.ucsal.clinica.model.Paciente;
import com.ucsal.clinica.util.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public boolean inserir(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO clinica.paciente " +
                     "(id_usuario, cpf, data_nascimento, sexo, numero_registro, ativo, data_criacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, paciente.getIdUsuario());
            pstmt.setString(2, paciente.getCpf());
            pstmt.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            pstmt.setString(4, String.valueOf(paciente.getSexo()));
            pstmt.setString(5, paciente.getNumeroRegistro());
            pstmt.setBoolean(6, paciente.isAtivo());
            pstmt.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
            
            int linhasAfetadas = pstmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        paciente.setIdPaciente(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Paciente buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM clinica.paciente WHERE cpf = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpf);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetParaPaciente(rs);
                }
            }
        }
        return null;
    }

    public Paciente buscarPorId(int idPaciente) throws SQLException {
        String sql = "SELECT * FROM clinica.paciente WHERE id_paciente = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPaciente);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetParaPaciente(rs);
                }
            }
        }
        return null;
    }

    public List<Paciente> listarTodos() throws SQLException {
        String sql = "SELECT p.* FROM clinica.paciente p " +
                     "JOIN clinica.usuario u ON p.id_usuario = u.id_usuario " +
                     "ORDER BY u.nome_completo";
        List<Paciente> pacientes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pacientes.add(mapResultSetParaPaciente(rs));
            }
        }
        return pacientes;
    }

    public boolean atualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE clinica.paciente SET data_nascimento = ?, sexo = ?, " +
                     "numero_registro = ?, observacoes = ?, ativo = ? WHERE id_paciente = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(paciente.getDataNascimento()));
            pstmt.setString(2, String.valueOf(paciente.getSexo()));
            pstmt.setString(3, paciente.getNumeroRegistro());
            pstmt.setString(4, paciente.getObservacoes());
            pstmt.setBoolean(5, paciente.isAtivo());
            pstmt.setInt(6, paciente.getIdPaciente());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    private Paciente mapResultSetParaPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setIdUsuario(rs.getInt("id_usuario"));
        paciente.setCpf(rs.getString("cpf"));
        paciente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        paciente.setSexo(rs.getString("sexo").charAt(0));
        paciente.setNumeroRegistro(rs.getString("numero_registro"));
        paciente.setObservacoes(rs.getString("observacoes"));
        paciente.setAtivo(rs.getBoolean("ativo"));
        paciente.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        return paciente;
    }
}

// ============================================================
// ARQUIVO: src/com/ucsal/clinica/dao/AgendamentoDAO.java
// ============================================================

package com.ucsal.clinica.dao;

import com.ucsal.clinica.model.Agendamento;
import com.ucsal.clinica.util.DatabaseConfig;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    public boolean inserir(Agendamento agendamento) throws SQLException {
        String sql = "INSERT INTO clinica.agendamento " +
                     "(id_paciente, id_profissional, id_atendente, data_agendamento, " +
                     "data_consulta, hora_consulta, motivo_consulta, observacoes, id_status, data_criacao, data_atualizacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, agendamento.getIdPaciente());
            pstmt.setInt(2, agendamento.getIdProfissional());
            pstmt.setInt(3, agendamento.getIdAtendente());
            pstmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataAgendamento()));
            pstmt.setDate(5, Date.valueOf(agendamento.getDataConsulta()));
            pstmt.setTime(6, Time.valueOf(agendamento.getHoraConsulta()));
            pstmt.setString(7, agendamento.getMotivoConsulta());
            pstmt.setString(8, agendamento.getObservacoes());
            pstmt.setInt(9, agendamento.getIdStatus());
            pstmt.setTimestamp(10, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setTimestamp(11, Timestamp.valueOf(java.time.LocalDateTime.now()));
            
            int linhasAfetadas = pstmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        agendamento.setIdAgendamento(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Agendamento> buscarPorData(LocalDate data) throws SQLException {
        String sql = "SELECT * FROM clinica.agendamento WHERE data_consulta = ? ORDER BY hora_consulta";
        List<Agendamento> agendamentos = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(data));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    agendamentos.add(mapResultSetParaAgendamento(rs));
                }
            }
        }
        return agendamentos;
    }

    public List<Agendamento> buscarPorPaciente(int idPaciente) throws SQLException {
        String sql = "SELECT * FROM clinica.agendamento WHERE id_paciente = ? ORDER BY data_consulta DESC";
        List<Agendamento> agendamentos = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPaciente);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    agendamentos.add(mapResultSetParaAgendamento(rs));
                }
            }
        }
        return agendamentos;
    }

    public Agendamento buscarPorId(int idAgendamento) throws SQLException {
        String sql = "SELECT * FROM clinica.agendamento WHERE id_agendamento = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAgendamento);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetParaAgendamento(rs);
                }
            }
        }
        return null;
    }

    public boolean atualizar(Agendamento agendamento) throws SQLException {
        String sql = "UPDATE clinica.agendamento SET id_status = ?, observacoes = ?, data_atualizacao = ? " +
                     "WHERE id_agendamento = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, agendamento.getIdStatus());
            pstmt.setString(2, agendamento.getObservacoes());
            pstmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setInt(4, agendamento.getIdAgendamento());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    private Agendamento mapResultSetParaAgendamento(ResultSet rs) throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setIdAgendamento(rs.getInt("id_agendamento"));
        agendamento.setIdPaciente(rs.getInt("id_paciente"));
        agendamento.setIdProfissional(rs.getInt("id_profissional"));
        agendamento.setIdAtendente(rs.getInt("id_atendente"));
        agendamento.setDataAgendamento(rs.getTimestamp("data_agendamento").toLocalDateTime());
        agendamento.setDataConsulta(rs.getDate("data_consulta").toLocalDate());
        agendamento.setHoraConsulta(rs.getTime("hora_consulta").toLocalTime());
        agendamento.setMotivoConsulta(rs.getString("motivo_consulta"));
        agendamento.setObservacoes(rs.getString("observacoes"));
        agendamento.setIdStatus(rs.getInt("id_status"));
        agendamento.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        agendamento.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
        return agendamento;
    }
}

// ============================================================
// ARQUIVO: src/com/ucsal/clinica/dao/ConsultaDAO.java
// ============================================================

package com.ucsal.clinica.dao;

import com.ucsal.clinica.model.Consulta;
import com.ucsal.clinica.util.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public boolean inserir(Consulta consulta) throws SQLException {
        String sql = "INSERT INTO clinica.consulta " +
                     "(id_agendamento, id_paciente, id_profissional, data_consulta, queixa_principal, " +
                     "anamnese, achados_exame_fisico, hipotese_diagnostica, notas_clinicas, peso, " +
                     "altura, pressao_arterial, frequencia_cardiaca, temperatura, data_criacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, consulta.getIdAgendamento());
            pstmt.setInt(2, consulta.getIdPaciente());
            pstmt.setInt(3, consulta.getIdProfissional());
            pstmt.setTimestamp(4, Timestamp.valueOf(consulta.getDataConsulta()));
            pstmt.setString(5, consulta.getQueixaPrincipal());
            pstmt.setString(6, consulta.getAnamnese());
            pstmt.setString(7, consulta.getAchadosExameFisico());
            pstmt.setString(8, consulta.getHipoteseDiagnostica());
            pstmt.setString(9, consulta.getNotasClinicas());
            pstmt.setBigDecimal(10, consulta.getPeso());
            pstmt.setBigDecimal(11, consulta.getAltura());
            pstmt.setString(12, consulta.getPressaoArterial());
            pstmt.setInt(13, consulta.getFrequenciaCardiaca());
            pstmt.setBigDecimal(14, consulta.getTemperatura());
            pstmt.setTimestamp(15, Timestamp.valueOf(java.time.LocalDateTime.now()));
            
            int linhasAfetadas = pstmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        consulta.setIdConsulta(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Consulta buscarPorId(int idConsulta) throws SQLException {
        String sql = "SELECT * FROM clinica.consulta WHERE id_consulta = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idConsulta);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetParaConsulta(rs);
                }
            }
        }
        return null;
    }

    public List<Consulta> buscarPorPaciente(int idPaciente) throws SQLException {
        String sql = "SELECT * FROM clinica.consulta WHERE id_paciente = ? ORDER BY data_consulta DESC";
        List<Consulta> consultas = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPaciente);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapResultSetParaConsulta(rs));
                }
            }
        }
        return consultas;
    }

    private Consulta mapResultSetParaConsulta(ResultSet rs) throws SQLException {
        Consulta consulta = new Consulta();
        consulta.setIdConsulta(rs.getInt("id_consulta"));
        consulta.setIdAgendamento(rs.getInt("id_agendamento"));
        consulta.setIdPaciente(rs.getInt("id_paciente"));
        consulta.setIdProfissional(rs.getInt("id_profissional"));
        consulta.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
        consulta.setQueixaPrincipal(rs.getString("queixa_principal"));
        consulta.setAnamnese(rs.getString("anamnese"));
        consulta.setAchadosExameFisico(rs.getString("achados_exame_fisico"));
        consulta.setHipoteseDiagnostica(rs.getString("hipotese_diagnostica"));
        consulta.setNotasClinicas(rs.getString("notas_clinicas"));
        consulta.setPeso(rs.getBigDecimal("peso"));
        consulta.setAltura(rs.getBigDecimal("altura"));
        consulta.setPressaoArterial(rs.getString("pressao_arterial"));
        consulta.setFrequenciaCardiaca(rs.getInt("frequencia_cardiaca"));
        consulta.setTemperatura(rs.getBigDecimal("temperatura"));
        consulta.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        return consulta;
    }
}
