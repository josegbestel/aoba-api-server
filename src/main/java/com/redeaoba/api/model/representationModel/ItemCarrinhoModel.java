package com.redeaoba.api.model.representationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.DataEntrega;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.enums.StatusItem;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemCarrinhoModel implements Serializable {

    @NotNull
    private long anuncioId;
    @NotNull
    private int quantidade;
    private String foto;
    private DataEntrega dataEntrega;
    private float valor;

    public long getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(long anuncioId) {
        this.anuncioId = anuncioId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public DataEntrega getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(DataEntrega dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public ItemCarrinho byModel(Anuncio anuncio){
        ItemCarrinho item = new ItemCarrinho();
        item.setAnuncio(anuncio);
        item.setStatus(StatusItem.PENDENTE);
        item.setQuantidade(this.quantidade);

        return item;
    }

    static public ItemCarrinhoModel toModel(ItemCarrinho item){
        ItemCarrinhoModel model = new ItemCarrinhoModel();
        model.setAnuncioId(item.getAnuncio().getId());
        model.setQuantidade(item.getQuantidade());
        model.setFoto(item.getAnuncio().getFotos().get(1));
        model.setValor(item.getAnuncio().getValor());
        model.setDataEntrega(item.getAnuncio().getDatasEntregas().get(0));

        return model;
    }
}
