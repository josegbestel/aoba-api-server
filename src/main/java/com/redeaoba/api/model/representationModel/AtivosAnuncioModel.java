package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.DataEntrega;
import com.redeaoba.api.model.Produtor;
import com.redeaoba.api.model.enums.DiaSemana;
import com.redeaoba.api.util.StringListConverter;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.List;

public class AtivosAnuncioModel implements Serializable {

    private Long id;
    private float valor;
    private int qtdeMax;
    private List<DiaSemana> diasDisponiveis;
    private List<DataEntrega> datasEntregas;

    private Produtor produtor;

    @Convert(converter = StringListConverter.class)
    private List<String> fotos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getQtdeMax() {
        return qtdeMax;
    }

    public void setQtdeMax(int qtdeMax) {
        this.qtdeMax = qtdeMax;
    }

    public Produtor getProdutor() {
        return produtor;
    }

    public void setProdutor(Produtor produtor) {
        this.produtor = produtor;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public List<DiaSemana> getDiasDisponiveis() {
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(List<DiaSemana> diasDisponiveis) {
        this.diasDisponiveis = diasDisponiveis;
    }

    public List<DataEntrega> getDatasEntregas() {
        return datasEntregas;
    }

    public void setDatasEntregas(List<DataEntrega> datasEntregas) {
        this.datasEntregas = datasEntregas;
    }

    static public AtivosAnuncioModel toModel(Anuncio anuncio){
        AtivosAnuncioModel ativosAnuncioModel = new AtivosAnuncioModel();
        ativosAnuncioModel.setId(anuncio.getId());
        ativosAnuncioModel.setValor(anuncio.getValor());
        ativosAnuncioModel.setQtdeMax(anuncio.getQtdeMax());
        ativosAnuncioModel.setProdutor(anuncio.getProdutor());
        ativosAnuncioModel.setFotos(anuncio.getFotos());
        ativosAnuncioModel.setDatasEntregas(anuncio.getDatasEntregas());
        ativosAnuncioModel.setDiasDisponiveis(anuncio.getDiasDisponiveis());

        return ativosAnuncioModel;
    }
}
