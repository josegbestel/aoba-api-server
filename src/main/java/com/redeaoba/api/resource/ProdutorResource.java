package com.redeaoba.api.resource;

import com.redeaoba.api.model.Produtor;
import com.redeaoba.api.model.representationModel.LoginModel;
import com.redeaoba.api.service.ProdutorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/produtor")
@Api
@CrossOrigin(origins = "*")
public class ProdutorResource {

    @Autowired
    ProdutorService produtorService;

    //LOGIN
    @GetMapping("/login")
    @ApiOperation("Metodo para realizar o login e obter as informações do profissional")
    public ResponseEntity<Produtor> login(@AuthenticationPrincipal UserDetails userDetails){
        System.out.println(userDetails.getUsername() + " : " + userDetails.getPassword());
        return ResponseEntity.ok(produtorService.readByEmail(userDetails.getUsername()));
    }


    //CADASTRAR NOVO PRODUTOR
    @PostMapping
    @ApiOperation("Cadastrar novo Produtor. Não inserir o ID")
    public ResponseEntity<Produtor> cadastrarProdutor(@Valid @RequestBody Produtor produtor){
        return ResponseEntity.ok(produtorService.create(produtor));
    }

    //OBTER PRODUTOR POR ID
    @GetMapping("/{id}")
    @ApiOperation("Obter produtor por ID")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Produtor> obterProdutorId(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(produtorService.read(id));
    }

    //DELETER
    @DeleteMapping("/{id}")
    @ApiOperation("Deleta um produtor")
    public ResponseEntity<Object> deletar(@PathVariable(value = "id")Long id){
        produtorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
