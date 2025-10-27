package br.com.eltonriva.pedidos.processador.service;

import br.com.eltonriva.pedidos.processador.entity.ItemPedido;
import br.com.eltonriva.pedidos.processador.entity.Pedido;
import br.com.eltonriva.pedidos.processador.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    private final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ItemPedidoService itemPedidoService;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService, ItemPedidoService itemPedidoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
    }

    public void save(Pedido pedido) {
        this.produtoService.save(pedido.getItemPedidos());
        List<ItemPedido> itemPedidos = this.itemPedidoService.save(pedido.getItemPedidos());
        this.pedidoRepository.save(pedido);
        this.itemPedidoService.updatedItemPedido(itemPedidos, pedido);
        logger.info("Pedido salvo: {}", pedido.toString());
    }
}
