package br.com.eltonriva.pedidos.processador.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoEventDto(UUID id, String cliente, String emailNotificacao, BigDecimal valorTotal, String status,
                             OffsetDateTime dataHora, List<ItemDto> itemPedidos
) {
    public record ItemDto(UUID id, ProdutoDto produto, Integer quantidade
    ) {
    }

    public record ProdutoDto(UUID id, String nome, BigDecimal valor
    ) {
    }
}
