package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.*;
import com.redeaoba.api.model.enums.OpcaoAlternativa;
import com.redeaoba.api.model.enums.StatusItem;
import com.redeaoba.api.model.enums.StatusPedido;
import com.redeaoba.api.model.representationModel.*;
import com.redeaoba.api.repository.*;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ComercianteRepository comercianteRepository;

    @Autowired
    ProdutorRepository produtorRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    AnuncioRepository anuncioRepository;

    @Autowired
    AsyncPedidoService asyncPedidoService;

    //Montar carrinho
    public PedidoTempModel rideCart(PedidoNovoModel pedidoNovoModel){
        Comerciante comerciante = comercianteRepository.findById(pedidoNovoModel.getCompradorId())
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));
        Optional<Endereco> endereco = enderecoRepository.findById(pedidoNovoModel.getEnderecoId());

        //Instanciar todos os itens
        List<ItemCarrinho> itens = new ArrayList<>();
        for(ItemCarrinhoLiteModel item : pedidoNovoModel.getItensCarrinho()) {
            Anuncio anuncio = anuncioRepository.findById(item.getAnuncioId())
                    .orElseThrow(() -> new NotFoundException("Anuncio " + item.getAnuncioId() + " nao localizado"));

            //Validar se tem essa quantidade disponível
            if (anuncio.getQtdeMax() < item.getQuantidade()) {
                throw new DomainException("Quantidade do item" + anuncio.getProduto().getNome() + " esta superior a disponivel");
            }
            itens.add(item.byModel(anuncio));
        }

        Pedido pedido = pedidoNovoModel.byModel(comerciante, endereco, itens);
        return PedidoTempModel.toModel(pedido);

        /*
        TODO
        - Implementar método de calcular frete no pedido
        - Implementar método toModel no PedidoTempModel
         */
    }

    //Criar
    public PedidoRealizadoModel create(PedidoNovoModel pedidoNovoModel) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();

        //Recusar pedidos das 20h às 12h
//        if(now.getHour() >= 20 || now.getHour() <= 11){
//            throw new DomainException("Pedidos nao podem ser realizados depois das 20h. " +
//                    "Deixe salvo esse carrinho e tente amanhã");
//        }

        Comerciante comerciante = comercianteRepository.findById(pedidoNovoModel.getCompradorId())
                .orElseThrow(() -> new NotFoundException("Comerciante nao localizado"));
        Endereco endereco = enderecoRepository.findById(pedidoNovoModel.getEnderecoId())
                .orElseThrow(() -> new NotFoundException("Endereco nao localizado"));

        //Instanciar todos os itens
        List<ItemCarrinho> itens = new ArrayList<>();
        for(ItemCarrinhoLiteModel item : pedidoNovoModel.getItensCarrinho()){
            Anuncio anuncio = anuncioRepository.findById(item.getAnuncioId())
                    .orElseThrow(() -> new NotFoundException("Anuncio "+ item.getAnuncioId() +" nao localizado"));

            //Validar se tem essa quantidade disponível
            if(anuncio.getQtdeMax() < item.getQuantidade()){
                throw new DomainException("Quantidade do item" + anuncio.getProduto().getNome() + " esta superior a disponivel");
            }
            itens.add(item.byModel(anuncio));
        }

        Pedido pedido = pedidoNovoModel.byModel(comerciante, endereco, itens);

        //Definições padrões de um pedido novo
        pedido = defaultSetingsNovoPedido(pedido);

        //Salvar pedido no BD
        pedido = pedidoRepository.save(pedido);

        //TODO: Adicionar uma trigger para ativar qnd vencer e n tiver resposta
        asyncPedidoService.acompanharPrazoResposta(pedido.getId(), pedido.getPrazoResposta());

        return PedidoRealizadoModel.toModel(pedido);
    }

    //Obter por Comerciante
    public List<PedidoRealizadoModel> readByCompradorId(long compradorId){
        if(!comercianteRepository.existsById(compradorId)){
            throw new NotFoundException("Comerciante nao localizado");
        }
        List<Pedido> pedidos = pedidoRepository.findByCompradorId(compradorId)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));

        return PedidoRealizadoModel.toModel(pedidos);
    }

    //Obter respondidos por Produtor
    public List<PedidoRealizadoModel> readRespondidosByProdutorId(long produtorId){
        Produtor produtor = produtorRepository.findById(produtorId)
                .orElseThrow((() -> new NotFoundException("Produtor nao localizado")));


        List<Pedido> pedidos = produtor.getPedidos();

        //Filtrar apenas casos respondidos
        List<Pedido> pedidosProdutor = new ArrayList<>();
        for (Pedido p : pedidos){
            if(p.getItensCarrinhoByProdutorId(produtorId).get(0).getDtResposta() != null){
                pedidosProdutor.add(p);
            }
        }
        return PedidoRealizadoModel.toModel(pedidosProdutor);
    }

    //Obter novos por Produtor
    public List<PedidoProdutorNovoModel> readNovosByProdutorId(long produtorId){
        Produtor produtor = produtorRepository.findById(produtorId)
                .orElseThrow((() -> new NotFoundException("Produtor nao localizado")));

        //Filtrar apenas casos não respondidos
        List<Pedido> pedidos = new ArrayList<>();
        for (Pedido p : produtor.getPedidos()){
            if(p.getItensCarrinhoByProdutorId(produtorId).get(0).getDtResposta() == null){
                pedidos.add(p);
            }
        }

        if(pedidos.size() == 0){
            throw new NotFoundException("Nao ha nenhum pedido para este produtor");
        }

        List<PedidoProdutorNovoModel> novos = new ArrayList<>();
        for(Pedido p : pedidos){
            List<ItemCarrinho> itensProdutor = p.getItensCarrinhoByProdutorId(produtorId);
            if(itensProdutor.get(0).getDtResposta() == null){
                p.setItensCarrinho(itensProdutor);
                novos.add(PedidoProdutorNovoModel.toModel(p));
            }
        }
        return novos;
    }

    //Responder produtor
    public void updatePedidoResponder(long pedidoId, long produtorId, boolean aceite){
        Produtor produtor = produtorRepository.findById(produtorId)
                .orElseThrow((() -> new NotFoundException("Produtor nao localizado")));

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));

        if(pedido.getItensCarrinhoByProdutorId(produtorId).size() == 0){
            throw new DomainException("Este produtor nao tem nenhum item neste pedido");
        }

        if(aceite){
            List<ItemCarrinho> itensProdutor = pedido.getItensCarrinhoByProdutorId(produtorId);
            pedido.confirmItensByProdutorId(produtorId);
            pedidoRepository.save(pedido);
        }else{
            if(pedido.getOpcaoAlternativa() == OpcaoAlternativa.ACEITAR_SUGESTAO){
                //TODO: Buscar outra opção
            }else if(pedido.getOpcaoAlternativa() == OpcaoAlternativa.CANCELAR_PRODUTO){
                pedido.cancelItensByProdutorId(produtorId);
                pedidoRepository.save(pedido);
            }else{
                throw new DomainException("O pedido nao tem opcao alternativa definida");
            }
        }
    }

    //Entregar pedido
    public void updatePedidoEntregar(long pedidoId){
        LocalDateTime now = LocalDateTime.now();

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));
        pedido.setStatus(StatusPedido.ENTREGUE);
        pedido.setDtEntrega(now);

        for (ItemCarrinho i : pedido.getItensCarrinho()) {
            i.setStatus(StatusItem.FINALIZADO);
            i.setDtResposta(now);
        }

        pedidoRepository.save(pedido);
    }


    //[interna] Calcular o frete
    private float calcularFrete(Pedido pedido){
        //TODO: Implementar algorítmo para calcular frete
        return 50;
    }

    //[internat] Definir variáveis padrões ao criar um pedido
    private Pedido defaultSetingsNovoPedido(Pedido pedido){
        LocalDateTime now = LocalDateTime.now();

        //Define a data de criação
        pedido.setDtCriacao(now);
        //Define o prazo default (30min para responder)
        pedido.setPrazoResposta(now.plusMinutes(30));
        //Define o status inicial
        pedido.setStatus(StatusPedido.REALIZADO);
        //Definir valor do frete
        pedido.setValorFrete(calcularFrete(pedido));
        //Define o prazo de cada item do pedido
        pedido.getItensCarrinho().forEach(i -> i.setDtPrazoResposta(pedido.getPrazoResposta()));

        return pedido;
    }


}
