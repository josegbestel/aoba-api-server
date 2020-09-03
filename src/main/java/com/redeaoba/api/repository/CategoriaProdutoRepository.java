package com.redeaoba.api.repository;

import com.redeaoba.api.model.CategoriaProduto;
import com.redeaoba.api.model.enums.SecaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {


    Optional<List<CategoriaProduto>> findBySecao(SecaoProduto secao);

    boolean existsByNome(String nome);

    Optional<List<CategoriaProduto>> findByNomeContainingIgnoreCase(String nome);
}

