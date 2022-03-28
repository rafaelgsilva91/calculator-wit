package com.wit.challenge.calculator.consumer;

import com.wit.challenge.calculator.config.RabbitMQConfig;
import com.wit.challenge.calculator.enums.EnumOperator;
import com.wit.challenge.calculator.services.CalculatorService;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RpcServerController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CalculatorService calculatorService;

    @RabbitListener(queues = RabbitMQConfig.RPC_MESSAGE_QUEUE, concurrency = "3")
    public void process(Message message) {
        Message build = MessageBuilder
                .withBody(getResults(message).getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        CorrelationData correlationData = new CorrelationData(message.getMessageProperties().getCorrelationId());
        rabbitTemplate.convertSendAndReceive(RabbitMQConfig.RPC_EXCHANGE, RabbitMQConfig.RPC_REPLY_MESSAGE_QUEUE, build, correlationData);
    }

    private String getResults(Message message) {
        try {
            JSONObject jsonObject = new JSONObject(new String(message.getBody()));
            if (jsonObject.has("operator")) {
                EnumOperator operator = EnumOperator.valueOf(jsonObject.getString("operator"));
                if (operator == EnumOperator.SUM) {
                    jsonObject.put("operator", EnumOperator.SUM);
                    jsonObject.put("result", calculatorService.processCalc(jsonObject.getString("a"), jsonObject.getString("b"), EnumOperator.SUM));
                    return jsonObject.toString();
                } else if (operator == EnumOperator.SUBTRACT) {
                    jsonObject.put("operator", EnumOperator.SUBTRACT);
                    jsonObject.put("result", calculatorService.processCalc(jsonObject.getString("a"), jsonObject.getString("b"), EnumOperator.SUBTRACT));
                    return jsonObject.toString();
                } else if (operator == EnumOperator.MULTIPLY) {
                    jsonObject.put("operator", EnumOperator.MULTIPLY);
                    jsonObject.put("result", calculatorService.processCalc(jsonObject.getString("a"), jsonObject.getString("b"), EnumOperator.MULTIPLY));
                    return jsonObject.toString();
                } else if (operator == EnumOperator.DIVISION) {
                    jsonObject.put("operator", EnumOperator.DIVISION);
                    jsonObject.put("result", calculatorService.processCalc(jsonObject.getString("a"), jsonObject.getString("b"), EnumOperator.DIVISION));
                    return jsonObject.toString();
                }
            } else if (jsonObject.has("message")) {
                return jsonObject.put("message", "I am the server, I received the message from the client: " + jsonObject.getString("message")).toString();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }
}