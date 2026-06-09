package com.ucsal.clinica.servlet;

import com.google.gson.Gson;
import com.ucsal.clinica.dao.AgendamentoDAO;
import com.ucsal.clinica.dao.PacienteDAO;
import com.ucsal.clinica.util.DatabaseConfig;
import com.ucsal.clinica.util.GsonFactory;
import com.ucsal.clinica.util.RespostaJson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private final Gson gson = GsonFactory.criar();

    // GET /dashboard -> os 4 números do painel (todos os IDs lidos pelo script.js)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        try {
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("totalPacientes", pacienteDAO.listarTodos().size());
            stats.put("agendamentosHoje", agendamentoDAO.contarAgendamentosHoje());
            stats.put("totalProfissionais", contar(
                    "SELECT COUNT(*) FROM clinica.profissional_saude WHERE ativo = TRUE"));
            stats.put("examesPendentes", contar(
                    "SELECT COUNT(*) FROM clinica.solicitacao_exame s " +
                    "WHERE NOT EXISTS (SELECT 1 FROM clinica.resultado_exame r " +
                    "WHERE r.id_solicitacao = s.id_solicitacao)"));

            resp.getWriter().write(gson.toJson(stats));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(false, "Erro ao carregar dashboard: " + e.getMessage())));
        }
    }

    // Helper para contadores simples (COUNT(*))
    private int contar(String sql) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
