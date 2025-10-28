package br.com.eltonriva.pedidos.notificacao.listener;

import br.com.eltonriva.pedidos.notificacao.dto.PedidoEventDto;
import br.com.eltonriva.pedidos.notificacao.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Listener que consome o evento "pedido criado" e dispara o envio de e-mail.
 */
@Component
public class PedidoListener {
    private static final Logger logger = LoggerFactory.getLogger(PedidoListener.class);
    private final EmailService emailService;

    public PedidoListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void enviarNotificacao(PedidoEventDto pedido, Message message, @Header(name = "x-correlation-id", required = false) String correlationId) {
        var corr = (correlationId != null) ? correlationId : message.getMessageProperties().getCorrelationId();
        logger.info("Evento recebido. pedidoId={}, correlationId={}", pedido.id(), corr);
        this.emailService.enviarEmail(pedido, corr);
        logger.info("Notificação enviada. pedidoId={}, correlationId={}", pedido.id(), corr);
    }
}
