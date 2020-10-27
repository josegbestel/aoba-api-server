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
//        if(perfilModel.getNome() != null && perfilModel.getNomeFantasia() != null
//                && perfilModel.getEmailAntigo() != null && perfilModel.getEmailNovo() != null
//                && perfilModel.getTelefoneAntigo() != null && perfilModel.getTelefoneNovo() != null
//                && perfilModel.getSenhaAntiga() != null && perfilModel.getSenhaNova() != null){
//            throw new DomainException("Todos os campos enviados estão nulo");
//        }

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
