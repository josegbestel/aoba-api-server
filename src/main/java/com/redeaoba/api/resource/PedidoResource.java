package com.redeaoba.api.resource;

import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.representationModel.PedidoModel;
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

    //Criar pedido
    @PostMapping
    @ApiOperation("Cria um pedido")
    public ResponseEntity<Pedido> criarPedido(@Valid @RequestBody PedidoModel pedido){
        return ResponseEntity.ok(pedidoService.create(pedido));
    }

    //Obter pedidos por comerciante
    @GetMapping("/comerciante/{id}")
    @ApiOperation("Obtem todos os pedidos a partir do id do comerciante")
    public ResponseEntity<List<Pedido>> obterPedidosComerciante(@PathVariable(value = "id") long id){
        return ResponseEntity.ok(pedidoService.readByCompradorId(id));
    }
}
