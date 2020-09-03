package com.redeaoba.api.model.enums;

import java.io.Serializable;

public enum SecaoProduto implements Serializable {

    FRUTA,
    VERDURA,
    LEGUME;

    private String classificacao;

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
}
