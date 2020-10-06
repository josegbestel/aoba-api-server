package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Endereco;
import com.redeaoba.api.model.Pedido;

import java.util.ArrayList;
import java.util.List;

public class DetalhesPedidoRealizadoModel {

    private float totalItens;
    private float totalFrete;
    private String metodoPagamento;
    private EnderecoModel enderecoEntrega;
    private List<ItemDetalhePedidoRealizadoModel> itens = new ArrayList<>();

    public static DetalhesPedidoRealizadoModel toModel(Pedido pedido) {
        DetalhesPedidoRealizadoModel detalhes = new DetalhesPedidoRealizadoModel();
        detalhes.setTotalItens(pedido.getValorTotal());
        detalhes.setTotalFrete(pedido.getValorFrete());
        detalhes.setMetodoPagamento("Dinheiro");
        detalhes.setEnderecoEntrega(EnderecoModel.toModel(pedido.getEndereco()));
        pedido.getItensCarrinho().forEach(i -> detalhes.addItem(ItemDetalhePedidoRealizadoModel.toModel(i)));

        return detalhes;
    }

    public float getTotalItens() {
        return totalItens;
    }

    public void setTotalItens(float totalItens) {
        this.totalItens = totalItens;
    }

    public float getTotalFrete() {
        return totalFrete;
    }

    public void setTotalFrete(float totalFrete) {
        this.totalFrete = totalFrete;
    }

    public float getTotalPedido() {
        return totalFrete+totalItens;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public EnderecoModel getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(EnderecoModel enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public List<ItemDetalhePedidoRealizadoModel> getItens() {
        return itens;
    }

    public void setItens(List<ItemDetalhePedidoRealizadoModel> itens) {
        this.itens = itens;
    }

    public void addItem(ItemDetalhePedidoRealizadoModel item){
        this.itens.add(item);
    }

}
