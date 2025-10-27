package br.com.eltonriva.pedidos.processador.entity;

import br.com.eltonriva.pedidos.processador.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;
@Entity
public class Pedido {
    @Id
    private UUID id = UUID.randomUUID();
    private String cliente, emailNotificacao;
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itemPedidos = new ArrayList<>();
    private Double valorTotal;
    @Enumerated(EnumType.STRING)
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora = LocalDateTime.now();

    public Pedido() {
    }

    public Pedido(UUID id, String cliente, String emailNotificacao, List<ItemPedido> itemPedidos, Double valorTotal, Status status, LocalDateTime dataHora) {
        this.id = id;
        this.cliente = cliente;
        this.emailNotificacao = emailNotificacao;
        this.itemPedidos = itemPedidos;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataHora = dataHora;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<ItemPedido> getItemPedidos() {
        return itemPedidos;
    }

    public void setItemPedidos(List<ItemPedido> itemPedidos) {
        this.itemPedidos = itemPedidos;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id) && Objects.equals(cliente, pedido.cliente) && Objects.equals(emailNotificacao, pedido.emailNotificacao) && Objects.equals(itemPedidos, pedido.itemPedidos) && Objects.equals(valorTotal, pedido.valorTotal) && status == pedido.status && Objects.equals(dataHora, pedido.dataHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cliente, emailNotificacao, itemPedidos, valorTotal, status, dataHora);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Pedido.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cliente='" + cliente + "'")
                .add("emailNotificacao='" + emailNotificacao + "'")
                .add("itemPedidos=" + itemPedidos)
                .add("valorTotal=" + valorTotal)
                .add("status=" + status)
                .add("dataHora=" + dataHora)
                .toString();
    }
}
