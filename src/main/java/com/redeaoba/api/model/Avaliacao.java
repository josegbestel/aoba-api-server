package com.redeaoba.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redeaoba.api.model.representationModel.AvaliacaoModel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "avaliacao")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Avaliacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int nota;
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comerciante_id", nullable = false)
    private Comerciante comerciante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produtor_id", nullable = false)
    private Produtor produtor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    public Avaliacao() {
    }

    public Avaliacao(Pedido pedido, Produtor produtor, AvaliacaoModel avaliacaoModel) {
        this.nota = avaliacaoModel.getNota();
        this.comentario = avaliacaoModel.getComentario();
        this.produtor = produtor;
        this.comerciante = pedido.getComprador();
        this.pedido = pedido;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Comerciante getComerciante() {
        return comerciante;
    }

    public void setComerciante(Comerciante comerciante) {
        this.comerciante = comerciante;
    }

    public Produtor getProdutor() {
        return produtor;
    }

    public void setProdutor(Produtor produtor) {
        this.produtor = produtor;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
