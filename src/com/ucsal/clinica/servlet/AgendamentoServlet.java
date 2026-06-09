package com.ucsal.clinica.servlet;

import com.google.gson.Gson;
import com.ucsal.clinica.dao.AgendamentoDAO;
import com.ucsal.clinica.model.Agendamento;
import com.ucsal.clinica.util.GsonFactory;
import com.ucsal.clinica.util.RespostaJson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@WebServlet("/agendamentos")
public class AgendamentoServlet extends HttpServlet {

    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private final Gson gson = GsonFactory.criar();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        try {
            String dataFiltro = req.getParameter("data");
            if (dataFiltro != null && !dataFiltro.trim().isEmpty()) {
                List<Agendamento> agendamentos = agendamentoDAO.buscarPorData(LocalDate.parse(dataFiltro));
                resp.getWriter().write(gson.toJson(agendamentos));
                return;
            }

            List<Map<String, Object>> agendamentos = agendamentoDAO.listarResumo();
            resp.getWriter().write(gson.toJson(agendamentos));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(false, "Erro ao buscar agendamentos: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        try {
            AgendamentoRequest dados = gson.fromJson(req.getReader(), AgendamentoRequest.class);
            validar(dados);

            Agendamento agendamento = new Agendamento();
            agendamento.setIdPaciente(dados.idPaciente);
            agendamento.setIdProfissional(dados.idProfissional);
            agendamento.setDataConsulta(LocalDate.parse(dados.dataConsulta));
            agendamento.setHoraConsulta(LocalTime.parse(dados.horaConsulta));
            agendamento.setMotivoConsulta(dados.motivoConsulta.trim());
            agendamento.setObservacoes(dados.observacoes);
            agendamento.setDataAgendamento(LocalDateTime.now());
            agendamento.setIdStatus(1);
            agendamento.setDataCriacao(LocalDateTime.now());
            agendamento.setDataAtualizacao(LocalDateTime.now());

            if (agendamentoDAO.inserir(agendamento)) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(gson.toJson(
                        new RespostaJson(true, "Agendamento realizado com sucesso!", agendamento)));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(
                        new RespostaJson(false, "Erro ao salvar o agendamento")));
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

    private void configurarResposta(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
    }

    private void validar(AgendamentoRequest dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Dados do agendamento nao foram enviados");
        }
        if (dados.idPaciente <= 0) {
            throw new IllegalArgumentException("Paciente invalido");
        }
        if (dados.idProfissional <= 0) {
            throw new IllegalArgumentException("Profissional invalido");
        }
        if (dados.dataConsulta == null || dados.dataConsulta.trim().isEmpty()) {
            throw new IllegalArgumentException("Data da consulta e obrigatoria");
        }
        if (dados.horaConsulta == null || dados.horaConsulta.trim().isEmpty()) {
            throw new IllegalArgumentException("Hora da consulta e obrigatoria");
        }
        if (dados.motivoConsulta == null || dados.motivoConsulta.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo da consulta e obrigatorio");
        }
    }

    private static class AgendamentoRequest {
        int idPaciente;
        int idProfissional;
        String dataConsulta;
        String horaConsulta;
        String motivoConsulta;
        String observacoes;
    }
}
