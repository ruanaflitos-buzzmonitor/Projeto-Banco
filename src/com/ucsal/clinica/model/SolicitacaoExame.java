package com.ucsal.clinica.model;

import java.time.LocalDateTime;

public class SolicitacaoExame {
    private int idSolicitacao;
    private int idConsulta;
    private int idPaciente;
    private int idProfissional;
    private int idTipoExame;
    private LocalDateTime dataSolicitacao;
    private String motivoExame;
    private String urgencia;
    private String statusExame;
    private String observacoes;
    private LocalDateTime dataCriacao;

    // Getters e Setters
    public int getIdSolicitacao() { return idSolicitacao; }
    public void setIdSolicitacao(int idSolicitacao) { this.idSolicitacao = idSolicitacao; }

    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdProfissional() { return idProfissional; }
    public void setIdProfissional(int idProfissional) { this.idProfissional = idProfissional; }

    public int getIdTipoExame() { return idTipoExame; }
    public void setIdTipoExame(int idTipoExame) { this.idTipoExame = idTipoExame; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public String getMotivoExame() { return motivoExame; }
    public void setMotivoExame(String motivoExame) { this.motivoExame = motivoExame; }

    public String getUrgencia() { return urgencia; }
    public void setUrgencia(String urgencia) { this.urgencia = urgencia; }

    public String getStatusExame() { return statusExame; }
    public void setStatusExame(String statusExame) { this.statusExame = statusExame; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
