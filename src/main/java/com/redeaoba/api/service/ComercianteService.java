package com.redeaoba.api.service;

import com.redeaoba.api.exception.AuthorizationException;
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
            throw new DomainException("Ja existe comerciante com esse email");
        else if(comercianteRepository.existsByCnpj(comerciante.getCnpj()))
            throw new DomainException("Ja existe comerciante com esse codigo de registro");
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
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));
    }

    //read by email
    public Comerciante readByEmail(String email){
        return comercianteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));
    }

    //read enderecos
    public List<Endereco> readEnderecos(Long id, String emailComerciante){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));

        //[autorização] Verificar se o id informado é do comerciante do login
        if(!comerciante.getEmail().equals(emailComerciante))
            throw new AuthorizationException();

        return comerciante.getEnderecos();
    }

    //update endereco
    public List<Endereco> createEndereco(Long id, Endereco endereco, String emailComerciante){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));

        //[autorização] Verificar se o id informado é do comerciante do login
        if(!comerciante.getEmail().equals(emailComerciante))
            throw new AuthorizationException();

        System.out.println("qtde endereços: " + comerciante.getEnderecos().size());
        comerciante.addEndereco(endereco);
        comerciante = comercianteRepository.save(comerciante);
        return comerciante.getEnderecos();
    }

    //delete endereco
    public void deleteEndereco(Long id, Long enderecoId, String emailComerciante){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));

        //[autorização] Verificar se o id informado é do comerciante do login
        if(!comerciante.getEmail().equals(emailComerciante))
            throw new AuthorizationException();

        if(comerciante.removerEndereco(enderecoId)){
            throw new NotFoundException("Endereco nao localizado");
        }

        comercianteRepository.save(comerciante);
    }


    //delete
    public void delete(Long id, String emailComerciante){
        Comerciante comerciante = comercianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));

        //[autorização] Verificar se o id informado é do comerciante do login
        if(!comerciante.getEmail().equals(emailComerciante))
            throw new AuthorizationException();

        //TODO: Removever infos pessoais e desativas NUNCA EXCLUIR
        comercianteRepository.deleteById(id);
    }
}
