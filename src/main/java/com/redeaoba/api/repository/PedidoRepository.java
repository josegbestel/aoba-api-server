package com.redeaoba.api.repository;

import com.redeaoba.api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<List<Pedido>> findByCompradorId(long compradorId);
}
