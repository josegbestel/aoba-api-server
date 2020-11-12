package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.*;

import java.util.ArrayList;
import java.util.List;

public class PedidoRealizadoModel {

    private float valorFrete;
    private float valorTotal;
    private List<ItemCarrinhoModel> itens = new ArrayList<>();
    private List<DataEntrega> datasEntrega = new ArrayList<>();
    private Comerciante comerciante;
    private PedidoNovoModel requisicaoCriar;

    public float getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(float valorFrete) {
        this.valorFrete = valorFrete;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemCarrinhoModel> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinhoModel> itens) {
        this.itens = itens;
    }

    public void addItem(ItemCarrinhoModel item){
        this.itens.add(item);
    }

    public List<DataEntrega> getDatasEntrega() {
        return datasEntrega;
    }

    public void setDatasEntrega(List<DataEntrega> datasEntrega) {
        this.datasEntrega = datasEntrega;
    }

    public void addDataEntrega(DataEntrega dataEntrega){
        if(!existsDataEntrega(dataEntrega))
            this.datasEntrega.add(dataEntrega);
    }

//    public void addDatasEntregas(List<DataEntrega> datasEntregas){
//        //Adiciona apenas se não existir uma data dessa
//        for(DataEntrega dtNova : datasEntregas){
//            if(this.datasEntrega.size() > 0){
//                if(existsDataEntrega(dtNova))
//                    this.addDataEntrega(dtNova);
//            }else
//                this.datasEntrega.add(dtNova);
//        }
//    }

    private boolean existsDataEntrega(DataEntrega dtNova){
        for(DataEntrega dt : this.datasEntrega){
            if(!dt.compare(dtNova)){
                return true;
            }
        }
        return false;
    }

    public Comerciante getComerciante() {
        return comerciante;
    }

    public void setComerciante(Comerciante comerciante) {
        this.comerciante = comerciante;
    }

    public PedidoNovoModel getRequisicaoCriar() {
        PedidoNovoModel requisicao = this.requisicaoCriar;
        return requisicaoCriar;
    }

    public void setRequisicaoCriar(PedidoNovoModel requisicaoCriar) {
        this.requisicaoCriar = requisicaoCriar;
    }

    static public List<PedidoRealizadoModel> toModel(List<Pedido> pedidos){
        List<PedidoRealizadoModel> realizados = new ArrayList<>();
        for (Pedido p : pedidos) {
            PedidoRealizadoModel pt = PedidoRealizadoModel.toModel(p);
            pt.setRequisicaoCriar(null);
            realizados.add(pt);
        }

        return realizados;
    }

    static public PedidoRealizadoModel toModel(Pedido pedido){
        PedidoRealizadoModel tmp = new PedidoRealizadoModel();
        tmp.setRequisicaoCriar(PedidoNovoModel.toModel(pedido));
        tmp.setValorFrete(pedido.getValorFrete());
        tmp.setValorTotal(pedido.getValorTotal());
        tmp.setComerciante(pedido.getComprador());

        for(ItemCarrinho i : pedido.getItensCarrinho()){
            tmp.addItem(ItemCarrinhoModel.toModel(i));
            tmp.addDataEntrega(i.getAnuncio().getDatasEntregas().get(0));
        }
        return tmp;
    }


}
