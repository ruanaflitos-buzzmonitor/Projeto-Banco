package com.ucsal.clinica.dao;

import com.ucsal.clinica.model.Agendamento;
import com.ucsal.clinica.util.DatabaseConfig;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AgendamentoDAO {

    public boolean inserir(Agendamento agendamento) throws SQLException {
        String sql = "INSERT INTO clinica.agendamento " +
                "(id_paciente, id_profissional, id_atendente, data_agendamento, data_consulta, " +
                "hora_consulta, motivo_consulta, observacoes, id_status, data_criacao, data_atualizacao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, agendamento.getIdPaciente());
            pstmt.setInt(2, agendamento.getIdProfissional());
            if (agendamento.getIdAtendente() > 0) {
                pstmt.setInt(3, agendamento.getIdAtendente());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setTimestamp(4, Timestamp.valueOf(valorOuAgora(agendamento.getDataAgendamento())));
            pstmt.setDate(5, Date.valueOf(agendamento.getDataConsulta()));
            pstmt.setTime(6, Time.valueOf(agendamento.getHoraConsulta()));
            pstmt.setString(7, agendamento.getMotivoConsulta());
            pstmt.setString(8, agendamento.getObservacoes());
            pstmt.setInt(9, agendamento.getIdStatus() > 0 ? agendamento.getIdStatus() : 1);
            pstmt.setTimestamp(10, Timestamp.valueOf(valorOuAgora(agendamento.getDataCriacao())));
            pstmt.setTimestamp(11, Timestamp.valueOf(valorOuAgora(agendamento.getDataAtualizacao())));

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
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

    public Agendamento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clinica.agendamento WHERE id_agendamento = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }

        return null;
    }

    public List<Agendamento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clinica.agendamento ORDER BY data_consulta DESC, hora_consulta DESC";
        List<Agendamento> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }

        return lista;
    }

    public List<Agendamento> buscarPorData(LocalDate data) throws SQLException {
        String sql = "SELECT * FROM clinica.agendamento WHERE data_consulta = ? ORDER BY hora_consulta";
        List<Agendamento> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(data));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        }

        return lista;
    }

    public List<Agendamento> buscarPorPaciente(int idPaciente) throws SQLException {
        String sql = "SELECT * FROM clinica.agendamento WHERE id_paciente = ? " +
                "ORDER BY data_consulta DESC, hora_consulta DESC";
        List<Agendamento> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPaciente);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        }

        return lista;
    }

    public List<Map<String, Object>> listarResumo() throws SQLException {
        String sql = "SELECT a.id_agendamento, a.data_consulta, a.hora_consulta, " +
                "a.motivo_consulta, a.observacoes, sa.nome_status, " +
                "up.nome_completo AS paciente, uf.nome_completo AS profissional " +
                "FROM clinica.agendamento a " +
                "JOIN clinica.paciente p ON a.id_paciente = p.id_paciente " +
                "JOIN clinica.usuario up ON p.id_usuario = up.id_usuario " +
                "JOIN clinica.profissional_saude ps ON a.id_profissional = ps.id_profissional " +
                "JOIN clinica.usuario uf ON ps.id_usuario = uf.id_usuario " +
                "JOIN clinica.status_agendamento sa ON a.id_status = sa.id_status " +
                "ORDER BY a.data_consulta DESC, a.hora_consulta DESC";

        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", rs.getInt("id_agendamento"));
                item.put("data", rs.getDate("data_consulta").toLocalDate().toString());
                item.put("hora", rs.getTime("hora_consulta").toLocalTime().toString());
                item.put("paciente", rs.getString("paciente"));
                item.put("profissional", rs.getString("profissional"));
                item.put("motivo", rs.getString("motivo_consulta"));
                item.put("observacoes", rs.getString("observacoes"));
                item.put("status", rs.getString("nome_status"));
                lista.add(item);
            }
        }

        return lista;
    }

    public int contarAgendamentosHoje() throws SQLException {
        String sql = "SELECT COUNT(*) FROM clinica.agendamento WHERE data_consulta = CURRENT_DATE";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    public boolean atualizarStatus(int idAgendamento, int idStatus) throws SQLException {
        String sql = "UPDATE clinica.agendamento SET id_status = ?, data_atualizacao = ? " +
                "WHERE id_agendamento = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idStatus);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, idAgendamento);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Agendamento mapResultSet(ResultSet rs) throws SQLException {
        Agendamento agendamento = new Agendamento();

        agendamento.setIdAgendamento(rs.getInt("id_agendamento"));
        agendamento.setIdPaciente(rs.getInt("id_paciente"));
        agendamento.setIdProfissional(rs.getInt("id_profissional"));
        agendamento.setIdAtendente(rs.getInt("id_atendente"));
        agendamento.setDataAgendamento(toLocalDateTime(rs.getTimestamp("data_agendamento")));
        agendamento.setDataConsulta(rs.getDate("data_consulta").toLocalDate());
        agendamento.setHoraConsulta(rs.getTime("hora_consulta").toLocalTime());
        agendamento.setMotivoConsulta(rs.getString("motivo_consulta"));
        agendamento.setObservacoes(rs.getString("observacoes"));
        agendamento.setIdStatus(rs.getInt("id_status"));
        agendamento.setDataCriacao(toLocalDateTime(rs.getTimestamp("data_criacao")));
        agendamento.setDataAtualizacao(toLocalDateTime(rs.getTimestamp("data_atualizacao")));

        return agendamento;
    }

    private LocalDateTime valorOuAgora(LocalDateTime valor) {
        return valor != null ? valor : LocalDateTime.now();
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
