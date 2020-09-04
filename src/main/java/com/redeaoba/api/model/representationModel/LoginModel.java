package com.redeaoba.api.model.representationModel;

import java.io.Serializable;

public class LoginModel implements Serializable {

    private String senhaAntiga;
    private String senhaNova;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
