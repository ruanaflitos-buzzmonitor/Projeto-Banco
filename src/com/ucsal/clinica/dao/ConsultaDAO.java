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

        System.out.println("🔥 Entrou no método inserir Consulta");

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("🧠 Preparando INSERT no banco...");

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

            int rows = pstmt.executeUpdate();

            System.out.println("✅ Linhas afetadas: " + rows);

            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        consulta.setIdConsulta(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("❌ ERRO AO INSERIR CONSULTA");
            e.printStackTrace();
            throw e;
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

    public List<Consulta> listarTodos() throws SQLException {

        String sql = "SELECT * FROM clinica.consulta ORDER BY data_consulta DESC";

        List<Consulta> consultas = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                consultas.add(mapResultSetParaConsulta(rs));
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

        Timestamp dataConsulta = rs.getTimestamp("data_consulta");
        if (dataConsulta != null) {
            consulta.setDataConsulta(dataConsulta.toLocalDateTime());
        }

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

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            consulta.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        return consulta;
    }
}