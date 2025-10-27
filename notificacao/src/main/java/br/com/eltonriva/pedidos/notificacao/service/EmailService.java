package br.com.eltonriva.pedidos.notificacao.service;

import br.com.eltonriva.pedidos.notificacao.entity.Pedido;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarEmail(Pedido pedido) {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("pedidos-api@company.com");
        simpleMailMessage.setTo(pedido.getEmailNotificacao());
        simpleMailMessage.setSubject("Pedido de Compra");
        simpleMailMessage.setText(this.gerarMensagem(pedido));
        this.javaMailSender.send(simpleMailMessage);
    }

    private String gerarMensagem(Pedido pedido) {
        var pedidoId = pedido.getId().toString();
        var cliente = pedido.getCliente();
        var valor = String.valueOf(pedido.getValorTotal());
        var status = pedido.getStatus().name();
        return String.format("Olá, %s, seu pedido de número %s no valor de %s, foi realizado com sucesso.\nStatus: %s.", cliente, pedidoId, valor, status);
    }
}
