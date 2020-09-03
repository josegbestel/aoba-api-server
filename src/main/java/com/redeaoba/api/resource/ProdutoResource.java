package com.redeaoba.api.resource;

import com.redeaoba.api.model.Produto;
import com.redeaoba.api.model.representationModel.ProdutoModel;
import com.redeaoba.api.service.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/produto")
@Api
@CrossOrigin(origins = "*")
public class ProdutoResource {

    @Autowired
    ProdutoService produtoService;

    //CADASTRAR
    @PostMapping
    @ApiOperation("Cadastra um produto. Não inserir o ID")
    public ResponseEntity<Produto> cadastrar(@Valid @RequestBody ProdutoModel produto){
        return ResponseEntity.ok(produtoService.create(produto));
    }

    //CADASTRAR MASSIVAMENTE
    @PostMapping("/massivo")
    public ResponseEntity<List<Produto>> cadastarMassivamente(@RequestBody List<@Valid ProdutoModel> produtos){
        return ResponseEntity.ok(produtoService.create(produtos));
    }

    //LER POR ID
    @GetMapping("/{id}")
    @ApiOperation("Obter produto por ID")
    public ResponseEntity<Produto> getById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(produtoService.read(id));
    }

    //LER POR CATEGORIA
    @GetMapping("/categoria/{id}")
    @ApiOperation("Obtem uma lista de produtos a partir do ID de uma categoria. Ex: maça")
    public ResponseEntity<List<ProdutoModel>> getByCategoria(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(produtoService.readByCategoria(id));
    }

    //LER POR SEÇÃO
    @GetMapping("secao/{secao}")
    @ApiOperation("Obtem uma lista de produtos a partir de uma seção (fruta,verdura ou legume")
    public ResponseEntity<List<ProdutoModel>> getByCategoria(@PathVariable(value = "secao") String secao){
        return ResponseEntity.ok(produtoService.readBySecao(secao));
    }

    //EDITAR
    @PutMapping("/{id}")
    @ApiOperation("Edita um produto a partir do ID")
    public ResponseEntity<Produto> editar(@PathVariable(value = "id") Long id,
                                        @Valid @RequestBody Produto produto){
        return ResponseEntity.ok(produtoService.edit(id, produto));
    }

    //DELETAR
    @DeleteMapping("/{id}")
    @ApiOperation("Deleta um produto a partir do ID")
    public ResponseEntity<Object> deletar(@PathVariable(value = "id") Long id){
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
