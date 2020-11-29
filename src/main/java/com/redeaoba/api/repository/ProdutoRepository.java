package com.redeaoba.api.repository;

import com.redeaoba.api.model.CategoriaProduto;
import com.redeaoba.api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNome(String nome);
    Optional<List<Produto>> findByCategoriaId(Long categoriaId);
    Optional<Produto> findByNome(String nome);
}
