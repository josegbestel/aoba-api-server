package com.redeaoba.api.model.representationModel;

import java.io.Serializable;
import java.util.List;

public class AtivosCategoriaModel implements Serializable {

    private String categoria;
    private String foto;
    private List<AtivosProdutoModel> produtos;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<AtivosProdutoModel> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<AtivosProdutoModel> produtos) {
        this.produtos = produtos;
    }
}
