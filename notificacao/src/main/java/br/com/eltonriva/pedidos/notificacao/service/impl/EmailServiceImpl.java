package br.com.eltonriva.pedidos.notificacao.service.impl;

import br.com.eltonriva.pedidos.notificacao.dto.PedidoEventDto;
import br.com.eltonriva.pedidos.notificacao.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Envio de e-mail baseado no evento recebido.
 * Inclui idempotência simples em memória para evitar duplicidade em caso de reentregas.
 */
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender javaMailSender;
    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void enviarEmail(PedidoEventDto pedido, String correlationId) {
        var key = pedido.id() + ":" + (correlationId != null ? correlationId : "");
        if (!this.processed.add(key)) {
            logger.warn("Mensagem duplicada ignorada. key={}", key);
            return;
        }
        var msg = new SimpleMailMessage();
        msg.setFrom("pedidos-api@company.com");
        msg.setTo(pedido.emailNotificacao());
        msg.setSubject("Confirmação de Pedido");
        msg.setText("""
                Olá, %s!

                Seu pedido %s foi criado com sucesso.
                Valor total: R$ %s
                Status: %s

                (correlationId: %s)
                """.formatted(
                pedido.cliente(),
                pedido.id(),
                pedido.valorTotal(),
                pedido.status(),
                correlationId));
        this.javaMailSender.send(msg);
    }
}
