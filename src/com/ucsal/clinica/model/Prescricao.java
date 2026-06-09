package com.ucsal.clinica.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prescricao {
    private int idPrescricao;
    private int idConsulta;
    private int idPaciente;
    private int idProfissional;
    private String numeroPrescricao;
    private LocalDateTime dataPrescricao;
    private LocalDate dataValidade;
    private String observacoes;
    private LocalDateTime dataCriacao;

    // Getters e Setters
    public int getIdPrescricao() { return idPrescricao; }
    public void setIdPrescricao(int idPrescricao) { this.idPrescricao = idPrescricao; }

    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdProfissional() { return idProfissional; }
    public void setIdProfissional(int idProfissional) { this.idProfissional = idProfissional; }

    public String getNumeroPrescricao() { return numeroPrescricao; }
    public void setNumeroPrescricao(String numeroPrescricao) { this.numeroPrescricao = numeroPrescricao; }

    public LocalDateTime getDataPrescricao() { return dataPrescricao; }
    public void setDataPrescricao(LocalDateTime dataPrescricao) { this.dataPrescricao = dataPrescricao; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}

// ============================================================
