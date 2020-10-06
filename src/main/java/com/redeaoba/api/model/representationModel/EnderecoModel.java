package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Endereco;

public class EnderecoModel {

    private String logradouroNumero;
    private String bairro;
    private String cidadeUf;

    static EnderecoModel toModel(Endereco endereco){
        EnderecoModel model = new EnderecoModel();
        model.setLogradouroNumero(endereco.getLogradouro() + ", " + endereco.getNumero());
        model.setCidadeUf(endereco.getCidade() + "-" + endereco.getUf());
        model.setBairro(endereco.getBairro());

        return model;
    }

    public String getLogradouroNumero() {
        return logradouroNumero;
    }

    public void setLogradouroNumero(String logradouroNumero) {
        this.logradouroNumero = logradouroNumero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidadeUf() {
        return cidadeUf;
    }

    public void setCidadeUf(String cidadeUf) {
        this.cidadeUf = cidadeUf;
    }
}
