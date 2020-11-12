package com.redeaoba.api.resource;

import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.representationModel.*;
import com.redeaoba.api.service.PedidoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/pedido")
@Api
@CrossOrigin(origins = "*")
public class PedidoResource {

    @Autowired
    PedidoService pedidoService;

    @PostMapping("/carrinho")
    @ApiOperation("Requisita os itens, comprador e endereço e a API retorna um item completo com as infos do carrinho")
    public ResponseEntity<PedidoTempModel> criarCarrinho(@Valid @RequestBody PedidoNovoModel pedidoNovoModel,
                                                         @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(pedidoService.rideCart(pedidoNovoModel, userDetails.getUsername()));
    }

    //Criar pedido
    @PostMapping
    @ApiOperation("Cria um pedido")
    public ResponseEntity<PedidoRealizadoModel> criarPedido(@Valid @RequestBody PedidoNovoModel pedidoNovoModel,
                                                            @AuthenticationPrincipal UserDetails userDetails) throws InterruptedException {
        return  ResponseEntity.ok(pedidoService.create(pedidoNovoModel, userDetails.getUsername()));
    }

    //Obter pedidos por comerciante
    @GetMapping("/comerciante/{id}")
    @ApiOperation("Obtem todos os pedidos a partir do id do comerciante")
    public ResponseEntity<List<PedidoRealizadoModel>> obterPedidosComerciante(@PathVariable(value = "id") long id,
                                                                              @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(pedidoService.readByCompradorId(id, userDetails.getUsername()));
    }

    //Obter respondidos por produtor
    @GetMapping("/respondidos/produtor/{id}")
    @ApiOperation("Obtem todos os pedidos respondidos a partir do id do produtor")
    public ResponseEntity<List<PedidoRealizadoModel>> obterPedidosRespondidosProdutor(@PathVariable(value = "id") long id,
                                                                                      @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(pedidoService.readRespondidosByProdutorId(id, userDetails.getUsername()));
    }

    //Obter novos por produtor
    @GetMapping("/novos/produtor/{id}")
    @ApiOperation("Obtem todos os pedidos novos a partir do id do produtor")
    public ResponseEntity<List<PedidoProdutorNovoModel>> obterPedidosNovosProdutor(@PathVariable(value = "id") long id,
                                                                                   @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok((pedidoService.readNovosByProdutorId(id, userDetails.getUsername())));
    }

    //Responder pedido
    @PutMapping("/{pedidoId}/produtor/{produtorId}")
    @ApiOperation("Responde um pedido a partir do id")
    public ResponseEntity<Object> responderPedidoProdutor(@PathVariable(value = "pedidoId") long pedidoId,
                                                          @PathVariable(value = "produtorId") long produtorId,
                                                          @RequestParam(value = "aceite") boolean aceite,
                                                          @AuthenticationPrincipal UserDetails userDetails) throws InterruptedException {
        pedidoService.updatePedidoResponder(pedidoId, produtorId, aceite, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    //Comerciante confirmar opção alternativa
    //ex: /31//item/32?aceite=true
    @PutMapping("/{pedidoId}/item/{itemId}")
    @ApiOperation("O Comerciante confirma ou não o item de opção alternativa com valor superior ao anterior")
    public ResponseEntity<Object> confirmarOpcaoAlternativa(@PathVariable(value = "pedidoId") long pedidoId,
                                                            @PathVariable(value = "itemId") long itemId,
                                                            @RequestParam(value = "aceite") boolean aceite,
                                                            @AuthenticationPrincipal UserDetails userDetails) throws InterruptedException {
        pedidoService.confirmOpcaoAlternativa(pedidoId, itemId, aceite, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    //Entregar pedido
    @PutMapping("/{id}/entregar")
    @ApiOperation("Entrega um pedido")
    public ResponseEntity<Object> entregarPedido(@PathVariable(value = "id") long id){
        pedidoService.updatePedidoEntregar(id);
        return ResponseEntity.noContent().build();
    }

    //Reprocessar OpcaoAlternativa
    @PutMapping("/{pedidoId}/item/{itemId}/reprocessar")
    public ResponseEntity<Object> reprocessarOpcaoAlternativa(@PathVariable(value = "pedidoId") long pedidoId,
                                                              @PathVariable(value = "itemId") long itemId) throws InterruptedException {

        pedidoService.reprocessarOpcaoALternativa(pedidoId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{pedidoId}/avaliar")
    public ResponseEntity<Object> avaliarPedido(@PathVariable(value = "pedidoId") long pedidoId,
                                                @Valid @RequestBody AvaliacaoModel avaliacao,
                                                @AuthenticationPrincipal UserDetails userDetails){
        pedidoService.avaliarPedido(pedidoId, avaliacao, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
