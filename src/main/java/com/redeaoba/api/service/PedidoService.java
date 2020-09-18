package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.*;
import com.redeaoba.api.model.representationModel.ItemCarrinhoLiteModel;
import com.redeaoba.api.model.representationModel.ItemCarrinhoModel;
import com.redeaoba.api.model.representationModel.PedidoModel;
import com.redeaoba.api.model.representationModel.PedidoTempModel;
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

    //Montar carrinho
    public PedidoTempModel rideCart(PedidoModel pedidoModel){
        Comerciante comerciante = comercianteRepository.findById(pedidoModel.getCompradorId())
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
        Endereco endereco = enderecoRepository.findById(pedidoModel.getEnderecoId())
                .orElseThrow(() -> new NotFoundException("Endereço não localizado"));

        //Instanciar todos os itens
        List<ItemCarrinho> itens = new ArrayList<>();
        for(ItemCarrinhoLiteModel item : pedidoModel.getItensCarrinho()) {
            Anuncio anuncio = anuncioRepository.findById(item.getAnuncioId())
                    .orElseThrow(() -> new NotFoundException("Anúncio " + item.getAnuncioId() + " não localizado"));

            //Validar se tem essa quantidade disponível
            if (anuncio.getQtdeMax() < item.getQuantidade()) {
                throw new DomainException("Quantidade do item" + anuncio.getProduto().getNome() + " está superior à disponível");
            }
            itens.add(item.byModel(anuncio));
        }

        Pedido pedido = pedidoModel.byModel(comerciante, endereco, itens);
        return PedidoTempModel.toModel(pedido);

        /*
        TODO
        - Implementar método de calcular frete no pedido
        - Implementar método toModel no PedidoTempModel
         */
    }


    //Criar
    public Pedido create(PedidoModel pedidoModel){
        Comerciante comerciante = comercianteRepository.findById(pedidoModel.getCompradorId())
                .orElseThrow(() -> new NotFoundException("Comerciante não localizado"));
        Endereco endereco = enderecoRepository.findById(pedidoModel.getEnderecoId())
                .orElseThrow(() -> new NotFoundException("Endereço não localizado"));

        //Instanciar todos os itens
        List<ItemCarrinho> itens = new ArrayList<>();
        for(ItemCarrinhoLiteModel item : pedidoModel.getItensCarrinho()){
            Anuncio anuncio = anuncioRepository.findById(item.getAnuncioId())
                    .orElseThrow(() -> new NotFoundException("Anúncio "+ item.getAnuncioId() +" não localizado"));

            //Validar se tem essa quantidade disponível
            if(anuncio.getQtdeMax() < item.getQuantidade()){
                throw new DomainException("Quantidade do item" + anuncio.getProduto().getNome() + " está superior à disponível");
            }
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
