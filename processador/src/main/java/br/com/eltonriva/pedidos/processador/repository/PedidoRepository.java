package br.com.eltonriva.pedidos.processador.repository;

import br.com.eltonriva.pedidos.processador.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
}
