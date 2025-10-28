package br.com.eltonriva.pedidos.api.publisher;

import br.com.eltonriva.pedidos.api.dto.PedidoEventDto;
import br.com.eltonriva.pedidos.api.dto.PedidoRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class PedidoPublisher {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    public PedidoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public record Published(String correlationId, PedidoEventDto evento) {
    }

    public Published publish(PedidoRequest req) {
        var correlationId = UUID.randomUUID().toString();
        var evento = new PedidoEventDto();
        evento.setId(UUID.randomUUID());
        evento.setCliente(req.cliente());
        evento.setEmailNotificacao(req.emailNotificacao());
        evento.setValorTotal(req.valorTotal());
        evento.setStatus("EM_PROCESSAMENTO");
        evento.setDataHora(OffsetDateTime.now());
        var itens = new ArrayList<PedidoEventDto.Item>();
        req.itemPedidos().forEach(i -> {
            var item = new PedidoEventDto.Item();
            item.setId(i.id() != null ? i.id() : UUID.randomUUID());
            var p = new PedidoEventDto.Produto();
            p.setId(i.produto().id() != null ? i.produto().id() : UUID.randomUUID());
            p.setNome(i.produto().nome());
            p.setValor(i.produto().valor());
            item.setProduto(p);
            item.setQuantidade(i.quantidade());
            itens.add(item);
        });
        evento.setItemPedidos(itens);
        this.rabbitTemplate.convertAndSend(this.exchangeName, "", evento, message -> {
            message.getMessageProperties().setHeader("x-correlation-id", correlationId);
            return message;
        });
        return new Published(correlationId, evento);
    }
}
