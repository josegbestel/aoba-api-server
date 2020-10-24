package com.redeaoba.api.service;

import com.redeaoba.api.config.ApplicationConfig;
import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.exception.NotFoundException;
import com.redeaoba.api.model.*;
import com.redeaoba.api.model.enums.OpcaoAlternativa;
import com.redeaoba.api.model.enums.StatusAnuncio;
import com.redeaoba.api.model.enums.StatusItem;
import com.redeaoba.api.model.enums.StatusPedido;
import com.redeaoba.api.model.representationModel.*;
import com.redeaoba.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

//    @Autowired
//    AsyncPedidoService asyncPedidoService;

    @Autowired
    ItemCarrinhoRepository itemCarrinhoRepository;

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

    //Criar novo
    public PedidoRealizadoModel create(PedidoNovoModel pedidoNovoModel) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();

        //Recusar pedidos das 20h às 12h
//        if(now.getHour() >= 20 || now.getHour() <= 11){
//            throw new DomainException("Pedidos nao podem ser realizados depois das 20h. " +
//                    "Deixe salvo esse carrinho e finalize amanha depois das 12h");
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

        //TODO: chama uma função para qnd vencer e n tiver resposta
//        asyncPedidoService.acompanharPrazoResposta(pedido.getId(), pedido.getPrazoResposta());

        //TODO: Enviar notificação para produtores

        return PedidoRealizadoModel.toModel(pedido);
    }

    //Obter por Comerciante
    public List<PedidoRealizadoModel> readByCompradorId(long compradorId){
        if(!comercianteRepository.existsById(compradorId)){
            throw new NotFoundException("Comerciante nao localizado");
        }
        List<Pedido> pedidos = pedidoRepository.findByCompradorId(compradorId)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));

        //Remover itens cancelados ou removidos dos pedidos
        for (Pedido p : pedidos){
            p.setItensCarrinho(p.getItensCarrinhoAtivos());
        }

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

        //Remover itens cancelados ou removidos dos pedidos
        for (Pedido p : pedidosProdutor){
            p.setItensCarrinho(p.getItensCarrinhoAtivos());
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

        //Remover itens cancelados ou removidos dos pedidos
        for (Pedido p : pedidos){
            p.setItensCarrinho(p.getItensCarrinhoAtivos());
        }

        //Remover itens aguardando confirmação
        for(int i = 0; i < pedidos.size(); i++){
            Pedido p = pedidos.get(i);

            boolean itemAguardando = false;
            for (ItemCarrinho item : p.getItensCarrinho()) {
                if(!itemAguardando)
                    if(item.getStatus() == StatusItem.AGUARDANDO_CONFIRMACAO)
                        itemAguardando = true;
            }
            //Se tiver pelo menos 1 item aguardando confirmação, remover da lista
            if(itemAguardando){
                pedidos.remove(i);
            }
        }

        //Transformar os pedidos em representation model
        List<PedidoProdutorNovoModel> novos = new ArrayList<>();
        for(Pedido p : pedidos){
            List<ItemCarrinho> itensProdutor = p.getItensCarrinhoByProdutorId(produtorId);
            if(itensProdutor.size() >0){
                if(itensProdutor.get(0).getDtResposta() == null){
                    p.setItensCarrinho(itensProdutor);
                    novos.add(PedidoProdutorNovoModel.toModel(p));
                }
            }
        }

        if(novos.size() == 0){
            throw new NotFoundException("Nao ha nenhum pedido para este produtor");
        }

        return novos;
    }

    //Responder produtor
    public void updatePedidoResponder(long pedidoId, long produtorId, boolean aceite) throws InterruptedException {
        Produtor produtor = produtorRepository.findById(produtorId)
                .orElseThrow((() -> new NotFoundException("Produtor nao localizado")));

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));

        if(pedido.getItensCarrinhoByProdutorId(produtorId).size() == 0){
            throw new DomainException("Este produtor nao tem nenhum item neste pedido");
        }

        List<ItemCarrinho> itensProdutor = pedido.getItensCarrinhoByProdutorId(produtorId);

        //Validar se ja foram respondidos
        int qtdeRespondidos = 0;
        for (ItemCarrinho i : itensProdutor) {
            if(i.getDtResposta() != null)
                qtdeRespondidos++;
        }
        if(qtdeRespondidos == itensProdutor.size()){
            throw new DomainException("Este produtor ja respondeu este pedido");
        }

        if(aceite){
            pedido.confirmItensByProdutorId(produtorId);
            pedidoRepository.save(pedido);
        }else{
            pedido.rejectItensByProdutorId(produtorId);
            pedidoRepository.save(pedido);

            if(pedido.getOpcaoAlternativa() == OpcaoAlternativa.ACEITAR_SUGESTAO){
                //Buscar outra opção
                for(ItemCarrinho itemProdutor : itensProdutor){
                    buscarOpcaoItem(pedido, itemProdutor);
                }
            }else if(pedido.getOpcaoAlternativa() == OpcaoAlternativa.CANCELAR_PRODUTO){
                pedido.cancelItensByProdutorId(produtorId);
                pedidoRepository.save(pedido);
            }else{
                throw new DomainException("O pedido nao tem opcao alternativa definida");
            }
        }
    }

    //Pedir confirmação do comprador para segunda opção c/ preço superior
    public void confirmOpcaoAlternativa(long idPedido, long idItem, boolean confirmacao) throws InterruptedException {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));

        for (ItemCarrinho i : pedido.getItensCarrinho()) {
            if(i.getId() == idItem){
                if(confirmacao){
                    i.setStatus(StatusItem.PENDENTE);
                    //TODO: Enviar notificação para produtor
                }
            }
        }
        pedidoRepository.save(pedido);
        //TODO: Chamar método async para acompanhar prazo
//        asyncPedidoService.acompanharPrazoResposta(pedido.getId(), pedido.getPrazoResposta());
    }

    //Entregar pedido
    public void updatePedidoEntregar(long pedidoId){
        LocalDateTime now = LocalDateTime.now();

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));
        pedido.setStatus(StatusPedido.ENTREGUE);
        pedido.setDtEntrega(now);

        for (ItemCarrinho i : pedido.getItensCarrinho()) {
            if(i.getStatus() == StatusItem.PENDENTE){
                i.setStatus(StatusItem.FINALIZADO);
                i.setDtResposta(now);
            }
        }

        pedidoRepository.save(pedido);
    }

    //Reprocessar segunda opção
    public void reprocessarOpcaoALternativa(long pedidoId, long itemId) throws InterruptedException {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido nao localizado"));

        for (ItemCarrinho i : pedido.getItensCarrinho()) {
            if(i.getId() == itemId){
                buscarOpcaoItem(pedido, i);
            }
        }
    }

    //[interna] Calcular o frete
    private float calcularFrete(Pedido pedido){
        //TODO: Implementar algorítmo para calcular frete
        return 50;
    }

    //[interna] Definir variáveis padrões ao criar um pedido
    private Pedido defaultSetingsNovoPedido(Pedido pedido){
        LocalDateTime now = LocalDateTime.now();

        //Define a data de criação
        pedido.setDtCriacao(now);
        //Define o prazo default (30min para responder)
        pedido.setPrazoResposta(now.plusMinutes(ApplicationConfig.PRAZO_RESPOSTA_PRODUTOR));
        //Define o status inicial
        pedido.setStatus(StatusPedido.REALIZADO);
        //Definir valor do frete
        pedido.setValorFrete(calcularFrete(pedido));
        //Define o prazo de cada item do pedido
        pedido.getItensCarrinho().forEach(i -> i.setDtPrazoResposta(pedido.getPrazoResposta()));

        return pedido;
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
//            asyncPedidoService.acompanharPrazoResposta(pedido.getId(), pedido.getPrazoResposta());

        return pedido;
    }

    //[interna] Buscar opção alternativa
    public void buscarOpcaoItem(Pedido pedido, ItemCarrinho item) throws InterruptedException {

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




}
