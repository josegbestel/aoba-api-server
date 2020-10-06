package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoRealizadoModel {

    private long pedidoId;
    private String nomeFantasiaComerciante;
    private LocalDateTime dtRealizado;
    private StatusPedido status;
    private LocalDateTime dtConfirmado;
    private LocalDateTime prazoEntrega;
    private String telefoneComerciante;
    private DetalhesPedidoRealizadoModel detalhes;

    public static PedidoRealizadoModel toModel(Pedido pedido){
        PedidoRealizadoModel model = new PedidoRealizadoModel();
        model.setPedidoId(pedido.getId());
        model.setNomeFantasiaComerciante(pedido.getComprador().getNomeFantasia());
        model.setDtRealizado(pedido.getDtCriacao());
        model.setStatus(pedido.getStatus());
        model.setDtConfirmado(pedido.getDtConfirmado());
        model.setPrazoEntrega(pedido.getPrazoEntrega());
        model.setTelefoneComerciante(pedido.getComprador().getTelefone());
        model.setDetalhes(DetalhesPedidoRealizadoModel.toModel(pedido));

        return model;
    }

    public static List<PedidoRealizadoModel> toModel(List<Pedido> pedidos){
        List<PedidoRealizadoModel> models = new ArrayList<>();
        pedidos.forEach(p -> models.add(PedidoRealizadoModel.toModel(p)));

        return models;
    }


    public long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getNomeFantasiaComerciante() {
        return nomeFantasiaComerciante;
    }

    public void setNomeFantasiaComerciante(String nomeFantasiaComerciante) {
        this.nomeFantasiaComerciante = nomeFantasiaComerciante;
    }

    public LocalDateTime getDtRealizado() {
        return dtRealizado;
    }

    public void setDtRealizado(LocalDateTime dtRealizado) {
        this.dtRealizado = dtRealizado;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
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

    public String getTelefoneComerciante() {
        return telefoneComerciante;
    }

    public void setTelefoneComerciante(String telefoneComerciante) {
        this.telefoneComerciante = telefoneComerciante;
    }

    public DetalhesPedidoRealizadoModel getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(DetalhesPedidoRealizadoModel detalhes) {
        this.detalhes = detalhes;
    }


}
