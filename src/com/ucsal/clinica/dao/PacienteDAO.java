package com.ucsal.clinica.dao;

import com.ucsal.clinica.model.Paciente;
import com.ucsal.clinica.util.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public boolean inserir(Paciente paciente) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return inserir(paciente, conn);
        }
    }

    public boolean inserir(Paciente paciente, Connection conn) throws SQLException {
        String sql = "INSERT INTO clinica.paciente " +
                     "(id_usuario, cpf, data_nascimento, sexo, numero_registro, ativo, data_criacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, paciente.getIdUsuario());
            pstmt.setString(2, paciente.getCpf());
            pstmt.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            pstmt.setString(4, String.valueOf(paciente.getSexo()));
            pstmt.setString(5, paciente.getNumeroRegistro());
            pstmt.setBoolean(6, paciente.isAtivo());
            pstmt.setTimestamp(7, Timestamp.valueOf(paciente.getDataCriacao() != null
                    ? paciente.getDataCriacao() : java.time.LocalDateTime.now()));
            
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

    public void inserirContatoTelefone(Connection conn, int idPaciente, String telefone) throws SQLException {
        if (telefone == null || telefone.trim().isEmpty()) {
            return;
        }

        String sql = "INSERT INTO clinica.contato_paciente " +
                "(id_paciente, tipo_contato, valor_contato, principal) VALUES (?, ?, ?, TRUE)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPaciente);
            pstmt.setString(2, "Telefone");
            pstmt.setString(3, telefone.replaceAll("[^0-9]", ""));
            pstmt.executeUpdate();
        }
    }

    public String buscarTelefonePrincipal(int idPaciente) throws SQLException {
        String sql = "SELECT valor_contato FROM clinica.contato_paciente " +
                "WHERE id_paciente = ? AND tipo_contato ILIKE 'telefone' " +
                "ORDER BY principal DESC, id_contato DESC LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPaciente);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("valor_contato");
                }
            }
        }

        return "-";
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
