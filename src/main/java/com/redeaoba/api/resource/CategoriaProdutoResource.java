package com.redeaoba.api.resource;

import com.redeaoba.api.model.CategoriaProduto;
import com.redeaoba.api.service.CategoriaProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/categoria-produto")
@Api
@CrossOrigin(origins = "*")
public class CategoriaProdutoResource {

    @Autowired
    CategoriaProdutoService categoriaProdutoService;

    //CADASTRAR
    @PostMapping
    @ApiOperation("Cadastra uma categoria de produto. Não inserir o ID")
    public ResponseEntity<CategoriaProduto> cadastrar(@Valid @RequestBody CategoriaProduto categoria){
        return ResponseEntity.ok(categoriaProdutoService.create(categoria));
    }

    //CADASTRAR EM MASSA
    @PostMapping("/lote")
    @ApiOperation("Cadastra em massa")
    public ResponseEntity<List<CategoriaProduto>> cadastrarEmMassa(@RequestBody List<@Valid CategoriaProduto> categorias){
        return ResponseEntity.ok(categoriaProdutoService.create(categorias));
    }

    //LER POR ID
    @GetMapping("/{id}")
    @ApiOperation("Obtém uma categoria a partir do ID")
    public ResponseEntity<CategoriaProduto> obterPorId(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(categoriaProdutoService.read(id));
    }

    //LER POR SEÇÃO
    @GetMapping("secao/{secao}")
    @ApiOperation("Obtém as categorias a partir de uma seção. Ex: verdura")
    public ResponseEntity<List<CategoriaProduto>> obterPorSecao(@PathVariable(value = "secao") String secao){
        return ResponseEntity.ok(categoriaProdutoService.readBySecao(secao));
    }

    //BUSCAR COM FILTRO DE NOME
    @GetMapping("busca/{busca}")
    @ApiOperation("Obtém as categorias em que o nome contém a busca. Ex: 'ma'")
    public ResponseEntity<List<CategoriaProduto>> obterBusca(@PathVariable(value = "busca") String busca){
        return ResponseEntity.ok(categoriaProdutoService.readLike(busca));
    }

    //EDITAR
    @PutMapping("/{id}")
    @ApiOperation("Edita uma categoria de produto")
    public ResponseEntity<CategoriaProduto> editar(@PathVariable(value = "id") Long id,
                                                   @Valid @RequestBody CategoriaProduto categoria){
        return ResponseEntity.ok(categoriaProdutoService.update(id, categoria));
    }

    //DELETAR
    @DeleteMapping("/{id}")
    @ApiOperation("Deleta uma categoria de produto a partir do id")
    public ResponseEntity<Object> deletar(@PathVariable(value = "id") Long id){
        categoriaProdutoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
