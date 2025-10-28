package br.com.eltonriva.pedidos.notificacao.service;

import br.com.eltonriva.pedidos.notificacao.dto.PedidoEventDto;

public interface EmailService {
    void enviarEmail(PedidoEventDto pedido, String correlationId);
}
