package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.CategoriaProduto;
import com.redeaoba.api.model.enums.SecaoProduto;
import com.redeaoba.api.repository.CategoriaProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    @Autowired
    CategoriaProdutoRepository categoriaProdutoRepository;

    //CREATE
    public CategoriaProduto create(CategoriaProduto categoriaProduto){
        if(!categoriaProdutoRepository.existsByNome(categoriaProduto.getNome())){
            return categoriaProdutoRepository.save(categoriaProduto);
        }
        throw new DomainException("Ja existe esta categoria de produto");
    }

    //CREATE MASSIVE
    public List<CategoriaProduto> create(List<CategoriaProduto> categorias) {
        List<CategoriaProduto> adicionadas = new ArrayList<>();
        for(CategoriaProduto c : categorias){
            if(!categoriaProdutoRepository.existsByNome(c.getNome())){
                CategoriaProduto add = categoriaProdutoRepository.save(c);
                adicionadas.add(add);
            }
        }
        if(categorias.size() != 0)
            return adicionadas;
        throw new DomainException("Nenhuma categoria foi adicionada pois elas ja existem no sistema");
    }


    //READ BY ID
    public CategoriaProduto read(Long id){
        return categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria de produto nao localizada"));
    }

    //READ LIKE
    public List<CategoriaProduto> readLike(String nome){
        return categoriaProdutoRepository.findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new NotFoundException("Categoria de produto nao localizada"));
    }

    //UPDATE
    public CategoriaProduto update(Long id, CategoriaProduto categoriaProduto){
        if(id != categoriaProduto.getId())
            throw new DomainException("inconsistencia na requisiçao: o ID nao bate com a categoria enviada");

        if(!categoriaProdutoRepository.existsById(id))
            throw new NotFoundException("Categoria de produto nao localizada");

        return categoriaProdutoRepository.save(categoriaProduto);
    }

    //DELETE
    public void delete(Long id){
        if(categoriaProdutoRepository.existsById(id))
            categoriaProdutoRepository.deleteById(id);

        throw new NotFoundException("Categoria de produto nao localizada");
    }

    public List<CategoriaProduto> readBySecao(String secao) {
        return categoriaProdutoRepository.findBySecao(SecaoProduto.valueOf(secao.toUpperCase()))
                .orElseThrow(() -> new NotFoundException("Seçao de produtos nao localizada"));
    }
}
