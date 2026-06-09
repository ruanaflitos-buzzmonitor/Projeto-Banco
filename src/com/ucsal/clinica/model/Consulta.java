package com.ucsal.clinica.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Consulta {
    private int idConsulta;
    private int idAgendamento;
    private int idPaciente;
    private int idProfissional;
    private LocalDateTime dataConsulta;
    private String queixaPrincipal;
    private String anamnese;
    private String achadosExameFisico;
    private String hipoteseDiagnostica;
    private String notasClinicas;
    private BigDecimal peso;
    private BigDecimal altura;
    private String pressaoArterial;
    private int frequenciaCardiaca;
    private BigDecimal temperatura;
    private LocalDateTime dataCriacao;

    // Getters e Setters
    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public int getIdAgendamento() { return idAgendamento; }
    public void setIdAgendamento(int idAgendamento) { this.idAgendamento = idAgendamento; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdProfissional() { return idProfissional; }
    public void setIdProfissional(int idProfissional) { this.idProfissional = idProfissional; }

    public LocalDateTime getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDateTime dataConsulta) { this.dataConsulta = dataConsulta; }

    public String getQueixaPrincipal() { return queixaPrincipal; }
    public void setQueixaPrincipal(String queixaPrincipal) { this.queixaPrincipal = queixaPrincipal; }

    public String getAnamnese() { return anamnese; }
    public void setAnamnese(String anamnese) { this.anamnese = anamnese; }

    public String getAchadosExameFisico() { return achadosExameFisico; }
    public void setAchadosExameFisico(String achadosExameFisico) { this.achadosExameFisico = achadosExameFisico; }

    public String getHipoteseDiagnostica() { return hipoteseDiagnostica; }
    public void setHipoteseDiagnostica(String hipoteseDiagnostica) { this.hipoteseDiagnostica = hipoteseDiagnostica; }

    public String getNotasClinicas() { return notasClinicas; }
    public void setNotasClinicas(String notasClinicas) { this.notasClinicas = notasClinicas; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getAltura() { return altura; }
    public void setAltura(BigDecimal altura) { this.altura = altura; }

    public String getPressaoArterial() { return pressaoArterial; }
    public void setPressaoArterial(String pressaoArterial) { this.pressaoArterial = pressaoArterial; }

    public int getFrequenciaCardiaca() { return frequenciaCardiaca; }
    public void setFrequenciaCardiaca(int frequenciaCardiaca) { this.frequenciaCardiaca = frequenciaCardiaca; }

    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}

// ============================================================
