package com.redeaoba.api.service;

import com.redeaoba.api.exception.AuthorizationException;
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
    public Usuario edit(long idUsuario, UsuarioPerfilModel perfilModel, String emailUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario nao localizado"));

        //[autenticação] Se o idUsuário é o mesmo do login
        if(!usuario.getEmail().equals(emailUsuario))
            throw new AuthorizationException();

        //Alterar senha
        //Se apenas 1 estiver preenchido, lançar erro
        if(perfilModel.getSenhaAntiga() != null && perfilModel.getSenhaNova() != null){
            System.out.println("Alterar senha");
            usuario.updateSenha(perfilModel);
        }

        //Alterar telefone
        if(perfilModel.getTelefoneAntigo() != null && perfilModel.getTelefoneNovo() != null){
            System.out.println("Alterar telefone");
            usuario.updateTelefone(perfilModel);
        }

        //Alterar email
        if(perfilModel.getEmailAntigo() != null && perfilModel.getEmailNovo() != null){
            System.out.println("Alterar email");
            usuario.updateEmail(perfilModel);
        }

        //Alterar nomes
        usuario.updateNomes(perfilModel);

        return usuarioRepository.save(usuario);
    }
}
