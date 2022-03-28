package com.wit.challenge.calculator.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}