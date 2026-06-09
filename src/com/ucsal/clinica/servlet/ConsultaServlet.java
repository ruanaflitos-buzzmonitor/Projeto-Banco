package com.ucsal.clinica.servlet;

import com.google.gson.Gson;
import com.ucsal.clinica.dao.AgendamentoDAO;
import com.ucsal.clinica.dao.ConsultaDAO;
import com.ucsal.clinica.model.Agendamento;
import com.ucsal.clinica.model.Consulta;
import com.ucsal.clinica.util.DatabaseConfig;
import com.ucsal.clinica.util.GsonFactory;
import com.ucsal.clinica.util.RespostaJson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/consultas")
public class ConsultaServlet extends HttpServlet {

    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private final Gson gson = GsonFactory.criar();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        try {
            resp.getWriter().write(gson.toJson(listarConsultas()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(false, "Erro ao buscar consultas: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        try {
            ConsultaRequest dados = gson.fromJson(req.getReader(), ConsultaRequest.class);
            validar(dados);

            Agendamento agendamento = agendamentoDAO.buscarPorId(dados.idAgendamento);
            if (agendamento == null) {
                throw new IllegalArgumentException("Agendamento nao encontrado");
            }

            Consulta consulta = new Consulta();
            consulta.setIdAgendamento(agendamento.getIdAgendamento());
            consulta.setIdPaciente(agendamento.getIdPaciente());
            consulta.setIdProfissional(agendamento.getIdProfissional());
            consulta.setDataConsulta(agendamento.getDataConsulta().atTime(agendamento.getHoraConsulta()));
            consulta.setQueixaPrincipal(dados.queixaPrincipal.trim());
            consulta.setAnamnese(dados.anamnese);
            consulta.setAchadosExameFisico(dados.achadosExameFisico);
            consulta.setHipoteseDiagnostica(dados.hipoteseDiagnostica);
            consulta.setNotasClinicas(dados.notasClinicas);
            consulta.setPeso(toBigDecimal(dados.peso));
            consulta.setAltura(toBigDecimal(dados.altura));
            consulta.setPressaoArterial(dados.pressaoArterial);
            consulta.setFrequenciaCardiaca(toInt(dados.frequenciaCardiaca));
            consulta.setTemperatura(toBigDecimal(dados.temperatura));

            if (consultaDAO.inserir(consulta)) {
                agendamentoDAO.atualizarStatus(agendamento.getIdAgendamento(), 2);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(gson.toJson(
                        new RespostaJson(true, "Consulta registrada com sucesso!", consulta)));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(
                        new RespostaJson(false, "Erro ao salvar a consulta")));
            }
        } catch (com.google.gson.JsonParseException | IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new RespostaJson(false, e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(false, "Erro interno: " + e.getMessage())));
        }
    }

    private List<Map<String, Object>> listarConsultas() throws SQLException {
        String sql = "SELECT c.id_consulta, c.data_consulta, c.queixa_principal, " +
                "up.nome_completo AS paciente, uf.nome_completo AS profissional " +
                "FROM clinica.consulta c " +
                "JOIN clinica.paciente p ON c.id_paciente = p.id_paciente " +
                "JOIN clinica.usuario up ON p.id_usuario = up.id_usuario " +
                "JOIN clinica.profissional_saude ps ON c.id_profissional = ps.id_profissional " +
                "JOIN clinica.usuario uf ON ps.id_usuario = uf.id_usuario " +
                "ORDER BY c.data_consulta DESC";

        List<Map<String, Object>> consultas = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", rs.getInt("id_consulta"));
                item.put("data", rs.getTimestamp("data_consulta").toLocalDateTime().toString());
                item.put("paciente", rs.getString("paciente"));
                item.put("profissional", rs.getString("profissional"));
                item.put("queixa", rs.getString("queixa_principal"));
                consultas.add(item);
            }
        }

        return consultas;
    }

    private void configurarResposta(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
    }

    private void validar(ConsultaRequest dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Dados da consulta nao foram enviados");
        }
        if (dados.idAgendamento <= 0) {
            throw new IllegalArgumentException("Agendamento invalido");
        }
        if (dados.queixaPrincipal == null || dados.queixaPrincipal.trim().isEmpty()) {
            throw new IllegalArgumentException("Queixa principal e obrigatoria");
        }
    }

    private BigDecimal toBigDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(valor.trim());
    }

    private int toInt(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(valor.trim());
    }

    private static class ConsultaRequest {
        int idAgendamento;
        String queixaPrincipal;
        String anamnese;
        String achadosExameFisico;
        String hipoteseDiagnostica;
        String notasClinicas;
        String peso;
        String altura;
        String pressaoArterial;
        String frequenciaCardiaca;
        String temperatura;
    }
}
