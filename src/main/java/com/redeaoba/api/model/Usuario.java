package com.redeaoba.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redeaoba.api.model.enums.AuthType;
import com.redeaoba.api.model.representationModel.LoginModel;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    @NotBlank
    private String nome;

    @NotBlank
    private String nomeFantasia;

    private String foto;

    @NotBlank
    private String telefone;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private AuthType authType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty(value = "senha")
    @JsonIgnore
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTelefone() {
        return telefone
                .replace("(", "")
                .replace("-", "")
                .replace(" ", "")
                .replace(".", "");
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone
                .replace("(", "")
                .replace("-", "")
                .replace(" ", "")
                .replace(".", "");
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public void updateSenha(LoginModel loginModel) {
        if(this.getSenha().equals(loginModel.getSenhaAntiga())){
            this.setSenha(loginModel.getSenhaNova());
        }
    }

    //TODO: Implementar metodo que faça a média das avaliações
    public float getRating(){
        return -1;
    }
}
