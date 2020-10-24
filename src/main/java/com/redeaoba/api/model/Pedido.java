package com.redeaoba.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redeaoba.api.model.enums.OpcaoAlternativa;
import com.redeaoba.api.model.enums.StatusItem;
import com.redeaoba.api.model.enums.StatusPedido;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    private List<ItemCarrinho> itensCarrinho;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comprador_id", nullable = false)
    private Comerciante comprador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    private float valorFrete;

    private LocalDateTime dtCriacao;
    private LocalDateTime prazoResposta;
    private LocalDateTime dtConfirmado;
    private LocalDateTime prazoEntrega;
    private LocalDateTime dtEntrega;

    @Enumerated(EnumType.STRING)
    private OpcaoAlternativa opcaoAlternativa;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ItemCarrinho> getItensCarrinho() {
        return itensCarrinho;
    }

    @JsonIgnore
    public List<ItemCarrinho> getItensPendentes(){
        List<ItemCarrinho> pendentes = new ArrayList<>();

        for(ItemCarrinho i : this.getItensCarrinho()){
            if(i.getStatus() == StatusItem.PENDENTE)
                pendentes.add(i);
        }

        return pendentes;
    }


    @JsonIgnore
    public List<ItemCarrinho> getItensCarrinhoAtivos(){
        List<ItemCarrinho> ativos = new ArrayList<>();

        for (ItemCarrinho i : itensCarrinho) {
            if(i.getStatus() != StatusItem.CANCELADO && i.getStatus() != StatusItem.REMOVIDO)
                ativos.add(i);
        }

        return ativos;
    }

    public void setItensCarrinho(List<ItemCarrinho> itensCarrinho) {
        itensCarrinho.forEach(i -> i.setPedido(this));
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

    @JsonIgnore
    public boolean buscarOpcaoAlternativa(){
        if (this.opcaoAlternativa == OpcaoAlternativa.ACEITAR_SUGESTAO)
            return true;
        return false;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
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

    public LocalDateTime getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public LocalDateTime getPrazoResposta() {
        return this.prazoResposta;
    }

    public void setPrazoResposta(LocalDateTime prazoResposta) {
        this.prazoResposta = prazoResposta;
    }

    public LocalDateTime getDtConfirmado() {
        return dtConfirmado;
    }

    public void setDtConfirmado(LocalDateTime dtConfirmado) {
        this.dtConfirmado = dtConfirmado;
    }

    public LocalDateTime getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(LocalDateTime prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public LocalDateTime getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(LocalDateTime dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public List<ItemCarrinho> getItensCarrinhoByProdutorId(long produtorId){
        List<ItemCarrinho> itensProdutor = new ArrayList<>();
        for (ItemCarrinho i : this.getItensCarrinho()){
            if(i.getAnuncio().getProdutor().getId() == produtorId){
                itensProdutor.add(i);
            }
        }
        return itensProdutor;
    }

    public void refreshStatus(){
        int total = this.getItensCarrinho().size();
        int cancelados = 0;
        int confirmados = 0;
        int pendentes = 0;
        int removidos = 0;

        for(ItemCarrinho i : this.getItensCarrinho()){
            if(i.getStatus() == StatusItem.CANCELADO)
                cancelados += 1;
            else if(i.getStatus() == StatusItem.CONFIRMADO)
                confirmados += 1;
            else if(i.getStatus() == StatusItem.PENDENTE)
                pendentes += 1;
            else if(i.getStatus() == StatusItem.REMOVIDO)
                removidos += 1;
        }

        if(cancelados == total)
            this.status = StatusPedido.CANCELADO;
        else if(confirmados == (total-removidos-cancelados)){
            this.status = StatusPedido.CONFIRMADO;
            this.dtConfirmado = LocalDateTime.now();
        }
        else if(pendentes > 0)
            this.status = StatusPedido.PENDENTE;
    }

    public void rejectItensByProdutorId(long produtorId){
        for (ItemCarrinho i : this.getItensCarrinho()){
            if(i.getAnuncio().getProdutor().getId() == produtorId){
                i.setDtResposta(LocalDateTime.now());
            }
        }
        refreshStatus();
    }

    public void confirmItensByProdutorId(long produtorId){
        for (ItemCarrinho i : this.getItensCarrinho()){
            if(i.getAnuncio().getProdutor().getId() == produtorId){
                i.setStatus(StatusItem.CONFIRMADO);
                i.setDtResposta(LocalDateTime.now());
            }
        }
        refreshStatus();
    }

    public void cancelItensByProdutorId(long produtorId){
        for (ItemCarrinho i : this.getItensCarrinho()){
            if(i.getAnuncio().getProdutor().getId() == produtorId){
                i.setStatus(StatusItem.CANCELADO);
                i.setDtResposta(LocalDateTime.now());
            }
        }
        refreshStatus();
    }
}
