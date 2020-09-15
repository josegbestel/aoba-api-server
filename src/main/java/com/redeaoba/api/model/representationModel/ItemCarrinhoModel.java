package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.enums.StatusItem;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ItemCarrinhoModel implements Serializable {

    @NotNull
    private long anuncioId;

    @NotNull
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

    public ItemCarrinho byModel(Anuncio anuncio){
        ItemCarrinho item = new ItemCarrinho();
        item.setAnuncio(anuncio);
        item.setStatus(StatusItem.PENDENTE);
        item.setQuantidade(this.quantidade);

        return item;
    }
}
