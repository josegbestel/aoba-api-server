package com.redeaoba.api.model;

import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "comerciante")
public class Comerciante extends Usuario implements Serializable {

    @CNPJ
    @NotBlank
    @NotNull
    private String cnpj;

    @OneToMany(mappedBy = "comprador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
