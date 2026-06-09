package com.ucsal.clinica.model;

import java.time.LocalDateTime;

public class Diagnostico {
    private int idDiagnostico;
    private int idConsulta;
    private int idPaciente;
    private String descricaoDiagnostico;
    private String cid10;
    private boolean principal;
    private LocalDateTime dataDiagnostico;

    // Getters e Setters
    public int getIdDiagnostico() { return idDiagnostico; }
    public void setIdDiagnostico(int idDiagnostico) { this.idDiagnostico = idDiagnostico; }

    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getDescricaoDiagnostico() { return descricaoDiagnostico; }
    public void setDescricaoDiagnostico(String descricaoDiagnostico) { this.descricaoDiagnostico = descricaoDiagnostico; }

    public String getCid10() { return cid10; }
    public void setCid10(String cid10) { this.cid10 = cid10; }

    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }

    public LocalDateTime getDataDiagnostico() { return dataDiagnostico; }
    public void setDataDiagnostico(LocalDateTime dataDiagnostico) { this.dataDiagnostico = dataDiagnostico; }
}

// ============================================================
