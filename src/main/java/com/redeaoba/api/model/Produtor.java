package com.redeaoba.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "produtor")

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produtor extends Usuario implements Serializable {

    @Column(unique = true)
    private String codigoRegistro;

    @NotNull
    @NotBlank
    private String descricao;

    @OneToMany(mappedBy = "produtor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Anuncio> anuncios;

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
