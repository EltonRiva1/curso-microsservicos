package br.com.eltonriva.pedidos.processador.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pedido_item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_item_id", nullable = false, updatable = false)
    private UUID eventItemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;
    @Column(nullable = false)
    private UUID produtoId;
    @Column(nullable = false)
    private String produtoNome;
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal produtoValor;
    @Column(nullable = false)
    private Integer quantidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getEventItemId() {
        return eventItemId;
    }

    public void setEventItemId(UUID eventItemId) {
        this.eventItemId = eventItemId;
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }

    public UUID getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(UUID produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public BigDecimal getProdutoValor() {
        return produtoValor;
    }

    public void setProdutoValor(BigDecimal produtoValor) {
        this.produtoValor = produtoValor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @PrePersist
    public void prePersistDefaults() {
        if (this.eventItemId == null) {
            this.eventItemId = UUID.randomUUID();
        }
        if (this.produtoId == null) {
            this.produtoId = UUID.randomUUID();
        }
        if (this.produtoValor == null) {
            this.produtoValor = BigDecimal.ZERO;
        }
        if (this.quantidade == null) {
            this.quantidade = 0;
        }
    }
}
