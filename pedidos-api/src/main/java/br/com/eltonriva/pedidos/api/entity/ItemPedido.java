package br.com.eltonriva.pedidos.api.entity;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class ItemPedido {
    private UUID id = UUID.randomUUID();
    private Produto produto;
    private Integer quantidade;

    public ItemPedido() {
    }

    public ItemPedido(UUID id, Produto produto, Integer quantidade) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id) && Objects.equals(produto, that.produto) && Objects.equals(quantidade, that.quantidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, produto, quantidade);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ItemPedido.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("produto=" + produto)
                .add("quantidade=" + quantidade)
                .toString();
    }
}
