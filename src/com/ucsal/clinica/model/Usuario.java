package com.ucsal.clinica.model;

import java.time.LocalDateTime;

public class Usuario {
    private int idUsuario;
    private String email;
    private String senhaHash;
    private String nomeCompleto;
    private boolean ativo;
    private int idPerfil;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Getters e Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}

// ============================================================
