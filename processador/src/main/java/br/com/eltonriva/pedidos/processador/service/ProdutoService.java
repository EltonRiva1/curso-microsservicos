package br.com.eltonriva.pedidos.processador.service;

import br.com.eltonriva.pedidos.processador.entity.ItemPedido;
import br.com.eltonriva.pedidos.processador.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void save(List<ItemPedido> itemPedidos) {
        itemPedidos.forEach(itemPedido -> this.produtoRepository.save(itemPedido.getProduto()));
    }
}
