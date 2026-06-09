package com.ucsal.clinica.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Agendamento {
    private int idAgendamento;
    private int idPaciente;
    private int idProfissional;
    private int idAtendente;
    private LocalDateTime dataAgendamento;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private String motivoConsulta;
    private String observacoes;
    private int idStatus;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Getters e Setters
    public int getIdAgendamento() { return idAgendamento; }
    public void setIdAgendamento(int idAgendamento) { this.idAgendamento = idAgendamento; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdProfissional() { return idProfissional; }
    public void setIdProfissional(int idProfissional) { this.idProfissional = idProfissional; }

    public int getIdAtendente() { return idAtendente; }
    public void setIdAtendente(int idAtendente) { this.idAtendente = idAtendente; }

    public LocalDateTime getDataAgendamento() { return dataAgendamento; }
    public void setDataAgendamento(LocalDateTime dataAgendamento) { this.dataAgendamento = dataAgendamento; }

    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public LocalTime getHoraConsulta() { return horaConsulta; }
    public void setHoraConsulta(LocalTime horaConsulta) { this.horaConsulta = horaConsulta; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public int getIdStatus() { return idStatus; }
    public void setIdStatus(int idStatus) { this.idStatus = idStatus; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}

// ============================================================
