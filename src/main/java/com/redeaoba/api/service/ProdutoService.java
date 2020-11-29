package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.CategoriaProduto;
import com.redeaoba.api.model.Produto;
import com.redeaoba.api.model.enums.SecaoProduto;
import com.redeaoba.api.model.representationModel.ProdutoModel;
import com.redeaoba.api.repository.CategoriaProdutoRepository;
import com.redeaoba.api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    CategoriaProdutoRepository categoriaProdutoRepository;

    //CREATE
    public Produto create(ProdutoModel produtoModel){
        if(!produtoRepository.existsByNome(produtoModel.getNome())){
            CategoriaProduto categoria = categoriaProdutoRepository.findById(produtoModel.getCategoriaId())
                    .orElseThrow((() -> new NotFoundException("Categoria nao localizada")));

            Produto produto = new Produto();
            produto.setNome(produtoModel.getNome());
            produto.setFoto(produtoModel.getFoto());
            produto.setCategoria(categoria);
            return produtoRepository.save(produto);
        }
        throw new DomainException("Ja existe produto com esse nome");
    }

    //CREATE MASSIVE
    public List<Produto> create(List<ProdutoModel> produtosModel){
        List<Produto> produtos = new ArrayList<>();

        for(ProdutoModel pm : produtosModel){
            Optional<CategoriaProduto> categoria;
            if(pm.getCategoriaId() == 0)
                categoria = categoriaProdutoRepository.findByNome(pm.getCategoriaNome());
            else
                categoria = categoriaProdutoRepository.findById(pm.getCategoriaId());

            if(categoria.isPresent()){
                System.out.println("Achou a categoria " + categoria.get().getNome());
                Produto produto = new Produto();
                produto.setNome(pm.getNome());
                produto.setFoto(pm.getFoto());
                produto.setCategoria(categoria.get());

                //Se ja existir, instanciar o id
                Optional<Produto> existente = produtoRepository.findByNome(pm.getNome());
                if(existente.isPresent()){
                    produto.setId(existente.get().getId());
                }
                produtos.add(produtoRepository.save(produto));
            }else
                System.out.println("NÃO achou a categoria "
                        + pm.getCategoriaNome() + " "
                        + pm.getCategoriaId() + "("
                        + pm.getNome() + ")");
//            }
        }
        return produtos;

//        throw new NotFoundException("Nenhum produto foi cadastrado pois estes ja existem no sistema ou a categoria esta invalida");
    }

    //READ BY ID
    public Produto read(Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto nao localizado"));
    }

    //READ POR CATEGORIA
    public List<ProdutoModel> readByCategoria(Long categoriaId){
        CategoriaProduto categoria = categoriaProdutoRepository.findById(categoriaId)
                .orElseThrow(() -> new NotFoundException("Categoria nao localizada"));

        List<Produto> produtos = produtoRepository.findByCategoriaId(categoria.getId())
                .orElseThrow(() -> new NotFoundException("Produto nao localizado"));
        List<ProdutoModel> produtosModel = new ArrayList<>();
        produtos.forEach(p -> produtosModel.add(ProdutoModel.toModel(p)));

        return produtosModel;
    }


    //READ POR SECAO
    public List<ProdutoModel> readBySecao(String secao){
        List<CategoriaProduto> categorias = categoriaProdutoRepository.findBySecao(SecaoProduto.valueOf(secao.toUpperCase()))
                .orElseThrow(() -> new NotFoundException("Secao nao localizada"));

        List<Produto> allProdutos = new ArrayList<>();
        for(CategoriaProduto categoria : categorias){
            Optional<List<Produto>> produtos = produtoRepository.findByCategoriaId(categoria.getId());
            if(produtos.isPresent()){
                produtos.get().forEach(p -> allProdutos.add(p));
            }
        }
        if(allProdutos.size() == 0){
            throw new NotFoundException("Nao foi localizada a secao");
        }

        List<ProdutoModel> produtosModel = new ArrayList<>();
        allProdutos.forEach(ap -> produtosModel.add(ProdutoModel.toModel(ap)));

        return produtosModel;

    }


    //EDITAR PRODUTO
    public Produto edit(Long id, Produto produto){
        if(id != produto.getId()){
            throw new DomainException("inconsistência na requisicao: o ID nao bate com o produto enviado");
        }
        if(!produtoRepository.existsById(id)){
            throw new NotFoundException("Produto nao localizado");
        }

        return produtoRepository.save(produto);
    }


    //DELETAR PRODUTO
    public void delete(Long id){
        if(produtoRepository.existsById(id))
            produtoRepository.deleteById(id);

        throw new NotFoundException("Produto nao encontrado");

    }

    public List<Produto> readAll() {
        return produtoRepository.findAll();
    }
}
