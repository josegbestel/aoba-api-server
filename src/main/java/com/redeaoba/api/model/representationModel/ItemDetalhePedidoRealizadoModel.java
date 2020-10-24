package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.enums.DiaSemana;
import com.redeaoba.api.model.enums.StatusItem;

public class ItemDetalhePedidoRealizadoModel {

    private long id;
    private String nomeItem;
    private String foto;
    private int qtde;
    private float total;
    private DiaSemana entrega;
    private StatusItem statusItem;

    public static ItemDetalhePedidoRealizadoModel toModel(ItemCarrinho item) {
        ItemDetalhePedidoRealizadoModel model = new ItemDetalhePedidoRealizadoModel();
        model.setId(item.getId());
        model.setNomeItem(item.getAnuncio().getProduto().getNome());
        model.setFoto(item.getAnuncio().getFotos().get(0));
        model.setQtde(item.getQuantidade());
        model.setTotal(item.getValorTotal());
        model.setEntrega(item.getAnuncio().getDatasEntregas().get(0).getDiaSemana());
        model.setStatusItem(item.getStatus());

        return model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public DiaSemana getEntrega() {
        return entrega;
    }

    public void setEntrega(DiaSemana entrega) {
        this.entrega = entrega;
    }

    public StatusItem getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(StatusItem statusItem) {
        this.statusItem = statusItem;
    }
}
