package com.wit.challenge.calculator.resources;

import com.google.gson.Gson;
import com.wit.challenge.calculator.config.RabbitMQConfig;
import com.wit.challenge.calculator.enums.EnumOperator;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/rest", produces = "application/json")
public class CalculatorResource {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private UUID uuid;

    @GetMapping(value = "/sum")
    public ResponseEntity sum(@RequestParam String a, @RequestParam String b) {
        uuid = UUID.randomUUID();
        String response = getMessageResponse(getJSONObject(a, b, EnumOperator.SUM).toString(), uuid);
        return ResponseEntity.ok().header("X-Request-Id", uuid.toString()).body(response);
    }

    @GetMapping(value = "/subtract")
    public ResponseEntity subtract(@RequestParam String a, @RequestParam String b) {
        uuid = UUID.randomUUID();
        String response = getMessageResponse(getJSONObject(a, b, EnumOperator.SUBTRACT).toString(), uuid);
        return ResponseEntity.ok().header("X-Request-Id", uuid.toString()).body(response);
    }

    @GetMapping(value = "/multiply")
    public ResponseEntity multiply(@RequestParam String a, @RequestParam String b) {
        uuid = UUID.randomUUID();
        String response = getMessageResponse(getJSONObject(a, b, EnumOperator.MULTIPLY).toString(), uuid);
        return ResponseEntity.ok().header("X-Request-Id", uuid.toString()).body(response);
    }

    @GetMapping(value = "/division")
    public ResponseEntity division(@RequestParam String a, @RequestParam String b) {
        uuid = UUID.randomUUID();
        String response = getMessageResponse(getJSONObject(a, b, EnumOperator.DIVISION).toString(), uuid);
        return ResponseEntity.ok().header("X-Request-Id", uuid.toString()).body(response);
    }

    @GetMapping("/send")
    public ResponseEntity testMessage(String message) {
        JSONObject object = new JSONObject();
        object.put("message", message);
        uuid = UUID.randomUUID();
        String response = getMessageResponse(object.toString(), uuid);
        return ResponseEntity.ok().header("X-Request-Id", uuid.toString()).body(response);
    }

    private String getMessageResponse(String message, UUID uuid) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(uuid.toString());
        correlationData.setId(uuid.toString());
        Message newMessage = MessageBuilder
                .withBody(message.getBytes())
                .setReplyTo(RabbitMQConfig.RPC_REPLY_MESSAGE_QUEUE)
                .setHeader(MessageHeaders.ID, uuid)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        Object result = rabbitTemplate.convertSendAndReceive(RabbitMQConfig.RPC_EXCHANGE, RabbitMQConfig.RPC_MESSAGE_QUEUE, newMessage, correlationData);
        return new Gson().toJson(result);
    }

    private JSONObject getJSONObject(String a, String b, EnumOperator operator) {
        JSONObject object = new JSONObject();
        object.put("a", a);
        object.put("b", b);
        object.put("operator", operator.toString());
        return object;
    }
}
