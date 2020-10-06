package com.redeaoba.api.repository;

import com.redeaoba.api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<List<Pedido>> findByCompradorId(long compradorId);

//    Optional<List<Pedido>> findByProdutorId(long produtorId);
//
//    Optional<List<Pedido>> findByProdutorIdAndDtRespostaNotNull(long produtorId);
//
//    Optional<List<Pedido>> findByProdutorIdAndDtRespostaNull(long produtorId);
}
