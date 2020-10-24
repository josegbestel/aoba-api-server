package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.enums.OpcaoAlternativa;
import com.redeaoba.api.model.enums.StatusItem;
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

@Service
@Transactional
public class AsyncPedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
//    PedidoService pedidoService;

    @Async
    public void acompanharPrazoResposta(Long pedidoId, LocalDateTime prazo) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();

        long millis = ChronoUnit.MILLIS.between(now, prazo);

        LocalDateTime prazoDateTime = LocalDateTime.now().plusSeconds(millis/1000);
        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") => prazo: " + prazoDateTime + " [" + millis + " millis]");
        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") =>  Inicio da espera");
        Thread.sleep(millis);

//        Hibernate.initialize(Pedido.class);

        //Lógica do pedido aqui
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow((() -> new DomainException("Houve problema ao consultar o pedido " + pedidoId)));

        for (ItemCarrinho i : pedido.getItensPendentes()) {
            //Se o prazo do item for < do que agora
            if(i.getDtPrazoResposta().isBefore(LocalDateTime.now())){
                //Verifica se item teve resposta
                if(i.getDtResposta() == null){
                    //Item sem resposta
                    if(pedido.getOpcaoAlternativa() == OpcaoAlternativa.ACEITAR_SUGESTAO){
                        //Buscar alternativa
//                        pedidoService.buscarOpcaoItem(pedido, i);
                    }else{
                        //Cancelar item
                        i.setStatus(StatusItem.CANCELADO);
                    }
                }
            }

        }

        //Persistir em banco
        pedido.refreshStatus();
        pedidoRepository.save(pedido);

        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") => fim da espera");
    }
}
