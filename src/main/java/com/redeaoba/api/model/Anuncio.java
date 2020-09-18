package com.redeaoba.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redeaoba.api.model.enums.DiaSemana;
import com.redeaoba.api.model.representationModel.AnuncioModel;
import com.redeaoba.api.util.StringListConverter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "anuncio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Anuncio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produtor_id", nullable = false)
    private Produtor produtor;

    @NotNull
    private LocalDateTime dtCriacao;

    @NotNull
    private LocalDateTime dtValidade;

    @NotNull
    private boolean ativo;

    @NotNull
    private Float valor;

    @NotNull
    private Integer qtdeMax;

    @Convert(converter = StringListConverter.class)
    private List<String> fotos;

    @OneToMany(mappedBy = "anuncio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemCarrinho> itensCarrinho;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Produtor getProdutor() {
        return produtor;
    }

    public void setProdutor(Produtor produtor) {
        this.produtor = produtor;
    }

    public LocalDateTime getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public LocalDateTime getDtValidade() {
        return dtValidade;
    }

    public void setDtValidade(LocalDateTime dtValidade) {
        this.dtValidade = dtValidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Integer getQtdeMax() {
        return qtdeMax;
    }

    public void setQtdeMax(Integer qtdeMax) {
        this.qtdeMax = qtdeMax;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public boolean isValido() {
        return this.dtValidade.isAfter(LocalDateTime.now());
    }

    public static Anuncio byModel(AnuncioModel model, Produto produto, Produtor produtor){
        Anuncio anuncio = new Anuncio();
        anuncio.setDtCriacao(model.getDtCriacao());
        anuncio.setDtValidade(model.getDtValidade());
        anuncio.setAtivo(model.isAtivo());
        anuncio.setValor(model.getValor());
        anuncio.setQtdeMax(model.getQtdeMax());
        anuncio.setProduto(produto);
        anuncio.setProdutor(produtor);
        anuncio.setFotos(model.getFotos());

        return anuncio;
    }

    public List<DiaSemana> getDiasDisponiveis(){
        return getProdutor().getDiasAtendimento();
    }

    public List<DataEntrega> getDatasEntregas(){
        List<DataEntrega> entregas = new ArrayList<>();
        List<DiaSemana> diasProdutor = this.getProdutor().getDiasAtendimento();

        //Pegar dia referencia
        LocalDate today = LocalDate.now();
        if(LocalDateTime.now().getHour() < 20)
            today = today.plusDays(1);
        else
            today = today.plusDays(2);

        DiaSemana todayWeek = DiaSemana.getByDayOfWeek(today.getDayOfWeek().getValue());

        for(DiaSemana d : diasProdutor){
            //Ver a diferença entre hoje e dia do produtor
            int diff = DiaSemana.daysBetween(todayWeek, d);

            //adicionar essa qtde de dias
            LocalDate dt = LocalDate.now();
            if(diff < 0){
                //Proxima semana
                dt = dt.plusDays(7-Math.abs(diff));
            }else {
                //Esta semana
                dt = dt.plusDays(diff);
            }
            entregas.add(DataEntrega.toLocalDate(dt, d));

//            System.out.println("diferenca entre " + todayWeek + " e " + d + " = " + diff);
//            System.out.println("Dias a adicionar: " + (diff < 0 ? 7-Math.abs(diff) : diff));
//            System.out.println("Dia entrega: " + d + " (" + dt + ")");
        }

        //Orderna entregas
        Collections.sort(entregas);

        return entregas;
    }

}
