package br.com.eltonriva.pedidos.processador.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
@Entity
public class Produto {
    @Id
    private UUID id = UUID.randomUUID();
    private Double valor;
    private String nome;

    public Produto() {
    }

    public Produto(UUID id, Double valor, String nome) {
        this.id = id;
        this.valor = valor;
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id) && Objects.equals(valor, produto.valor) && Objects.equals(nome, produto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, nome);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Produto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("valor=" + valor)
                .add("nome='" + nome + "'")
                .toString();
    }
}
