package com.redeaoba.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redeaoba.api.model.enums.StatusItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_carrinho")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemCarrinho implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private Anuncio anuncio;

    @NotNull
    private int quantidade;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusItem status;

    private LocalDateTime dtResposta;
    private LocalDateTime dtPrazoResposta;
    private int interacoesRespostas;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pedido pedido;

    public float getValorTotal(){
        float vlrTotal = anuncio.getValor() * this.quantidade;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        String number = df.format(vlrTotal);
        vlrTotal = Float.parseFloat(number.replace(",", "."));

        return vlrTotal;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public StatusItem getStatus() {
        return status;
    }

    public void setStatus(StatusItem status) {
        this.status = status;
    }

    public LocalDateTime getDtResposta() {
        return dtResposta;
    }

    public void setDtResposta(LocalDateTime dtResposta) {
        this.dtResposta = dtResposta;
    }

    public LocalDateTime getDtPrazoResposta() {
        return dtPrazoResposta;
    }

    public void setDtPrazoResposta(LocalDateTime dtPrazoResposta) {
        this.dtPrazoResposta = dtPrazoResposta;
    }

    public int getInteracoesRespostas() {
        return interacoesRespostas;
    }

    public void setInteracoesRespostas(int interacoesRespostas) {
        this.interacoesRespostas = interacoesRespostas;
    }

    @JsonIgnore
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
