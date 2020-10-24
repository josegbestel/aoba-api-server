package com.redeaoba.api.model.representationModel;

import com.redeaoba.api.model.Usuario;

import java.io.Serializable;

public class UsuarioPerfilModel implements Serializable {

    private String nome;
    private String nomeFantasia;
    private String emailAntigo;
    private String emailNovo;
    private String telefoneAntigo;
    private String telefoneNovo;
    private String senhaAntiga;
    private String senhaNova;

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

    public String getEmailAntigo() {
        return emailAntigo;
    }

    public void setEmailAntigo(String emailAntigo) {
        this.emailAntigo = emailAntigo;
    }

    public String getEmailNovo() {
        return emailNovo;
    }

    public void setEmailNovo(String emailNovo) {
        this.emailNovo = emailNovo;
    }

    public String getTelefoneAntigo() {
        return telefoneAntigo;
    }

    public void setTelefoneAntigo(String telefoneAntigo) {
        this.telefoneAntigo = telefoneAntigo;
    }

    public String getTelefoneNovo() {
        return telefoneNovo;
    }

    public void setTelefoneNovo(String telefoneNovo) {
        this.telefoneNovo = telefoneNovo;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }
}
