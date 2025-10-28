package br.com.eltonriva.pedidos.processador.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido", uniqueConstraints = {
        @UniqueConstraint(name = "uk_pedido_event_id", columnNames = "event_id")
})
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID eventId;
    @Column(nullable = false)
    private String cliente;
    @Column(nullable = false)
    private String emailNotificacao;
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valorTotal;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private OffsetDateTime dataHora;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> itens = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEmailNotificacao() {
        return emailNotificacao;
    }

    public void setEmailNotificacao(String emailNotificacao) {
        this.emailNotificacao = emailNotificacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public List<ItemEntity> getItens() {
        return itens;
    }

    public void setItens(List<ItemEntity> itens) {
        this.itens = itens;
    }

    @PrePersist
    public void prePersistDefaults() {
        if (this.eventId == null) {
            this.eventId = UUID.randomUUID();
        }
        if (this.dataHora == null) {
            this.dataHora = OffsetDateTime.now();
        }
        if (this.status == null) {
            this.status = "PROCESSADO";
        }
        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
    }
}
