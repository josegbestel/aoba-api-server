package com.redeaoba.api.service;

import com.redeaoba.api.exception.DomainException;
import com.redeaoba.api.model.ItemCarrinho;
import com.redeaoba.api.model.Pedido;
import com.redeaoba.api.model.enums.StatusItem;
import com.redeaoba.api.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AsyncPedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    PedidoRepository pedidoRepository;

    @Async
    public void acompanharPrazoResposta(long pedidoId, LocalDateTime prazo) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();

        long millis = ChronoUnit.MILLIS.between(now, prazo);

        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") > prazo: " + millis + " millis");
        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") >  Inicio da espera");
        Thread.sleep(millis);

        //Lógica do pedido aqui
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow((() -> new DomainException("Houve problema ao consultar o pedido " + pedidoId)));

        for (ItemCarrinho i : pedido.getItensCarrinho()) {
            //Se o item estiver fora do prazo
            if(i.getDtPrazoResposta().isBefore(prazo)){
                if(i.getStatus() == StatusItem.CONFIRMADO || i.getStatus() == StatusItem.BUSCANDO_OPCAO){
                    //TODO: Buscar opção
                }else{
                    i.setStatus(StatusItem.CANCELADO);
                    i.setDtResposta(now);
                }
            }
        }

        logger.info("acompanharPrazoResposta (pedido: " + pedidoId + ") > fim da espera");
    }
}
