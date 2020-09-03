package com.redeaoba.api.resource;

import com.redeaoba.api.model.Comerciante;
import com.redeaoba.api.model.representationModel.loginModel;
import com.redeaoba.api.service.ComercianteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/comerciante")
@Api
@CrossOrigin(origins = "*")
public class ComercianteResource {

    @Autowired
    ComercianteService comercianteService;

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
                                              @Valid @RequestBody loginModel login){
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
