package com.redeaoba.api.model.enums;

public enum StatusItem {

    PENDENTE,
    CONFIRMADO,
    CANCELADO,
    BUSCANDO_OPCAO,
    FINALIZADO;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
