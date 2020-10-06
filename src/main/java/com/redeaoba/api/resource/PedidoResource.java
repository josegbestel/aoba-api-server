package com.redeaoba.api.resource;

import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.representationModel.PedidoProdutorNovoModel;
import com.redeaoba.api.model.representationModel.PedidoNovoModel;
import com.redeaoba.api.model.representationModel.PedidoRealizadoModel;
import com.redeaoba.api.model.representationModel.PedidoTempModel;
import com.redeaoba.api.service.PedidoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PedidoTempModel> criarCarrinho(@Valid @RequestBody PedidoNovoModel pedidoNovoModel){
        return ResponseEntity.ok(pedidoService.rideCart(pedidoNovoModel));
    }

    //Criar pedido
    @PostMapping
    @ApiOperation("Cria um pedido")
    public ResponseEntity<PedidoRealizadoModel> criarPedido(@Valid @RequestBody PedidoNovoModel pedidoNovoModel) throws InterruptedException {
        return  ResponseEntity.ok(pedidoService.create(pedidoNovoModel));
    }

    //Obter pedidos por comerciante
    @GetMapping("/comerciante/{id}")
    @ApiOperation("Obtem todos os pedidos a partir do id do comerciante")
    public ResponseEntity<List<PedidoRealizadoModel>> obterPedidosComerciante(@PathVariable(value = "id") long id){
        return ResponseEntity.ok(pedidoService.readByCompradorId(id));
    }

    //Obter respondidos por produtor
    @GetMapping("/respondidos/produtor/{id}")
    @ApiOperation("Obtem todos os pedidos respondidos a partir do id do produtor")
    public ResponseEntity<List<PedidoRealizadoModel>> obterPedidosRespondidosProdutor(@PathVariable(value = "id") long id){
        return ResponseEntity.ok(pedidoService.readRespondidosByProdutorId(id));
    }

    //Obter novos por produtor
    @GetMapping("/novos/produtor/{id}")
    @ApiOperation("Obtem todos os pedidos novos a partir do id do produtor")
    public ResponseEntity<List<PedidoProdutorNovoModel>> obterPedidosNovosProdutor(@PathVariable(value = "id") long id){
        return ResponseEntity.ok((pedidoService.readNovosByProdutorId(id)));
    }

    //Responder pedido
    @PutMapping("/{pedidoId}/produtor/{produtorId}")
    @ApiOperation("Responde um pedido a partir do id")
    public ResponseEntity<Object> responderPedidoProdutor(@PathVariable(value = "pedidoId") long pedidoId,
                                                          @PathVariable(value = "produtorId") long produtorId,
                                                          @RequestParam(value = "aceite") boolean aceite){
        pedidoService.updatePedidoResponder(pedidoId, produtorId, aceite);
        return ResponseEntity.noContent().build();
    }

    //Entregar pedido
    @PutMapping("/{id}/entregar")
    @ApiOperation("Entrega um pedido")
    public ResponseEntity<Object> entregarPedido(@PathVariable(value = "id") long id){
        pedidoService.updatePedidoEntregar(id);
        return ResponseEntity.noContent().build();
    }
}
