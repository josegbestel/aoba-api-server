package com.redeaoba.api.resource;

import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.representationModel.AnuncioModel;
import com.redeaoba.api.model.representationModel.AtivosSecaoModel;
import com.redeaoba.api.service.AnuncioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @ApiOperation("Cria um anuncio. ** Não inserir o ID ** ")
    public ResponseEntity<Anuncio> criar(@RequestBody @Valid AnuncioModel anuncioModel,
                                         @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(anuncioService.create(anuncioModel, userDetails.getUsername()));
    }

    //LER POR ID
    @GetMapping("/{id}")
    @ApiOperation("Obtem um anuncio pelo ID do anúncio")
    public ResponseEntity<Anuncio> obterPorId(@PathVariable(value = "id") Long id,
                                              @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(anuncioService.read(id, userDetails.getUsername()));
    }

    //LER POR PRODUTOR
    @GetMapping("/produtor/{id}")
    @ApiOperation("Obtem uma lista de anuncios a partir do ID pro produtor")
    public ResponseEntity<List<Anuncio>> obterPorProdutor(@PathVariable(value = "id") Long id,
                                                          @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(anuncioService.readByProdutor(id, userDetails.getUsername()));
    }

    //LER ALL VALIDOS
    @GetMapping("/validos")
    @ApiOperation("Obtem uma lista de anuncios ativos [secao -> categoria -> produto -> anuncios]")
    public ResponseEntity<List<AtivosSecaoModel>> obterAtivos(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(anuncioService.readAnunciosAtivos());
    }

    //Editar
    @PutMapping("/{id}")
    @ApiOperation("Edita a quantidade e valor de um anúncio já criado pelo ID do anúncio. " +
            "Se editar o valor, o anuncio atual será desativado e criará um novo")
    public ResponseEntity<Object> editarAnuncio(@PathVariable(value = "id") Long id,
                                                @RequestParam(value = "qtde", required = false, defaultValue = "0") int qtde,
                                                @RequestParam(value = "valor", required = false, defaultValue = "0.0") float valor,
                                                @AuthenticationPrincipal UserDetails userDetails){
        anuncioService.updateAnuncio(id, qtde, valor, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    //ATUALIZAR VENCIMENTO
    @PutMapping("/{id}/vencimento")
    public ResponseEntity<Anuncio> atualizarVencimento(@PathVariable(value = "id") Long id,
                                                       @RequestParam(value = "vencimento")LocalDateTime vencimento,
                                                       @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(anuncioService.updateVencimento(id, vencimento, userDetails.getUsername()));
    }

    //DESATIVAR
    @PutMapping("/{id}/desativar")
    public ResponseEntity<Object> desativar(@PathVariable(value = "id") Long id,
                                            @AuthenticationPrincipal UserDetails userDetails){
        anuncioService.disable(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    //ATIVAR
    @PutMapping("/{id}/ativar")
    public ResponseEntity<Object> ativar(@PathVariable(value = "id") Long id,
                                         @AuthenticationPrincipal UserDetails userDetails){
        anuncioService.enable(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    //DETELAR
    @DeleteMapping("{id}")
    @ApiOperation("Deleta um anuncio por ID. [NÃO UTILIZAR ESSA REQUISIÇÃO EM PRODUÇÃO, ANUNCIOS NÃO DEVERÃO SER EXCLUÍDOS, APENAS INATIVADOS]")
    public ResponseEntity<Object> deletar(@PathVariable(value = "id") Long id,
                                          @AuthenticationPrincipal UserDetails userDetails){
        anuncioService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
