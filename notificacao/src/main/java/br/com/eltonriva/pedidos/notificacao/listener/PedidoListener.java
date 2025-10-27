package br.com.eltonriva.pedidos.notificacao.listener;

import br.com.eltonriva.pedidos.notificacao.entity.Pedido;
import br.com.eltonriva.pedidos.notificacao.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {
    private final Logger logger = LoggerFactory.getLogger(PedidoListener.class);
    private final EmailService emailService;

    public PedidoListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void enviarNotificacao(Pedido pedido) {
        this.emailService.enviarEmail(pedido);
        logger.info("Notificação gerada: {}", pedido.toString());
    }
}
