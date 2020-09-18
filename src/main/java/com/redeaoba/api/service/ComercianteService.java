package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.Comerciante;
import com.redeaoba.api.model.Endereco;
import com.redeaoba.api.model.enums.AuthType;
import com.redeaoba.api.model.representationModel.LoginModel;
import com.redeaoba.api.repository.ComercianteRepository;
import com.redeaoba.api.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Service
public class ComercianteService {

    @Autowired
    ComercianteRepository comercianteRepository;

    private void checkDuplicity(Comerciante comerciante){
        if(comercianteRepository.existsByEmail(comerciante.getEmail()))
            throw new DomainException("Já existe comerciante com esse email");
        else if(comercianteRepository.existsByCnpj(comerciante.getCnpj()))
            throw new DomainException("Já existe comerciante com esse código de registro");
    }

    //create
    public Comerciante create(Comerciante comerciante){
        checkDuplicity(comerciante);

        comerciante.setSenha(PasswordEncoder.encode(comerciante.getSenha()));
        comerciante.setAuthType(AuthType.USER);
        return comercianteRepository.save(comerciante);
    }

    //read
    public Comerciante read(Long id) {
        return comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
    }

    //read by email
    public Comerciante readByEmail(String email){
        return comercianteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
    }

    //read enderecos
    public List<Endereco> readEnderecos(Long id){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));

        return comerciante.getEnderecos();
    }


    //update password
    public void updatePassword(Long id, LoginModel loginModel){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
        comerciante.updateSenha(loginModel);
        comercianteRepository.save(comerciante);
    }

    //update endereco
    public List<Endereco> createEndereco(Long id, Endereco endereco){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
        comerciante.addEndereco(endereco);
        comerciante = comercianteRepository.save(comerciante);
        return comerciante.getEnderecos();
    }

    //delete endereco
    public void deleteEndereco(Long id, Long enderecoId){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
        if(comerciante.removerEndereco(enderecoId)){
            throw new NotFoundException("Endereço não localizado");
        }

        comercianteRepository.save(comerciante);
    }


    //delete
    public void delete(Long id){
        if(comercianteRepository.existsById(id))
            comercianteRepository.deleteById(id);
        else
            throw new NotFoundException("Comerciante não localizado");
    }
}
