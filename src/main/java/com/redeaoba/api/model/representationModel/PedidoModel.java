package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Comerciante;
import com.redeaoba.api.model.Endereco;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.enums.OpcaoAlternativa;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class PedidoModel implements Serializable {

    @NotNull
    private long compradorId;

    @NotNull
    private long enderecoId;

    @NotNull
    private List<ItemCarrinhoModel> itensCarrinho;

    @NotNull
    private float valorFrete;

    @NotNull
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

    public List<ItemCarrinhoModel> getItensCarrinho() {
        return itensCarrinho;
    }

    public void setItensCarrinho(List<ItemCarrinhoModel> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
    }

    public float getValorFrete() {
        return valorFrete;
    }

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
}
