package com.ucsal.clinica.model;

import java.time.LocalDateTime;

public class ProfissionalSaude {
    private int idProfissional;
    private int idUsuario;
    private String cpf;
    private String numeroRegistro;
    private String tipoProfissional;
    private int idEspecialidade;
    private boolean ativo;
    private LocalDateTime dataCriacao;

    // Getters e Setters
    public int getIdProfissional() { return idProfissional; }
    public void setIdProfissional(int idProfissional) { this.idProfissional = idProfissional; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public String getTipoProfissional() { return tipoProfissional; }
    public void setTipoProfissional(String tipoProfissional) { this.tipoProfissional = tipoProfissional; }

    public int getIdEspecialidade() { return idEspecialidade; }
    public void setIdEspecialidade(int idEspecialidade) { this.idEspecialidade = idEspecialidade; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}

// ============================================================
