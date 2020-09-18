package com.redeaoba.api.model;

import com.redeaoba.api.model.enums.OpcaoAlternativa;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    private List<ItemCarrinho> itensCarrinho;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comprador_id", nullable = false)
    private Comerciante comprador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    private float valorFrete;

    @Enumerated(EnumType.STRING)
    private OpcaoAlternativa opcaoAlternativa;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ItemCarrinho> getItensCarrinho() {
        return itensCarrinho;
    }

    public void setItensCarrinho(List<ItemCarrinho> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
    }

    public Comerciante getComprador() {
        return comprador;
    }

    public void setComprador(Comerciante comprador) {
        this.comprador = comprador;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
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

    public float getValorTotal() {
        float vlrTotal = 0;
        for(ItemCarrinho i : this.getItensCarrinho()){
            float vlrUnitario = i.getAnuncio().getValor();
            float qtde = i.getQuantidade();
            vlrTotal += (vlrUnitario * qtde);
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        String number = df.format(vlrTotal);
        vlrTotal = Float.parseFloat(number.replace(",", "."));

        return vlrTotal;
    }
}
