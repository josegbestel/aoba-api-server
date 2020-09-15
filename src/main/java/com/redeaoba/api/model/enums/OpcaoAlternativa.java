package com.redeaoba.api.model.enums;

public enum OpcaoAlternativa {

    ACEITAR_SUGESTAO,
    CANCELAR_PRODUTO,
    CANCELAR_PEDIDO;

    private String opcao;

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }
}
