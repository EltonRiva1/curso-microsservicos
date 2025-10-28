package br.com.eltonriva.pedidos.processador.listener;

import br.com.eltonriva.pedidos.processador.dto.PedidoEventDto;
import br.com.eltonriva.pedidos.processador.service.ProcessamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {
    private final ProcessamentoService processamentoService;

    public PedidoListener(ProcessamentoService processamentoService) {
        this.processamentoService = processamentoService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumir(PedidoEventDto dto, @Header(name = "x-correlation-id", required = false) String correlationId) {
        this.processamentoService.processar(dto, correlationId);
    }
}
