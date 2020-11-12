package com.redeaoba.api.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> handleAuthorization(AuthorizationException ex, WebRequest request){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Problema problema = new Problema();
        problema.setStatus(status.value());
        problema.setTitulo(ex.getMessage());
        problema.setDataHora(LocalDateTime.now());

        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> handleDomain(DomainException ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Problema problema = new Problema();
        problema.setStatus(status.value());
        problema.setTitulo(ex.getMessage());
        problema.setDataHora(LocalDateTime.now());

        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundDomain(DomainException ex, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problema problema = new Problema();
        problema.setStatus(status.value());
        problema.setTitulo(ex.getMessage());
        problema.setDataHora(LocalDateTime.now());

        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Problema.Campo> campos = new ArrayList<Problema.Campo>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()){
            String msg = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            String nome = ((FieldError) error).getField();

            campos.add(new Problema.Campo(nome, msg));
        }

        Problema problema = new Problema();
        problema.setStatus(status.value());
        problema.setTitulo("Um ou mais campos estão inválidos");
        problema.setDataHora(LocalDateTime.now());
        problema.setCampos(campos);

        return super.handleExceptionInternal(ex, problema, headers, status, request);
    }
}
