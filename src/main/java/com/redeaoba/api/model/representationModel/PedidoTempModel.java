package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.DataEntrega;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.Produtor;

import java.util.ArrayList;
import java.util.List;

public class PedidoTempModel {

    private float valorFrete;
    private float valorTotal;
    private List<ItemCarrinhoModel> itens = new ArrayList<>();
    private List<DataEntrega> datasEntrega = new ArrayList<>();
    private List<Produtor> produtores = new ArrayList<>();
    private PedidoModel requisicaoCriar;

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

    public List<Produtor> getProdutores() {
        return produtores;
    }

    public void setProdutores(List<Produtor> produtores) {
        this.produtores = produtores;
    }

    public void addProdutor(Produtor produtor){
        this.produtores.add(produtor);
    }

    public PedidoModel getRequisicaoCriar() {
        PedidoModel requisicao = this.requisicaoCriar;
        return requisicaoCriar;
    }

    public void setRequisicaoCriar(PedidoModel requisicaoCriar) {
        this.requisicaoCriar = requisicaoCriar;
    }

    static public PedidoTempModel toModel(Pedido pedido){
        PedidoTempModel tmp = new PedidoTempModel();
        tmp.setRequisicaoCriar(PedidoModel.toModel(pedido));
        tmp.setValorFrete(pedido.getValorFrete());
        tmp.setValorTotal(pedido.getValorTotal());

        for(ItemCarrinho i : pedido.getItensCarrinho()){
            tmp.addProdutor(i.getAnuncio().getProdutor());
            tmp.addItem(ItemCarrinhoModel.toModel(i));
            tmp.addDataEntrega(i.getAnuncio().getDatasEntregas().get(0));
        }
        return tmp;
    }

}
