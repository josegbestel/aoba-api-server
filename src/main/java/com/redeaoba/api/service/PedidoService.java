package com.redeaoba.api.service;

import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.*;
import com.redeaoba.api.model.representationModel.ItemCarrinhoModel;
import com.redeaoba.api.model.representationModel.PedidoModel;
import com.redeaoba.api.repository.AnuncioRepository;
import com.redeaoba.api.repository.ComercianteRepository;
import com.redeaoba.api.repository.EnderecoRepository;
import com.redeaoba.api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ComercianteRepository comercianteRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    AnuncioRepository anuncioRepository;


    //Criar
    public Pedido create(PedidoModel pedidoModel){
        Comerciante comerciante = comercianteRepository.findById(pedidoModel.getCompradorId())
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
        Endereco endereco = enderecoRepository.findById(pedidoModel.getEnderecoId())
                .orElseThrow(() -> new NotFoundException("Endereço não localizado"));

        List<ItemCarrinho> itens = new ArrayList<>();
        for(ItemCarrinhoModel item : pedidoModel.getItensCarrinho()){
            Anuncio anuncio = anuncioRepository.findById(item.getAnuncioId())
                    .orElseThrow(() -> new NotFoundException("Anúncio "+ item.getAnuncioId() +" não localizado"));
            itens.add(item.byModel(anuncio));
        }

        Pedido pedido = pedidoModel.byModel(comerciante, endereco, itens);
        return pedidoRepository.save(pedido);
    }


    //Obter por Comerciante
    public List<Pedido> readByCompradorId(long compradorId){
        if(!comercianteRepository.existsById(compradorId)){
            throw new NotFoundException("Comerciante não localizado");
        }
        return pedidoRepository.findByCompradorId(compradorId)
                .orElseThrow(() -> new NotFoundException("Pedido não localizado"));
    }
}
