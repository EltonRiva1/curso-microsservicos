package br.com.eltonriva.pedidos.api.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do publisher RabbitMQ para o pedidos-api.
 * - Exchange FANOUT onde os eventos são publicados.
 * - RabbitTemplate com conversor JSON.
 * - RabbitAdmin para declarar o exchange (útil em dev).
 */
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    /**
     * Exchange fanout onde o 'pedidos-api' PUBLICA eventos.
     */
    @Bean
    public Exchange pedidosExchange() {
        return new FanoutExchange(this.exchangeName, true, false);
    }

    /**
     * Declara o exchange na subida (especialmente útil em ambientes locais).
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Conversor Jackson para serializar o evento em JSON.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template de publicação configurado com JSON.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        var rt = new RabbitTemplate(connectionFactory);
        rt.setMessageConverter(messageConverter);
        return rt;
    }
}
