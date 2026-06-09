package com.ucsal.clinica.dao;

import com.ucsal.clinica.model.Usuario;
import com.ucsal.clinica.util.DatabaseConfig;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean inserir(Usuario usuario) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return inserir(usuario, conn);
        }
    }

    public boolean inserir(Usuario usuario, Connection conn) throws SQLException {
        String sql = "INSERT INTO clinica.usuario " +
                     "(email, senha_hash, nome_completo, ativo, id_perfil, data_criacao, data_atualizacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, usuario.getEmail());
            pstmt.setString(2, usuario.getSenhaHash());
            pstmt.setString(3, usuario.getNomeCompleto());
            pstmt.setBoolean(4, usuario.isAtivo());
            pstmt.setInt(5, usuario.getIdPerfil());
            pstmt.setTimestamp(6, Timestamp.valueOf(usuario.getDataCriacao() != null
                    ? usuario.getDataCriacao() : LocalDateTime.now()));
            pstmt.setTimestamp(7, Timestamp.valueOf(usuario.getDataAtualizacao() != null
                    ? usuario.getDataAtualizacao() : LocalDateTime.now()));
            
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
