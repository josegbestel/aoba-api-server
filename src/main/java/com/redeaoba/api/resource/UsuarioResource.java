package com.redeaoba.api.resource;

import com.redeaoba.api.model.Usuario;
import com.redeaoba.api.model.representationModel.UsuarioPerfilModel;
import com.redeaoba.api.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/usuario")
@Api
@CrossOrigin(origins = "*")
public class UsuarioResource {

    @Autowired
    UsuarioService usuarioService;

    @PutMapping("/{id}/editar-perfil")
    @ApiOperation("Edita o perfil do profissional. Atenção: Enviar apenas as \"duplas\" de cada campo")
    public ResponseEntity<Usuario> alterarPerfil(@PathVariable(value = "id") long id,
                                                 @Valid @RequestBody UsuarioPerfilModel perfilModel,
                                                 @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(usuarioService.edit(id, perfilModel, userDetails.getUsername()));
    }
}
