package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Pedido;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoProdutorNovoModel implements Serializable {

    private long idPedido;
    private String nomeFantasiaComerciante;
    private String nomeComerciante;
    private LocalDateTime prazoResposta;
    private List<ItemPedidoNovoModel> itens = new ArrayList<>();

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    public String getNomeFantasiaComerciante() {
        return nomeFantasiaComerciante;
    }

    public void setNomeFantasiaComerciante(String nomeFantasiaComerciante) {
        this.nomeFantasiaComerciante = nomeFantasiaComerciante;
    }

    public String getNomeComerciante() {
        return nomeComerciante;
    }

    public void setNomeComerciante(String nomeComerciante) {
        this.nomeComerciante = nomeComerciante;
    }

    public LocalDateTime getPrazoResposta() {
        return prazoResposta;
    }

    public void setPrazoResposta(LocalDateTime prazoResposta) {
        this.prazoResposta = prazoResposta;
    }

    public List<ItemPedidoNovoModel> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoNovoModel> itens) {
        this.itens = itens;
    }

    public void addItem(ItemPedidoNovoModel item){
        this.itens.add(item);
    }

    public static PedidoProdutorNovoModel toModel(Pedido pedido){
        PedidoProdutorNovoModel novo = new PedidoProdutorNovoModel();
        novo.setIdPedido(pedido.getId());
        novo.setNomeComerciante(pedido.getComprador().getNome());
        novo.setNomeFantasiaComerciante(pedido.getComprador().getNomeFantasia());
        pedido.getItensCarrinho().forEach(i -> novo.addItem(ItemPedidoNovoModel.toModel(i)));
        novo.setPrazoResposta(pedido.getPrazoResposta());

        return novo;
    }
}
