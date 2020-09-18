package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.enums.StatusItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemCarrinhoLiteModel implements Serializable {

    private long anuncioId;
    private int quantidade;

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

    static ItemCarrinhoLiteModel byModel(ItemCarrinhoModel itemCarrinhoModel){
        ItemCarrinhoLiteModel request = new ItemCarrinhoLiteModel();
        request.setAnuncioId(itemCarrinhoModel.getAnuncioId());
        request.setQuantidade(itemCarrinhoModel.getQuantidade());

        return request;
    }

    static List<ItemCarrinhoLiteModel> byModel(List<ItemCarrinhoModel> itens){
        List<ItemCarrinhoLiteModel> itensLite = new ArrayList<>();
        for(ItemCarrinhoModel item : itens){
            itensLite.add(ItemCarrinhoLiteModel.byModel(item));
        }

        return itensLite;
    }

    public ItemCarrinho byModel(Anuncio anuncio){
        ItemCarrinho item = new ItemCarrinho();
        item.setAnuncio(anuncio);
        item.setStatus(StatusItem.PENDENTE);
        item.setQuantidade(this.quantidade);

        return item;
    }
}
