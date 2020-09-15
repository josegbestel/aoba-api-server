package com.redeaoba.api.model;

import com.redeaoba.api.model.enums.DiaSemana;
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
    private String cnpj;

    @OneToMany(mappedBy = "comerciante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Endereco> enderecos;

    @OneToMany(mappedBy = "comprador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
}
