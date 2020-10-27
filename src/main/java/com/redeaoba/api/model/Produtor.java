package com.redeaoba.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redeaoba.api.model.enums.DiaSemana;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
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

    @ElementCollection(targetClass = DiaSemana.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="comerciante_dia_semana")
    @Column(name="dia_atendimento")
    private List<DiaSemana> diasAtendimento;

    @OneToMany(mappedBy = "produtor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Anuncio> anuncios;

    @OneToMany(mappedBy = "produtor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

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

    public List<DiaSemana> getDiasAtendimento() {
        return diasAtendimento;
    }

    public void setDiasAtendimento(List<DiaSemana> diasAtendimento) {
        this.diasAtendimento = diasAtendimento;
    }

    private List<Anuncio> getAnuncios() {
        return anuncios;
    }

    @JsonIgnore
    public List<Pedido> getPedidos(){
        List<Pedido> pedidos = new ArrayList<>();
        System.out.println("Qtde de anuncios: " + this.getAnuncios().size());
        for(Anuncio a : this.getAnuncios()){
            System.out.println("Anúncio " + a.getId());
            System.out.println(a.getProduto().getNome());
            for(ItemCarrinho i : a.getItensCarrinho()){
                pedidos.add(i.getPedido());
            }
        }
        return pedidos;
    }

    @JsonIgnore
    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    @JsonIgnore
    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public float getRating(){
        if(this.getAvaliacoes().size()>0){
            float notas = 0;
            float qtde = 0;

            for (Avaliacao a : this.getAvaliacoes()) {
                notas += a.getNota();
                qtde += 1;
            }

            return notas/qtde;
        }
        return -1;
    }
}
