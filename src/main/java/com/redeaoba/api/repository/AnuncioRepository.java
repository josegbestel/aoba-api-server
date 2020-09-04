package com.redeaoba.api.repository;

import com.redeaoba.api.model.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

    boolean existsByProdutorId(long produtorId);
    Optional<List<Anuncio>> findByProdutorId(long produtorId);
    Optional<List<Anuncio>> findByProdutoId(long produtoId);

    boolean existsByProdutorIdAndProdutoId(long produtorId, long produtoId);

    Optional<List<Anuncio>> findByDtValidadeGreaterThan(LocalDateTime dtValidade);

    Optional<Anuncio> findByProdutorIdAndProdutoId(Long produtorId, long produtoId);
}
