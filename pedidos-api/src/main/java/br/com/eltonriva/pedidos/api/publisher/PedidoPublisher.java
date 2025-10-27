package br.com.eltonriva.pedidos.api.publisher;

import br.com.eltonriva.pedidos.api.entity.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoPublisher {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    public PedidoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar(Pedido pedido) {
        this.rabbitTemplate.convertAndSend(this.exchangeName, "", pedido);
    }
}
