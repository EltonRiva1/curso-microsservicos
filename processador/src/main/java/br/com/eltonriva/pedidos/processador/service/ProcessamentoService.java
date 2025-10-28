package br.com.eltonriva.pedidos.processador.service;

import br.com.eltonriva.pedidos.processador.dto.PedidoEventDto;

public interface ProcessamentoService {
    void processar(PedidoEventDto evento, String correlationId);
}
