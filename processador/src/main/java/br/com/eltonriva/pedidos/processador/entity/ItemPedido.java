package br.com.eltonriva.pedidos.processador.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Entity
public class ItemPedido {
    @Id
    private UUID id = UUID.randomUUID();
    @ManyToOne
    private Produto produto;
    private Integer quantidade;
    @ManyToOne
    private Pedido pedido;

    public ItemPedido() {
    }

    public ItemPedido(UUID id, Produto produto, Integer quantidade, Pedido pedido) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.pedido = pedido;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id) && Objects.equals(produto, that.produto) && Objects.equals(quantidade, that.quantidade) && Objects.equals(pedido, that.pedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, produto, quantidade, pedido);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ItemPedido.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("produto=" + produto)
                .add("quantidade=" + quantidade)
                .add("pedido=" + pedido)
                .toString();
    }
}
