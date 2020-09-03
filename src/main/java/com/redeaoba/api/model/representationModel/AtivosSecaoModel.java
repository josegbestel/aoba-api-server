package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.enums.SecaoProduto;

import java.io.Serializable;
import java.util.List;

public class AtivosSecaoModel implements Serializable {

    private SecaoProduto secao;
    private List<AtivosCategoriaModel> categorias;

    public SecaoProduto getSecao() {
        return secao;
    }

    public void setSecao(SecaoProduto secao) {
        this.secao = secao;
    }

    public List<AtivosCategoriaModel> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<AtivosCategoriaModel> categorias) {
        this.categorias = categorias;
    }
}
