package com.redeaoba.api.exception;

public class AuthorizationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AuthorizationException(){
        super("Este usuário não está autorizado a realizar essa operação");
    }
}
