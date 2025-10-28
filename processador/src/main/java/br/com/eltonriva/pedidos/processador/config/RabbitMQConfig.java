package br.com.eltonriva.pedidos.processador.config;

import org.springframework.amqp.core.*;
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

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.exchange.dlx.name:}")
    private String dlxName;
    @Value("${rabbitmq.queue.dlq.name:}")
    private String dlqName;

    @Bean
    public FanoutExchange pedidosExchange() {
        return new FanoutExchange(this.exchangeName, true, false);
    }

    @Bean
    public Queue processadorQueue() {
        Map<String, Object> args = (this.dlxName == null || this.dlxName.isBlank())
                ? Map.of()
                : Map.of("x-dead-letter-exchange", this.dlxName);
        return new Queue(this.queueName, true, false, false, args);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(this.processadorQueue()).to(this.pedidosExchange());
    }

    @Bean
    public Declarables optionalDlx() {
        if (this.dlxName == null || this.dlxName.isBlank() || this.dlqName == null || this.dlqName.isBlank()) {
            return new Declarables();
        }
        var dlx = new FanoutExchange(this.dlxName, true, false);
        var dlq = new Queue(this.dlqName, true);
        var bind = BindingBuilder.bind(dlq).to(dlx);
        return new Declarables(dlx, dlq, bind);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer cfg, ConnectionFactory cf, MessageConverter messageConverter) {
        var f = new SimpleRabbitListenerContainerFactory();
        cfg.configure(f, cf);
        f.setMessageConverter(messageConverter);
        return f;
    }
}
