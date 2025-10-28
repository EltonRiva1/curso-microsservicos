package br.com.eltonriva.pedidos.processador.service.impl;

import br.com.eltonriva.pedidos.processador.dto.PedidoEventDto;
import br.com.eltonriva.pedidos.processador.entity.ItemEntity;
import br.com.eltonriva.pedidos.processador.entity.PedidoEntity;
import br.com.eltonriva.pedidos.processador.repository.PedidoRepository;
import br.com.eltonriva.pedidos.processador.service.ProcessamentoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ProcessamentoServiceImpl implements ProcessamentoService {
    public static final String PROCESSADO = "PROCESSADO";
    private final PedidoRepository pedidoRepository;

    public ProcessamentoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    @Override
    public void processar(PedidoEventDto dto, String correlationId) {
        var pedido = new PedidoEntity();
        pedido.setEventId(safeUuid(correlationId, UUID.randomUUID()));
        pedido.setCliente(dto.cliente());
        pedido.setEmailNotificacao(dto.emailNotificacao());
        if (dto.valorTotal() != null)
            pedido.setValorTotal(new BigDecimal(dto.valorTotal().toString()));
        pedido.setStatus(PROCESSADO);
        pedido.setDataHora(dto.dataHora() != null ? dto.dataHora() : OffsetDateTime.now());
        var itens = new ArrayList<ItemEntity>();
        if (dto.itemPedidos() != null) {
            dto.itemPedidos().forEach(i -> {
                var item = new ItemEntity();
                UUID eventItemId = i.id() != null
                        ? i.id()
                        : this.safeUuid(correlationId, UUID.randomUUID());
                item.setEventItemId(eventItemId);
                if (i.produto() != null) {
                    item.setProdutoId(i.produto().id() != null ? i.produto().id() : UUID.randomUUID());
                    item.setProdutoNome(i.produto().nome());
                    if (i.produto().valor() != null)
                        item.setProdutoValor(new BigDecimal(i.produto().valor().toString()));
                }
                item.setQuantidade(i.quantidade() != null ? i.quantidade() : 0);
                item.setPedido(pedido);
                itens.add(item);
            });
        }
        pedido.setItens(itens);
        this.pedidoRepository.save(pedido);
    }

    private UUID safeUuid(String maybeUuid, UUID fallback) {
        try {
            return (maybeUuid != null) ? UUID.fromString(maybeUuid) : fallback;
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }
}
