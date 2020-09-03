package com.redeaoba.api.resource;

import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.representationModel.AnuncioModel;
import com.redeaoba.api.model.representationModel.AtivosSecaoModel;
import com.redeaoba.api.service.AnuncioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/anuncio")
@Api
@CrossOrigin(origins = "*")
public class AnuncioResource {

    @Autowired
    AnuncioService anuncioService;

    //CRIAR
    @PostMapping
    @ApiOperation("Cria um anuncio. Não inserir o ID")
    public ResponseEntity<Anuncio> criar(@RequestBody @Valid AnuncioModel anuncioModel){
        return ResponseEntity.ok(anuncioService.create(anuncioModel));
    }

    //LER POR ID
    @GetMapping("/{id}")
    @ApiOperation("Obtem um anuncio pelo ID")
    public ResponseEntity<Anuncio> obterPorId(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(anuncioService.read(id));
    }

    //LER POR PRODUTOR
    @GetMapping("/produtor/{id}")
    @ApiOperation("Obtem uma lista de anuncios a partir do ID pro produtor")
    public ResponseEntity<List<Anuncio>> obterPorProdutor(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(anuncioService.readByProdutor(id));
    }

    //LER ALL VALIDOS
    @GetMapping("/validos")
    @ApiOperation("Obtem uma lista de anuncios ativos [secao -> categoria -> produto -> anuncios]")
    public ResponseEntity<List<AtivosSecaoModel>> obterAtivos(){
        return ResponseEntity.ok(anuncioService.readAnunciosAtivos());
    }

    //DECREMENTAR QTDE
    @PutMapping("/{id}/decrementar-qtde/{qtde}")
    @ApiOperation("Diminui a quantidade de um anuncio")
    public ResponseEntity<Anuncio> decrementarQtde(@PathVariable(value = "id") Long id,
                                                   @PathVariable(value = "qtde") int qtde){
        return ResponseEntity.ok(anuncioService.decrementQtde(id, qtde));
    }

    //ATUALIZAR QTDE
    @PutMapping("/{id}/qtde/{qtde")
    public ResponseEntity<Anuncio> atualizarQtde(@PathVariable(value = "id") Long id,
                                                 @PathVariable(value = "qtde") int qtde){
        return ResponseEntity.ok(anuncioService.updateQtde(id, qtde));
    }

    //ATUALIZAR VENCIMENTO
    @PutMapping("/{id}/vencimento")
    public ResponseEntity<Anuncio> atualizarVencimento(@PathVariable(value = "id") Long id,
                                                       @RequestParam(value = "vencimento")LocalDateTime vencimento){
        return ResponseEntity.ok(anuncioService.updateVencimento(id, vencimento));
    }

    //DESATIVAR
    @PutMapping("/{id}/desativar")
    public ResponseEntity<Object> desativar(@PathVariable(value = "id") Long id){
        anuncioService.disable(id);
        return ResponseEntity.noContent().build();
    }

    //ATIVAR
    @PutMapping("/{id}/ativar")
    public ResponseEntity<Object> ativar(@PathVariable(value = "id") Long id){
        anuncioService.enable(id);
        return ResponseEntity.noContent().build();
    }

    //DETELAR
    @DeleteMapping("{id}")
    @ApiOperation("Deleta um anuncio por ID. [NÃO UTILIZAR ESSA REQUISIÇÃO EM PRODUÇÃO, ANUNCIOS NÃO DEVERÃO SER EXCLUÍDOS, APENAS INATIVADOS]")
    public ResponseEntity<Object> deletar(@PathVariable(value = "id") Long id){
        anuncioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
