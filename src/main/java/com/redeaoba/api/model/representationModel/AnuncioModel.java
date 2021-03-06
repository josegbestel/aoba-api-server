package com.redeaoba.api.model.representationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.util.StringListConverter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnuncioModel implements Serializable {

    private Long id;

    @NotNull
    private long produtoId;

    @NotNull
    private long produtorId;

    @NotNull
    private Float valor;

    @Convert(converter = StringListConverter.class)
    private List<String> fotos;

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

    public long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(long produtoId) {
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

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
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
        model.setFotos(anuncio.getFotos());

        return model;
    }


    public static List<AnuncioModel> toModel(List<Anuncio> anuncios) {
        List<AnuncioModel> anunciosModel = new ArrayList<>();
        anuncios.forEach(a -> anunciosModel.add(AnuncioModel.toModel(a)));
        return anunciosModel;
    }
}
