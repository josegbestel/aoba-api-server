package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.Produto;
import com.redeaoba.api.model.Produtor;
import com.redeaoba.api.model.enums.AuthType;
import com.redeaoba.api.model.representationModel.loginModel;
import com.redeaoba.api.repository.ProdutorRepository;
import com.redeaoba.api.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutorService {

    @Autowired
    ProdutorRepository produtorRepository;

    private void checkDuplicity(Produtor produtor){
        if(produtorRepository.existsByEmail(produtor.getEmail()))
            throw new DomainException("Já existe produtor com esse email");
        else if(produtorRepository.existsByCodigoRegistro(produtor.getCodigoRegistro()))
            throw new DomainException("Já existe produtor com esse código de registro");
    }


    //create
    public Produtor create(Produtor produtor){
        checkDuplicity(produtor);

        produtor.setSenha(PasswordEncoder.encode(produtor.getSenha()));
        produtor.setAuthType(AuthType.USER);
        return produtorRepository.save(produtor);
    }

    //read
    public Produtor read(Long id) {
        return produtorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produtor não localizado"));
    }


    //update password
    public void updatePassword(Long id, loginModel loginModel){
        Produtor produtor = produtorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produtor não localizado"));
        produtor.updateSenha(loginModel);
        produtorRepository.save(produtor);
    }


    //delete
    public void delete(Long id){
        if(produtorRepository.existsById(id))
            produtorRepository.deleteById(id);
        else
            throw new NotFoundException("Produtor não localizado");
    }
}
