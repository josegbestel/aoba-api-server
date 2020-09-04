package com.redeaoba.api.model.representationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redeaoba.api.model.Produto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoModel implements Serializable {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private Long Id;

    @NotBlank
    private String nome;

    @NotBlank
    private String foto;

    @NotNull
    private long categoriaId;

    private String categoriaNome;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public static ProdutoModel toModel(Produto produto){
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(produto.getId());
        produtoModel.setNome(produto.getNome());
        produtoModel.setFoto(produto.getFoto());
        produtoModel.setCategoriaId(produto.getCategoria().getId());
        produtoModel.setCategoriaNome(produto.getCategoria().getNome());

        return produtoModel;
    }

}
