package com.ucsal.clinica.servlet;

import com.google.gson.Gson;
import com.ucsal.clinica.dao.PacienteDAO;
import com.ucsal.clinica.dao.UsuarioDAO;
import com.ucsal.clinica.model.Paciente;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/pacientes")
public class PacienteServlet extends HttpServlet {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = GsonFactory.criar();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        try {
            List<Paciente> pacientes = pacienteDAO.listarTodos();
            List<Map<String, Object>> saida = new ArrayList<>();

            for (Paciente paciente : pacientes) {
                Usuario usuario = usuarioDAO.buscarPorId(paciente.getIdUsuario());

                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", paciente.getIdPaciente());
                item.put("nome", usuario != null ? usuario.getNomeCompleto() : "");
                item.put("cpf", paciente.getCpf());
                item.put("dataNascimento", paciente.getDataNascimento() != null
                        ? paciente.getDataNascimento().toString() : "");
                item.put("sexo", String.valueOf(paciente.getSexo()));
                item.put("telefone", pacienteDAO.buscarTelefonePrincipal(paciente.getIdPaciente()));
                item.put("email", usuario != null ? usuario.getEmail() : "");
                saida.add(item);
            }

            resp.getWriter().write(gson.toJson(saida));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(false, "Erro ao buscar pacientes: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configurarResposta(resp);

        Connection conn = null;

        try {
            PacienteRequest dados = gson.fromJson(req.getReader(), PacienteRequest.class);
            validar(dados);

            Usuario usuario = new Usuario();
            usuario.setNomeCompleto(dados.nome.trim());
            usuario.setEmail(dados.email.trim());
            usuario.setSenhaHash("123456");
            usuario.setAtivo(true);
            usuario.setIdPerfil(5);
            usuario.setDataCriacao(LocalDateTime.now());
            usuario.setDataAtualizacao(LocalDateTime.now());

            Paciente paciente = new Paciente();
            paciente.setCpf(somenteDigitos(dados.cpf));
            paciente.setDataNascimento(LocalDate.parse(dados.dataNascimento));
            paciente.setSexo(dados.sexo != null && !dados.sexo.trim().isEmpty()
                    ? dados.sexo.trim().charAt(0) : 'M');
            paciente.setAtivo(true);
            paciente.setDataCriacao(LocalDateTime.now());

            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            if (!usuarioDAO.inserir(usuario, conn)) {
                throw new IllegalStateException("Erro ao criar o usuario do paciente");
            }

            paciente.setIdUsuario(usuario.getIdUsuario());
            if (!pacienteDAO.inserir(paciente, conn)) {
                throw new IllegalStateException("Erro ao salvar o paciente");
            }

            pacienteDAO.inserirContatoTelefone(conn, paciente.getIdPaciente(), dados.telefone);
            conn.commit();

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(
                    new RespostaJson(true, "Paciente cadastrado com sucesso!", paciente)));
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

    private void configurarResposta(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
    }

    private void validar(PacienteRequest dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Dados do paciente nao foram enviados");
        }
        if (vazio(dados.nome)) {
            throw new IllegalArgumentException("Nome e obrigatorio");
        }
        if (vazio(dados.cpf) || somenteDigitos(dados.cpf).length() != 11) {
            throw new IllegalArgumentException("CPF invalido");
        }
        if (vazio(dados.email)) {
            throw new IllegalArgumentException("Email e obrigatorio");
        }
        if (vazio(dados.dataNascimento)) {
            throw new IllegalArgumentException("Data de nascimento e obrigatoria");
        }
        if (vazio(dados.sexo)) {
            throw new IllegalArgumentException("Sexo e obrigatorio");
        }
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (Exception ignored) {
            }
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String somenteDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("[^0-9]", "");
    }

    private static class PacienteRequest {
        String nome;
        String cpf;
        String email;
        String dataNascimento;
        String sexo;
        String telefone;
    }
}
