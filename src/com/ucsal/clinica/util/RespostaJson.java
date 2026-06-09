package com.ucsal.clinica.util;

/**
 * Resposta padrão que os Servlets enviam ao JavaScript.
 * Serializada para JSON pelo Gson.
 */
public class RespostaJson {
    private boolean sucesso;
    private String mensagem;
    private Object dados; // pode ser uma lista, um objeto, etc.

    public RespostaJson(boolean sucesso, String mensagem, Object dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public RespostaJson(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public Object getDados() { return dados; }
}
