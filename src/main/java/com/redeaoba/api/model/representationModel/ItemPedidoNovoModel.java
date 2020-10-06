package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.ItemCarrinho;

import java.io.Serializable;

public class ItemPedidoNovoModel implements Serializable {

    private String item;
    private int quantidade;
    private float valor;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public static ItemPedidoNovoModel toModel(ItemCarrinho itemCarrinho) {
        ItemPedidoNovoModel itemNovo = new ItemPedidoNovoModel();
        itemNovo.setItem(itemCarrinho.getAnuncio().getProduto().getNome());
        itemNovo.setQuantidade(itemCarrinho.getQuantidade());
        itemNovo.setValor(itemCarrinho.getAnuncio().getValor());

        return itemNovo;
    }
}
