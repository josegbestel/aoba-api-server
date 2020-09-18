package com.redeaoba.api.model.representationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redeaoba.api.model.Comerciante;
import com.redeaoba.api.model.Endereco;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.enums.OpcaoAlternativa;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PedidoModel implements Serializable {

    @NotNull
    private long compradorId;

    @NotNull
    private long enderecoId;

    @NotNull
    private List<ItemCarrinhoLiteModel> itensCarrinho;

    @NotNull
    private float valorFrete;

    private OpcaoAlternativa opcaoAlternativa;

    public long getCompradorId() {
        return compradorId;
    }

    public void setCompradorId(long compradorId) {
        this.compradorId = compradorId;
    }

    public long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(long enderecoId) {
        this.enderecoId = enderecoId;
    }

    public List<ItemCarrinhoLiteModel> getItensCarrinho() {
        return itensCarrinho;
    }

    public void setItensCarrinho(List<ItemCarrinhoLiteModel> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
    }

    public float getValorFrete() {
        return valorFrete;
    }

    @JsonIgnore
    public void setValorFrete(float valorFrete) {
        this.valorFrete = valorFrete;
    }

    public OpcaoAlternativa getOpcaoAlternativa() {
        return opcaoAlternativa;
    }

    public void setOpcaoAlternativa(OpcaoAlternativa opcaoAlternativa) {
        this.opcaoAlternativa = opcaoAlternativa;
    }

    public Pedido byModel(Comerciante comerciante, Endereco endereco, List<ItemCarrinho> itens){
        Pedido pedido = new Pedido();
        pedido.setComprador(comerciante);
        pedido.setEndereco(endereco);
        pedido.setOpcaoAlternativa(this.opcaoAlternativa);
        pedido.setValorFrete(this.valorFrete);
        pedido.setItensCarrinho(itens);

        return pedido;
    }

    static public PedidoModel toModel(Pedido pedido){
        PedidoModel model = new PedidoModel();
        model.setCompradorId(pedido.getComprador().getId());
        model.setEnderecoId(pedido.getEndereco().getId());
        model.setOpcaoAlternativa(pedido.getOpcaoAlternativa());
        model.setValorFrete(pedido.getValorFrete());

        List<ItemCarrinhoLiteModel> itensModel = new ArrayList<>();
        pedido.getItensCarrinho().forEach(i -> itensModel.add(ItemCarrinhoLiteModel.byModel(ItemCarrinhoModel.toModel(i))));
        model.setItensCarrinho(itensModel);

        return model;
    }
}
