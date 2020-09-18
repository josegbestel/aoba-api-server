package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.CategoriaProduto;
import com.redeaoba.api.model.Produto;
import com.redeaoba.api.model.Produtor;
import com.redeaoba.api.model.enums.SecaoProduto;
import com.redeaoba.api.model.representationModel.*;
import com.redeaoba.api.repository.AnuncioRepository;
import com.redeaoba.api.repository.CategoriaProdutoRepository;
import com.redeaoba.api.repository.ProdutoRepository;
import com.redeaoba.api.repository.ProdutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {

    @Autowired
    AnuncioRepository anuncioRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutorRepository produtorRepository;

    @Autowired
    CategoriaProdutoRepository categoriaProdutoRepository;

    //CREATE
    public Anuncio create(AnuncioModel anuncioModel){
        Optional<Anuncio> anuncioExistente = anuncioRepository.findByProdutorIdAndProdutoId(anuncioModel.getProdutorId(), anuncioModel.getProdutoId());
        if(anuncioExistente.isPresent()){
            if(anuncioExistente.get().isValido() && anuncioExistente.get().isAtivo()){
                throw new DomainException("Ja existe anuncio ativo e válido com esse produtor e produto");
            }
        }

        anuncioModel.setAtivo(true);
        anuncioModel.setDtCriacao(LocalDateTime.now());
        anuncioModel.setDtValidade(LocalDateTime.now().plusDays(7));
        Produtor produtor = produtorRepository.findById(anuncioModel.getProdutorId())
                .orElseThrow(() -> new NotFoundException("Produtor não localizado"));
        Produto produto = produtoRepository.findById(anuncioModel.getProdutoId())
                .orElseThrow(() -> new NotFoundException("Produto não localizado"));

        Anuncio anuncio = Anuncio.byModel(anuncioModel, produto, produtor);
        anuncio.setId(0);

        return anuncioRepository.save(anuncio);
    }

    //READ
    public Anuncio read(Long id){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio não localizado"));

        return anuncio;
    }

    //READ POR PRODUTOR
    public List<Anuncio> readByProdutor(Long produtorId){
        Produtor produtor = produtorRepository.findById(produtorId)
                .orElseThrow(() -> new NotFoundException("Produtor não localizado"));
        List<Anuncio> anuncios = anuncioRepository.findByProdutorId(produtor.getId())
                .orElseThrow(() -> new NotFoundException("Não há anuncios desse produtor"));

        return anuncios;
    }

    //READ ATIVOS POR SECAO-CATEGORIA-ANUNCIO
    public List<AtivosSecaoModel> readAnunciosAtivos(){
        List<SecaoProduto> secoes = Arrays.asList(SecaoProduto.values());

        //LIST SECOES MODEL
        List<AtivosSecaoModel> secoesModel = new ArrayList<>();

        //Obter as seções
        for (SecaoProduto secao: secoes) {
            //SECAO MODEL
            AtivosSecaoModel secaoModel = new AtivosSecaoModel();
            secaoModel.setSecao(secao);

            //Obter categorias das seções
            Optional<List<CategoriaProduto>> categoriasProduto = categoriaProdutoRepository.findBySecao(secao);
            if(categoriasProduto.isPresent()){
                //LIST CATEGORIAS MODEL
                List<AtivosCategoriaModel> categoriasModel = new ArrayList<>();
                for (CategoriaProduto categoria : categoriasProduto.get()){
                    //CATEGORIA MODEL
                    AtivosCategoriaModel categoriaModel = new AtivosCategoriaModel();
                    categoriaModel.setCategoria(categoria.getNome());
                    categoriaModel.setFoto(categoria.getFoto());

                    //Obter produtos de uma determinada categoria
                    Optional<List<Produto>> produtos = produtoRepository.findByCategoriaId(categoria.getId());
                    if(produtos.isPresent()){
                        //LIST PRODUTOS MODEL
                        List<AtivosProdutoModel> produtosModel = new ArrayList<>();
                        //PRODUTO MODEL
                        for(Produto produto : produtos.get()){
                            AtivosProdutoModel produtoModel = new AtivosProdutoModel();
                            produtoModel.setNome(produto.getNome());
                            produtoModel.setFoto(produto.getFoto());

                            //TODO: MELHORAR: BUSCAR POR PRODUTO, ATIVO = TRUE E DT VALIDADE >= AGORA
                            Optional<List<Anuncio>> anuncios = anuncioRepository.findByProdutoId(produto.getId());
                            if(anuncios.isPresent()){
                                //LIST ANUNCIOS MODEL
                                List<AtivosAnuncioModel> anunciosModel = new ArrayList<>();
                                //ANUNCIO MODEL
                                for(Anuncio anuncio : anuncios.get()){
                                    //VERIFICAR SE ESTÁ NA VALIDADE E ATIVO
                                    if (anuncio.isAtivo() && anuncio.isValido()){
                                        AtivosAnuncioModel ativosAnuncioModel = AtivosAnuncioModel.toModel(anuncio);
                                        anunciosModel.add(ativosAnuncioModel);
                                    }
                                }
                                produtoModel.setAnuncios(anunciosModel);
                            }else{
                                produtoModel.setAnuncios(new ArrayList<>());
                            }
                            if(produtoModel.getAnuncios().size() != 0)
                                produtosModel.add(produtoModel);
                        }
                        categoriaModel.setProdutos(produtosModel);
                    }else{
                        categoriaModel.setProdutos(new ArrayList<>());
                    }
                    if(categoriaModel.getProdutos().size() != 0)
                        categoriasModel.add(categoriaModel);
                }
                secaoModel.setCategorias(categoriasModel);
            }else{
                secaoModel.setCategorias(new ArrayList<>());
            }
            if(secaoModel.getCategorias().size() != 0)
                secoesModel.add(secaoModel);
        }
        return secoesModel;
    }

    //DECREMENTAR QUANTIDADE
    public Anuncio decrementQtde(Long id, int qtde){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio não localizado"));

        anuncio.setQtdeMax(anuncio.getQtdeMax()-qtde);
        return anuncioRepository.save(anuncio);
    }

    //UPDATE QUANTIDADE
    public Anuncio updateQtde(Long id, int qtde){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio não localizado"));

        anuncio.setQtdeMax(qtde);
        return anuncioRepository.save(anuncio);
    }

    //UPDATE VENCIMENTO
    public Anuncio updateVencimento(Long id, LocalDateTime validade){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio não localizado"));

        anuncio.setDtValidade(validade);
        return anuncioRepository.save(anuncio);
    }

    //DESATIVAR
    public void disable(Long id){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio não localizado"));
        anuncio.setAtivo(false);
        anuncio.setDtValidade(anuncio.getDtValidade().isAfter(LocalDateTime.now()) ? anuncio.getDtValidade() : LocalDateTime.now());
        anuncioRepository.save(anuncio);
    }

    //ATIVAR
    public void enable(Long id){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio não localizado"));
        anuncio.setAtivo(true);
        anuncio.setDtValidade(LocalDateTime.now().plusDays(7));
        anuncioRepository.save(anuncio);
    }

    //DELETAR
    public void delete(Long id){
        if(!anuncioRepository.existsById(id))
            throw new NotFoundException("Anuncio não localizado");

        anuncioRepository.deleteById(id);

    }

}
