package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Produtor;

import java.io.Serializable;

public class AtivosAnuncioModel implements Serializable {

    private Long id;
    private float valor;
    private int qtdeMax;
    private Produtor produtor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getQtdeMax() {
        return qtdeMax;
    }

    public void setQtdeMax(int qtdeMax) {
        this.qtdeMax = qtdeMax;
    }

    public Produtor getProdutor() {
        return produtor;
    }

    public void setProdutor(Produtor produtor) {
        this.produtor = produtor;
    }
}
