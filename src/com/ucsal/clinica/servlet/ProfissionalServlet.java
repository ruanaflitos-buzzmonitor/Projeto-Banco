package com.ucsal.clinica.servlet;

import com.google.gson.Gson;
import com.ucsal.clinica.dao.UsuarioDAO;
import com.ucsal.clinica.model.Usuario;
import com.ucsal.clinica.util.DatabaseConfig;
import com.ucsal.clinica.util.GsonFactory;
import com.ucsal.clinica.util.RespostaJson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/profissionais")
public class ProfissionalServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = GsonFactory.criar();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        try {
            resp.getWriter().write(gson.toJson(listarProfissionais()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(false, "Erro ao buscar profissionais: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        Connection conn = null;

        try {
            ProfissionalRequest dados = gson.fromJson(req.getReader(), ProfissionalRequest.class);
            validar(dados);

            Usuario usuario = new Usuario();
            usuario.setNomeCompleto(dados.nome.trim());
            usuario.setEmail(dados.email.trim());
            usuario.setSenhaHash("123456");
            usuario.setAtivo(true);
            usuario.setIdPerfil(resolvePerfil(dados.tipo));
            usuario.setDataCriacao(LocalDateTime.now());
            usuario.setDataAtualizacao(LocalDateTime.now());

            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            if (!usuarioDAO.inserir(usuario, conn)) {
                throw new IllegalStateException("Erro ao criar o usuario do profissional");
            }

            int idProfissional = inserirProfissional(conn, usuario.getIdUsuario(), dados);
            conn.commit();

            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("id", idProfissional);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(true, "Profissional cadastrado com sucesso!", resposta)));
        } catch (com.google.gson.JsonParseException | IllegalArgumentException e) {
            rollback(conn);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new RespostaJson(false, e.getMessage())));
        } catch (Exception e) {
            rollback(conn);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(false, "Erro interno: " + e.getMessage())));
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }

    private List<Map<String, Object>> listarProfissionais() throws SQLException {
        String sql = "SELECT ps.id_profissional, u.nome_completo, ps.numero_registro, " +
                "ps.tipo_profissional, e.nome_especialidade, ps.ativo " +
                "FROM clinica.profissional_saude ps " +
                "JOIN clinica.usuario u ON ps.id_usuario = u.id_usuario " +
                "LEFT JOIN clinica.especialidade e ON ps.id_especialidade = e.id_especialidade " +
                "ORDER BY u.nome_completo";

        List<Map<String, Object>> profissionais = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", rs.getInt("id_profissional"));
                item.put("nome", rs.getString("nome_completo"));
                item.put("registro", rs.getString("numero_registro"));
                item.put("tipo", rs.getString("tipo_profissional"));
                item.put("especialidade", rs.getString("nome_especialidade"));
                item.put("status", rs.getBoolean("ativo") ? "Ativo" : "Inativo");
                profissionais.add(item);
            }
        }

        return profissionais;
    }

    private int inserirProfissional(Connection conn, int idUsuario, ProfissionalRequest dados) throws SQLException {
        String sql = "INSERT INTO clinica.profissional_saude " +
                "(id_usuario, cpf, numero_registro, tipo_profissional, id_especialidade, ativo, data_criacao) " +
                "VALUES (?, ?, ?, ?, ?, TRUE, ?) ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, somenteDigitos(dados.cpf));
            pstmt.setString(3, dados.registro.trim());
            pstmt.setString(4, dados.tipo.trim());
            pstmt.setInt(5, buscarEspecialidade(conn, dados.especialidade.trim()));
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }

        throw new SQLException("Nenhum profissional foi inserido");
    }

    private int buscarEspecialidade(Connection conn, String nome) throws SQLException {
        String sql = "SELECT id_especialidade FROM clinica.especialidade WHERE nome_especialidade = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new IllegalArgumentException("Especialidade invalida");
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (Exception ignored) {
            }
        }
    }

    private int resolvePerfil(String tipo) {
        if (tipo != null && tipo.toLowerCase().contains("enfermeiro")) {
            return 3;
        }
        return 2;
    }

    private void configurarResposta(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
    }

    private void validar(ProfissionalRequest dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Dados do profissional nao foram enviados");
        }
        if (vazio(dados.nome)) {
            throw new IllegalArgumentException("Nome e obrigatorio");
        }
        if (vazio(dados.cpf)) {
            throw new IllegalArgumentException("CPF e obrigatorio");
        }
        if (vazio(dados.registro)) {
            throw new IllegalArgumentException("Registro profissional e obrigatorio");
        }
        if (vazio(dados.tipo)) {
            throw new IllegalArgumentException("Tipo profissional e obrigatorio");
        }
        if (vazio(dados.especialidade)) {
            throw new IllegalArgumentException("Especialidade e obrigatoria");
        }
        if (vazio(dados.email)) {
            throw new IllegalArgumentException("Email e obrigatorio");
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String somenteDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("[^0-9]", "");
    }

    private static class ProfissionalRequest {
        String nome;
        String cpf;
        String registro;
        String tipo;
        String especialidade;
        String email;
    }
}
