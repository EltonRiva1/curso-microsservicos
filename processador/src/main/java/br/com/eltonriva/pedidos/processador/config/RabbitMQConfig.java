package br.com.eltonriva.pedidos.processador.config;

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

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.queue.name}")
    private String queueName;

    /**
     * Exchange fanout de onde este serviço receberá eventos.
     */
    @Bean
    public FanoutExchange processadorExchange() {
        return new FanoutExchange(this.exchangeName);
    }

    /**
     * Fila específica do 'processador' (cada serviço pode ter a sua).
     */
    @Bean
    public Queue processadorQueue() {
        return new Queue(this.queueName);
    }

    /**
     * Binding da fila do 'processador' ao exchange fanout.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(this.processadorQueue()).to(this.processadorExchange());
    }

    /**
     * Admin do RabbitMQ para declarar recursos ao iniciar.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Conversor Jackson para JSON.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template para enviar mensagens (se necessário).
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    /**
     * Inicialização do RabbitAdmin no start da aplicação.
     */
    @Bean
    public ApplicationListener<?> applicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    /**
     * Fábrica dos listeners (@RabbitListener) deste serviço,
     * usando o conversor JSON para desserializar os eventos recebidos.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer, ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        var simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactoryConfigurer.configure(simpleRabbitListenerContainerFactory, connectionFactory);
        simpleRabbitListenerContainerFactory.setMessageConverter(messageConverter);
        return simpleRabbitListenerContainerFactory;
    }
}
