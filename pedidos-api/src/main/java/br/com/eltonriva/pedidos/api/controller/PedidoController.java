package br.com.eltonriva.pedidos.api.controller;

import br.com.eltonriva.pedidos.api.dto.PedidoRequest;
import br.com.eltonriva.pedidos.api.publisher.PedidoPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Recurso para criar um novo pedido")
public class PedidoController {
    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    private final PedidoPublisher pedidoPublisher;

    public PedidoController(PedidoPublisher pedidoPublisher) {
        this.pedidoPublisher = pedidoPublisher;
    }

    @PostMapping
    @Operation(
            summary = "Cria um novo pedido",
            description = "Valida o payload, completa IDs ausentes, publica no RabbitMQ e retorna o evento criado",
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Recurso criado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoRequest.class))
            )
    )
    public ResponseEntity<?> criar(@Valid @RequestBody PedidoRequest body) {
        logger.info("Recebido novo pedido para cliente={} itens={}", body.cliente(), body.itemPedidos().size());
        var pub = this.pedidoPublisher.publish(body);
        return ResponseEntity
                .created(null)
                .header("x-correlation-id", pub.correlationId())
                .body(pub.evento());
    }
}
