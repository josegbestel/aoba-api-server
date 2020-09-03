package com.redeaoba.api.model.representationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redeaoba.api.model.Anuncio;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnuncioModel implements Serializable {

    private Long id;

    @NotNull
    private Long produtoId;

    @NotNull
    private Long produtorId;

    @NotNull
    private Float valor;

    @NotNull
    private Integer qtdeMax;

    @JsonIgnore
    private LocalDateTime dtCriacao;

    @JsonIgnore
    private LocalDateTime dtValidade;

    @JsonIgnore
    private boolean ativo;

    public Long getId() {
        return id != null ? id : null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getProdutorId() {
        return produtorId;
    }

    public void setProdutorId(Long produtorId) {
        this.produtorId = produtorId;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Integer getQtdeMax() {
        return qtdeMax;
    }

    public void setQtdeMax(Integer qtdeMax) {
        this.qtdeMax = qtdeMax;
    }

    public LocalDateTime getDtCriacao() {
        return dtCriacao != null ? dtCriacao : null;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public LocalDateTime getDtValidade() {
        return dtValidade != null ? dtValidade : null;
    }

    public void setDtValidade(LocalDateTime dtValidade) {
        this.dtValidade = dtValidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    static public AnuncioModel toModel(Anuncio anuncio){
        AnuncioModel model = new AnuncioModel();
        model.setId(anuncio.getId());
        model.setProdutoId(anuncio.getProduto().getId());
        model.setProdutorId(anuncio.getProdutor().getId());
        model.setDtValidade(anuncio.getDtValidade());
        model.setDtCriacao(anuncio.getDtValidade());
        model.setValor(anuncio.getValor());
        model.setQtdeMax(anuncio.getQtdeMax());
        model.setAtivo(anuncio.isAtivo());

        return model;
    }


    public static List<AnuncioModel> toModel(List<Anuncio> anuncios) {
        List<AnuncioModel> anunciosModel = new ArrayList<>();
        anuncios.forEach(a -> anunciosModel.add(AnuncioModel.toModel(a)));
        return anunciosModel;
    }
}
