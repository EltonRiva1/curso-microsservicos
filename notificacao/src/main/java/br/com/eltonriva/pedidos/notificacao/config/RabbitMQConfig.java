package br.com.eltonriva.pedidos.notificacao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Configuração do RabbitMQ para o serviço de Notificação.
 * - Exchange fanout principal e DLX (dead-letter).
 * - Fila principal com DLX configurado e sua respectiva DLQ.
 * - Conversor JSON para mensagens.
 * - Factory padrão para @RabbitListener.
 */
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
     * Exchange principal (fanout).
     */
    @Bean
    public FanoutExchange notificacaoExchange() {
        return new FanoutExchange(this.exchangeName, true, false);
    }

    /**
     * Fila principal com DLX configurado.
     */
    @Bean
    public Queue notificacaoQueue() {
        return new Queue(this.queueName, true, false, false, Map.of("x-dead-letter-exchange", this.exchangeDlxName));
    }

    /**
     * Binding fila ↔ exchange principal.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(this.notificacaoQueue()).to(this.notificacaoExchange());
    }

    /**
     * Exchange e fila de Dead Letter (DLX/DLQ).
     */
    @Bean
    public FanoutExchange notificacaoDlxExchange() {
        return new FanoutExchange(this.exchangeDlxName, true, false);
    }

    @Bean
    public Queue notificacaoDlqQueue() {
        return new Queue(this.queueDlqName, true);
    }

    @Bean
    public Binding bindingDlq() {
        return BindingBuilder.bind(this.notificacaoDlqQueue()).to(this.notificacaoDlxExchange());
    }

    /**
     * Declara recursos automaticamente no broker na subida da aplicação.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Conversor JSON (Jackson).
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Fábrica padrão dos @RabbitListener com JSON.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
