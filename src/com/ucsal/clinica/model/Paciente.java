package com.ucsal.clinica.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Paciente {
    private int idPaciente;
    private int idUsuario;
    private String cpf;
    private LocalDate dataNascimento;
    private char sexo;
    private String numeroRegistro;
    private LocalDate dataPrimeiroAtendimento;
    private String observacoes;
    private boolean ativo;
    private LocalDateTime dataCriacao;

    // Getters e Setters
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public char getSexo() { return sexo; }
    public void setSexo(char sexo) { this.sexo = sexo; }

    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public LocalDate getDataPrimeiroAtendimento() { return dataPrimeiroAtendimento; }
    public void setDataPrimeiroAtendimento(LocalDate dataPrimeiroAtendimento) { this.dataPrimeiroAtendimento = dataPrimeiroAtendimento; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}

// ============================================================
