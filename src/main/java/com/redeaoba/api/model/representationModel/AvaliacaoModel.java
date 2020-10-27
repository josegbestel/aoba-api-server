package com.redeaoba.api.model.representationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AvaliacaoModel implements Serializable {

    @NotNull
    private int nota;

    @NotBlank
    private String comentario;

    private long produtorId;

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

    public long getProdutorId() {
        return produtorId;
    }

    public void setProdutorId(long produtorId) {
        this.produtorId = produtorId;
    }
}
