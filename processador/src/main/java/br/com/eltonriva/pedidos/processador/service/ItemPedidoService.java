package br.com.eltonriva.pedidos.processador.service;

import br.com.eltonriva.pedidos.processador.entity.ItemPedido;
import br.com.eltonriva.pedidos.processador.entity.Pedido;
import br.com.eltonriva.pedidos.processador.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoService {
    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<ItemPedido> save(List<ItemPedido> itemPedidos) {
        return this.itemPedidoRepository.saveAll(itemPedidos);
    }

    public void updatedItemPedido(List<ItemPedido> itemPedidos, Pedido pedido) {
        itemPedidos.forEach(itemPedido -> {
            itemPedido.setPedido(pedido);
            this.save(itemPedido);
        });
    }

    public void save(ItemPedido itemPedido) {
        this.itemPedidoRepository.save(itemPedido);
    }
}
