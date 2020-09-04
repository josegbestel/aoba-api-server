package com.redeaoba.api.resource;

import com.redeaoba.api.model.Comerciante;
import com.redeaoba.api.model.representationModel.LoginModel;
import com.redeaoba.api.service.ComercianteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/comerciante")
@Api
@CrossOrigin(origins = "*")
public class ComercianteResource {

    @Autowired
    ComercianteService comercianteService;

    //LOGIN
    @GetMapping("/login")
    @ApiOperation("Metodo para realizar o login e obter as informações do profissional")
    public ResponseEntity<Comerciante> login(@AuthenticationPrincipal UserDetails userDetails){
        System.out.println(userDetails.getUsername() + " : " + userDetails.getPassword());
        return ResponseEntity.ok(comercianteService.readByEmail(userDetails.getUsername()));
    }

    //CADASTRAR NOVO COMERCIANTE
    @PostMapping
    @ApiOperation("Cadastrar comerciante. Não inserir o ID")
    public ResponseEntity<Comerciante> cadastrarComerciante(@Valid @RequestBody Comerciante comerciante){
        return ResponseEntity.ok(comercianteService.create(comerciante));
    }

    //OBTER POR ID
    @GetMapping("/{id}")
    @ApiOperation("Obter comerciante por ID")
    public ResponseEntity<Comerciante> obterComercianteId(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(comercianteService.read(id));
    }

    //EDITAR SENHA
    @PutMapping("/{id}/editar-senha")
    @ApiOperation("Editar a senha")
    public ResponseEntity<Object> editarSenha(@PathVariable(value = "id")Long id,
                                              @Valid @RequestBody LoginModel login){
        comercianteService.updatePassword(id, login);
        return ResponseEntity.noContent().build();
    }

    //DELETER
    @DeleteMapping("/{id}")
    @ApiOperation("Deleta um comerciante")
    public ResponseEntity<Object> deletar(@PathVariable(value = "id")Long id){
        comercianteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
