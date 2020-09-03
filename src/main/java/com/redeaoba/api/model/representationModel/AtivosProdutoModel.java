package com.redeaoba.api.model.representationModel;

import java.io.Serializable;
import java.util.List;

public class AtivosProdutoModel implements Serializable {

    private String nome;
    private String foto;
    private List<AtivosAnuncioModel> anuncios;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<AtivosAnuncioModel> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<AtivosAnuncioModel> anuncios) {
        this.anuncios = anuncios;
    }
}
