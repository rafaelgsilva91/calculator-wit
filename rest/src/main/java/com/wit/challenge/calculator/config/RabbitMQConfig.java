package com.wit.challenge.calculator.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String RPC_MESSAGE_QUEUE = "calculator_msg_queue";
    public static final String RPC_REPLY_MESSAGE_QUEUE = "calculator_reply_msg_queue";
    public static final String RPC_EXCHANGE = "calculator_rpc_exchange";

    @Bean
    Queue msgQueue() {
        return new Queue(RPC_MESSAGE_QUEUE);
    }

    @Bean
    Queue replyQueue() {
        return new Queue(RPC_REPLY_MESSAGE_QUEUE);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(RPC_EXCHANGE);
    }

    @Bean
    Binding msgBinding() {
        return BindingBuilder.bind(msgQueue()).to(exchange()).with(RPC_MESSAGE_QUEUE);
    }

    @Bean
    Binding replyBinding() {
        return BindingBuilder.bind(replyQueue()).to(exchange()).with(RPC_REPLY_MESSAGE_QUEUE);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(producerJackson2MessageConverter());
        template.setReplyTimeout(6000);
        template.setReplyAddress(RPC_REPLY_MESSAGE_QUEUE);
        template.setUseDirectReplyToContainer(false);
        return template;
    }

    @Bean
    SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RPC_REPLY_MESSAGE_QUEUE);
        container.setMessageListener(rabbitTemplate(connectionFactory));
        return container;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

