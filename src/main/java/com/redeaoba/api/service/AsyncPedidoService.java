package com.redeaoba.api.service;

import com.redeaoba.api.config.ApplicationConfig;
import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.model.Anuncio;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.Produtor;
import com.redeaoba.api.model.enums.OpcaoAlternativa;
import com.redeaoba.api.model.enums.StatusAnuncio;
import com.redeaoba.api.model.enums.StatusItem;
import com.redeaoba.api.repository.AnuncioRepository;
import com.redeaoba.api.repository.ItemCarrinhoRepository;
import com.redeaoba.api.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AsyncPedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    AnuncioRepository anuncioRepository;

    @Autowired
    ItemCarrinhoRepository itemCarrinhoRepository;

//    @Autowired
//    PedidoService pedidoService;

    @Async
    public void acompanharPrazoResposta(long pedidoId, LocalDateTime prazo) throws InterruptedException {
//        LocalDateTime now = LocalDateTime.now();
//
//        long millis = ChronoUnit.MILLIS.between(now, prazo);
//
//        LocalDateTime prazoDateTime = LocalDateTime.now().plusSeconds(millis/1000);
//        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") => prazo: " + prazoDateTime + " [" + millis + " millis]");
//        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") =>  Inicio da espera");
//        Thread.sleep(millis);
//
////        Hibernate.initialize(Pedido.class);
//
//        //Lógica do pedido aqui
//        Pedido pedido = pedidoRepository.findById(pedidoId)
//                .orElseThrow((() -> new DomainException("Houve problema ao consultar o pedido " + pedidoId)));
//
//        for (ItemCarrinho i : pedido.getItensPendentes()) {
//            //Se o prazo do item for < do que agora
//            if(i.getDtPrazoResposta().isBefore(LocalDateTime.now())){
//                //Verifica se item teve resposta
//                if(i.getDtResposta() == null){
//                    //Item sem resposta
//                    if(pedido.getOpcaoAlternativa() == OpcaoAlternativa.ACEITAR_SUGESTAO){
//                        //Buscar alternativa
//                        buscarOpcaoItem(pedido, i);
//                    }else{
//                        //Cancelar item
//                        i.setStatus(StatusItem.CANCELADO);
//                    }
//                }
//            }
//
//        }
//
//        //Persistir em banco
//        pedido.refreshStatus();
//        pedidoRepository.save(pedido);
//
//        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") => fim da espera");
    }

    //[interna] Buscar opção alternativa
    private void buscarOpcaoItem(Pedido pedido, ItemCarrinho item) throws InterruptedException {

        List<Anuncio> anuncios;
        List<ItemCarrinho> itensProduto;
        List<Produtor> produtoresJaSolicitados = new ArrayList<>();

        //Validar se já não possui o máximo de interaços no item
        if(item.getInteracoesRespostas() >= ApplicationConfig.MAX_INTERACOES_RESPOSTA) {
            System.out.printf("Item já possui o máximo de interações, não é mais possível achar opção alternativa então será CANCELADO");
            //Cancelar item do pedido
            this.cancelarItemPedido(pedido, item);
            return;
        }

        //Buscar todos os anúncios ativos deste produto
        Optional<List<Anuncio>> anunciosOpt = anuncioRepository.findByProdutoId(item.getAnuncio().getProduto().getId());

        if(anunciosOpt.isPresent()){
            anuncios = anunciosOpt.get();

            //Filtar anuncios ativos
            anuncios = anuncios.stream().filter(i -> i.getStatus() == StatusAnuncio.ATIVO).collect(Collectors.toList());

            //Filtrar os que tem o preço <= item.getAnuncio().getValor()
            anuncios = anuncios.stream().filter(a -> a.getValor() <= item.getAnuncio().getValor()).collect(Collectors.toList());

            //Filtrar anúncios apenas de produtores que não tiveram interação com este item
            //Obter todos os itens do pedido que tem esse produto
            itensProduto = pedido.getItensCarrinho();
            itensProduto = itensProduto
                    .stream()
                    .filter(i -> i.getAnuncio().getProduto().getId() == item.getAnuncio().getProduto().getId())
                    .collect(Collectors.toList());

            //Obter lista de produtores que já tem um item deste produto neste pedido
            produtoresJaSolicitados.clear();
            itensProduto.forEach(i -> produtoresJaSolicitados.add(i.getAnuncio().getProdutor()));

            //Filtrar os itens que o produtor é != da lista de pedidos já existente no pedido
            for (Produtor p : produtoresJaSolicitados){
                anuncios = anuncios.stream().filter(a -> a.getProdutor().getId() != p.getId()).collect(Collectors.toList());
            }

            //Ordernar pelo rate do produtor
            Collections.sort(anuncios, new Comparator<Anuncio>() {
                @Override
                public int compare(Anuncio a1, Anuncio a2) {
                    boolean isBigger = a1.getProdutor().getRating() > a2.getProdutor().getRating();
                    if(a1.getProdutor().getRating() < a2.getProdutor().getRating())
                        return 1;
                    else if(a1.getProdutor().getRating() > a2.getProdutor().getRating())
                        return -1;
                    else
                        return 0;
                }
            });

            System.out.println("Anuncíos ordernados:");
            for (Anuncio a : anuncios){
                System.out.println("[rating: " + a.getProdutor().getRating()
                        + "] [id: " + a.getId()
                        + "] [produtor: " + a.getProdutor().getNome() + "]");
            }

            //Se tiver anúncios com preço >=
            if(anuncios.size() >0){
                //Pegar o primeiro
                Anuncio anuncioSegundaOpcao = anuncios.get(0);

                //Alterar anuncio do item
                pedido = alterarAnuncioItemPedido(pedido, item, anuncioSegundaOpcao, false);

                //TODO: Enviar notificação para produtor
            }else{
                //NÃO HÁ OPÇÕES DISPONÍVEIS
                // Buscar 2a opção com o menor preço (mas que preço seja >= atual)
                anuncios = anunciosOpt.get();

                //Filtar anuncios ativos
                anuncios = anuncios.stream().filter(i -> i.getStatus() == StatusAnuncio.ATIVO).collect(Collectors.toList());

                //Filtrar os que tem o preço >= item.getAnuncio().getValor()
                anuncios = anuncios.stream().filter(a -> a.getValor() >= item.getAnuncio().getValor()).collect(Collectors.toList());

                //Obter todos os itens do pedido que tem esse produto
                itensProduto = pedido.getItensCarrinho();
                itensProduto = itensProduto
                        .stream()
                        .filter(i -> i.getAnuncio().getProduto().getId() == item.getAnuncio().getProduto().getId())
                        .collect(Collectors.toList());

                //Obter lista de produtores que já tem um item deste produto neste pedido
                produtoresJaSolicitados.clear();
                itensProduto.forEach(i -> produtoresJaSolicitados.add(i.getAnuncio().getProdutor()));

                //Filtrar os itens que o produtor é != da lista de pedidos já existente no pedido
                for (Produtor p : produtoresJaSolicitados){
                    anuncios = anuncios.stream().filter(a -> a.getProdutor().getId() != p.getId()).collect(Collectors.toList());
                }

                if(anuncios.size() >0){
                    //Ordernar pelo valor do anúncio
                    Collections.sort(anuncios, new Comparator<Anuncio>() {
                        @Override
                        public int compare(Anuncio a1, Anuncio a2) {
                            if(a1.getValor() > a2.getValor())
                                return 1;
                            else if(a1.getValor() < a2.getValor())
                                return -1;
                            else
                                return 0;
                        }
                    });

                    System.out.println("Anuncíos ordernados:");
                    for (Anuncio a : anuncios){
                        System.out.println("[produtor: " + a.getProdutor().getNome()
                                + "] [id: " + a.getId()
                                + "] [valor: " + a.getValor() + "]");
                    }

                    //Pegar primeira oção
                    Anuncio anuncioSegundaOpcao = anuncios.get(0);

                    //Alterar anuncio do item
                    pedido = alterarAnuncioItemPedido(pedido, item, anuncioSegundaOpcao, true);

                    //TODO: Enviar para o comprador se deseja aceitar

                    //Persistir em banco
                    pedidoRepository.save(pedido);
                    return;
                }else{
                    System.out.println("Não há outro anúncio com esse produto");
                    //Cancelar item do pedido
                    this.cancelarItemPedido(pedido, item);
                    return;
                    /*
                    Lamentamos, mas por enquanto não temos mais opções desse produto.
                    Este item será removido do teu pedido.
                     */
                }
            }
        }else{
            System.out.println("Não há outro anúncio com esse produto");
            //Cancelar item do pedido
            this.cancelarItemPedido(pedido, item);
            return;
        }

    }

    //[interna] Cancelar item do pedido
    private Pedido cancelarItemPedido(Pedido pedido, ItemCarrinho item){
        for (ItemCarrinho i : pedido.getItensCarrinho()){
            if(i.getId() == item.getId()){
                i.setStatus(StatusItem.CANCELADO);
            }
        }
        return pedidoRepository.save(pedido);
    }

    //[interna] Substituir item no pedido
    private Pedido alterarAnuncioItemPedido(Pedido pedido, ItemCarrinho item, Anuncio anuncio,
                                            Boolean aConfirmar) throws InterruptedException {
        //Deixar 'Removido' item old
        for (ItemCarrinho i : pedido.getItensCarrinho()) {
            if(i.getId() == item.getId())
                i.setStatus(StatusItem.REMOVIDO);
        }

        int interacoes = item.getInteracoesRespostas();

        //Instanciar o item novo com o anuncio novo e deixar ele como "novo"
        ItemCarrinho itemNovo = new ItemCarrinho();
        itemNovo.setPedido(pedido);
        itemNovo.setQuantidade(item.getQuantidade());
        itemNovo.setId(0);
        itemNovo.setAnuncio(anuncio);
        itemNovo.setStatus(aConfirmar ? StatusItem.AGUARDANDO_CONFIRMACAO : StatusItem.PENDENTE);
        itemNovo.setDtResposta(null);
        itemNovo.setInteracoesRespostas(interacoes + 1);
        itemNovo.setDtPrazoResposta(LocalDateTime.now().plusMinutes(ApplicationConfig.PRAZO_RESPOSTA_PRODUTOR));

        //Salvar item
        itemCarrinhoRepository.save(itemNovo);

        //Atualizar prazo pedido
        pedido.setPrazoResposta(itemNovo.getDtPrazoResposta());

        //Add no pedido
        List<ItemCarrinho> itensNovos = pedido.getItensCarrinho();
        itensNovos.add(itemNovo);
        pedido.setItensCarrinho(itensNovos);
        pedido = pedidoRepository.save(pedido);

        //Chamar função async
//        if(!aConfirmar)
//            //TODO: Chamar função async para acompanhar prazo
            this.acompanharPrazoResposta(pedido.getId(), pedido.getPrazoResposta());

        return pedido;
    }




}
