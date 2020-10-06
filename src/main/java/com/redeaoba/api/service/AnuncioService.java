package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.CategoriaProduto;
import com.redeaoba.api.model.Produto;
import com.redeaoba.api.model.Produtor;
import com.redeaoba.api.model.enums.SecaoProduto;
import com.redeaoba.api.model.enums.StatusAnuncio;
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
                throw new DomainException("Ja existe anuncio ativo e valido com esse produtor e produto");
            }
        }

        anuncioModel.setAtivo(true);
        anuncioModel.setDtCriacao(LocalDateTime.now());
        anuncioModel.setDtValidade(LocalDateTime.now().plusDays(7));
        Produtor produtor = produtorRepository.findById(anuncioModel.getProdutorId())
                .orElseThrow(() -> new NotFoundException("Produtor nao localizado"));
        Produto produto = produtoRepository.findById(anuncioModel.getProdutoId())
                .orElseThrow(() -> new NotFoundException("Produto nao localizado"));

        Anuncio anuncio = Anuncio.byModel(anuncioModel, produto, produtor);
        anuncio.setId(0);
        anuncio.setStatus(StatusAnuncio.ATIVO);

        return anuncioRepository.save(anuncio);
    }

    //READ
    public Anuncio read(Long id){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio nao localizado"));

        return anuncio;
    }

    //READ POR PRODUTOR
    public List<Anuncio> readByProdutor(Long produtorId){
        Produtor produtor = produtorRepository.findById(produtorId)
                .orElseThrow(() -> new NotFoundException("Produtor nao localizado"));
        List<Anuncio> anuncios = anuncioRepository.findByProdutorId(produtor.getId())
                .orElseThrow(() -> new NotFoundException("Nao ha anuncios desse produtor"));

        //Verifica aqueles que estão vencidos e com ativo == true
        for (Anuncio a : anuncios) {
            if (a.getStatus() == StatusAnuncio.INATIVO && a.isAtivo() == true){
                a.setAtivo(false);
                anuncioRepository.save(a);
            }
        }

        //Faz um filtro retirando DESATUALIZADOS e REMOVIDOS
        anuncios.stream().filter(a -> a.getStatus() != StatusAnuncio.DESATUALIZADO || a.getStatus() != StatusAnuncio.REMOVIDO);

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
                                    //VERIFICAR SE ESTa NA VALIDADE E ATIVO
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
    private Anuncio decrementQtde(Long id, int qtde){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio nao localizado"));

        anuncio.setQtdeMax(anuncio.getQtdeMax()-qtde);
        return anuncioRepository.save(anuncio);
    }

    //UPDATE QTDE E VALOR
    public void updateAnuncio(long id, int qtde, float valor){
        System.out.println("id: " + id
                + "\nqtde: " + qtde
                + "\nvalor: " + valor);

        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio nao localizado"));

        if (qtde == 0.0){
            throw new DomainException("Valor de qtde invalido");
        }

        if(valor > 0){
            //Criar um novo e desativar o antigo
            Anuncio anuncioNovo = new Anuncio();
            anuncioNovo.setDtValidade(LocalDateTime.now().plusDays(7));
            anuncioNovo.setQtdeMax(qtde);
            anuncioNovo.setValor(valor);
            anuncioNovo.setAtivo(true);
            anuncioNovo.setDtCriacao(LocalDateTime.now());
            anuncioNovo.setFotos(anuncio.getFotos());
            anuncioNovo.setProduto(anuncio.getProduto());
            anuncioNovo.setProdutor(anuncio.getProdutor());
            anuncioNovo.setStatus(StatusAnuncio.ATIVO);

            anuncioRepository.save(anuncioNovo);

            anuncio.setStatus(StatusAnuncio.DESATUALIZADO);
            anuncioRepository.save(anuncio);
        }else{
            //Atualizar o novo
            anuncio.setQtdeMax(qtde);
            anuncio.setDtValidade(LocalDateTime.now().plusDays(7));

            anuncioRepository.save(anuncio);
        }
    }

    //UPDATE VENCIMENTO
    public Anuncio updateVencimento(Long id, LocalDateTime validade){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio nao localizado"));

        anuncio.setDtValidade(validade);
        return anuncioRepository.save(anuncio);
    }

    //DESATIVAR
    public void disable(Long id){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio nao localizado"));
        anuncio.setAtivo(false);
        anuncio.setDtValidade(anuncio.getDtValidade().isAfter(LocalDateTime.now()) ? anuncio.getDtValidade() : LocalDateTime.now());
        anuncioRepository.save(anuncio);
    }

    //ATIVAR
    public void enable(Long id){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio nao localizado"));
        anuncio.setAtivo(true);
        anuncio.setDtValidade(LocalDateTime.now().plusDays(7));
        anuncioRepository.save(anuncio);
    }

    //DELETAR
    public void delete(Long id){
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anuncio nao localizado"));

        anuncio.setStatus(StatusAnuncio.REMOVIDO);
        anuncio.setAtivo(false);
        anuncioRepository.save(anuncio);
    }

}
