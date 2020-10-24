package com.redeaoba.api.model.enums;

public enum StatusItem {

    PENDENTE,
    CONFIRMADO,
    CANCELADO,
    AGUARDANDO_CONFIRMACAO,
    FINALIZADO,
    REMOVIDO;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
