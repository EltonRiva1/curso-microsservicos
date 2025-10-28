package br.com.eltonriva.pedidos.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Payload recebido no POST /api/v1/pedidos
 * IDs (item e produto) são OPCIONAIS. Se vierem nulos, o servidor gerará.
 */
public record PedidoRequest(@NotBlank String cliente, @Email @NotBlank String emailNotificacao,
                            @NotNull @Valid List<ItemRequest> itemPedidos, @NotNull @Positive BigDecimal valorTotal
) {
    public record ItemRequest(UUID id, @NotNull @Valid ProdutoRequest produto, @NotNull @Positive Integer quantidade
    ) {
    }

    public record ProdutoRequest(UUID id, @NotBlank String nome, @NotNull @Positive BigDecimal valor
    ) {
    }
}
