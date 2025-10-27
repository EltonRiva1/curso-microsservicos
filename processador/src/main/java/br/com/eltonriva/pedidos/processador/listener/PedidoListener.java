package br.com.eltonriva.pedidos.processador.listener;

import br.com.eltonriva.pedidos.processador.entity.Pedido;
import br.com.eltonriva.pedidos.processador.entity.enums.Status;
import br.com.eltonriva.pedidos.processador.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {
    private final PedidoService pedidoService;

    public PedidoListener(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void salvarPedido(Pedido pedido) {
        pedido.setStatus(Status.PROCESSADO);
        this.pedidoService.save(pedido);
    }
}
