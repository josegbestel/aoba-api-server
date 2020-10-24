package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.Usuario;
import com.redeaoba.api.model.representationModel.UsuarioPerfilModel;
import com.redeaoba.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    //Editar perfil
    public Usuario edit(long idUsuario, UsuarioPerfilModel perfilModel){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario nao localizado"));

        //Validar se todos os campos estão null
        if(perfilModel.getNome().isEmpty() && perfilModel.getNomeFantasia().isEmpty()
                && perfilModel.getEmailAntigo().isEmpty() && perfilModel.getEmailNovo().isEmpty()
                && perfilModel.getTelefoneAntigo().isEmpty() && perfilModel.getTelefoneNovo().isEmpty()
                && perfilModel.getSenhaAntiga().isEmpty() && perfilModel.getSenhaNova().isEmpty()){
            throw new DomainException("Todos os campos enviados estão nulo");
        }

        //Alterar senha
        //Se apenas 1 estiver preenchido, lançar erro
        if(!perfilModel.getSenhaAntiga().isEmpty() && !perfilModel.getSenhaNova().isEmpty()){
            usuario.updateSenha(perfilModel);
        }else{
            throw new DomainException("Os dois campos ref a senha devem estar preenchidos");
        }

        //Alterar telefone
        if(!perfilModel.getTelefoneAntigo().isEmpty() && !perfilModel.getTelefoneNovo().isEmpty()){
            usuario.updateTelefone(perfilModel);
        }else{
            throw new DomainException("Os dois campos ref ao telefone devem estar preenchidos");
        }

        //Alterar email
        if(!perfilModel.getEmailAntigo().isEmpty() && !perfilModel.getEmailNovo().isEmpty()){
            usuario.updateEmail(perfilModel);
        }else{
            throw new DomainException("Os dois campos ref ao email devem estar preenchidos");
        }

        //Alterar nomes
        usuario.updateNomes(perfilModel);

        return usuarioRepository.save(usuario);
    }
}
