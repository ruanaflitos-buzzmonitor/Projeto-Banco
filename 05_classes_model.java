// ============================================================
// ARQUIVO: src/com/ucsal/clinica/model/Usuario.java
// ============================================================

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
// ARQUIVO: src/com/ucsal/clinica/model/Paciente.java
// ============================================================

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
// ARQUIVO: src/com/ucsal/clinica/model/ProfissionalSaude.java
// ============================================================

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
// ARQUIVO: src/com/ucsal/clinica/model/Agendamento.java
// ============================================================

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
// ARQUIVO: src/com/ucsal/clinica/model/Consulta.java
// ============================================================

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
// ARQUIVO: src/com/ucsal/clinica/model/Prescricao.java
// ============================================================

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
// ARQUIVO: src/com/ucsal/clinica/model/Diagnostico.java
// ============================================================

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
// ARQUIVO: src/com/ucsal/clinica/model/SolicitacaoExame.java
// ============================================================

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
