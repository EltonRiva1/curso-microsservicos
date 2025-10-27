package br.com.eltonriva.pedidos.notificacao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.exchange.dlx.name}")
    private String exchangeDlxName;
    @Value("${rabbitmq.queue.dlq.name}")
    private String queueDlqName;

    /**
     * Exchange principal (fanout) onde os produtores publicam eventos de pedido.
     * Fanout distribui a mensagem para TODAS as filas vinculadas, ignorando routing key.
     */
    @Bean
    public FanoutExchange notificacaoExchange() {
        return new FanoutExchange(this.exchangeName);
    }

    /**
     * Fila de consumo do microsserviço de Notificação.
     * - Durable = true (sobrevive a restart do broker).
     * - x-dead-letter-exchange aponta para o exchange de DLX; mensagens rejeitadas/
     *   reprocessadas acima do limite vão para a DLQ.
     */
    @Bean
    public Queue notificacaoQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", this.exchangeDlxName);
        return new Queue(this.queueName, true, false, false, arguments);
    }

    /**
     * Liga a fila de notificação ao exchange fanout.
     * Em fanout, não há necessidade de routing key.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(this.notificacaoQueue()).to(this.notificacaoExchange());
    }

    /**
     * Admin do RabbitMQ responsável por DECLARAR automaticamente exchanges,
     * filas e bindings na subida do contexto.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Conversor de mensagens para JSON (Jackson).
     * Usado tanto pelo RabbitTemplate quanto pelos listeners.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template para envio de mensagens (se este serviço também publicar algo).
     * Configurado para serializar/deserializar como JSON.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    /**
     * Garante a inicialização do RabbitAdmin quando o contexto Spring estiver pronto,
     * forçando a declaração dos recursos no broker.
     */
    @Bean
    public ApplicationListener<?> applicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    /**
     * FÁBRICA dos containers de listeners (@RabbitListener).
     * - Aplica auto-config do Spring Boot (concurrency, ack, etc.).
     * - Define o MessageConverter JSON para desserializar o payload (ex.: Pedido).
     * IMPORTANTE: o nome do bean deve ser 'rabbitListenerContainerFactory'
     * para ser usado como padrão pelos @RabbitListener.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer, ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        var simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactoryConfigurer.configure(simpleRabbitListenerContainerFactory, connectionFactory);
        simpleRabbitListenerContainerFactory.setMessageConverter(messageConverter);
        return simpleRabbitListenerContainerFactory;
    }

    /**
     * Exchange de Dead Letter (DLX) para onde vão mensagens rejeitadas,
     * expiradas ou que excederam tentativas de reprocessamento.
     */
    @Bean
    public FanoutExchange notificacaoDlxExchange() {
        return new FanoutExchange(this.exchangeDlxName);
    }

    /**
     * Dead Letter Queue (DLQ) que recebe as mensagens roteadas pelo DLX.
     * Útil para análise/recuperação manual.
     */
    @Bean
    public Queue notificacaoDlqQueue() {
        return new Queue(this.queueDlqName);
    }

    /**
     * Binding da DLQ ao DLX (fanout).
     */
    @Bean
    public Binding bindingDlq() {
        return BindingBuilder.bind(this.notificacaoDlqQueue()).to(this.notificacaoDlxExchange());
    }
}
