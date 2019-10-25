package demo.msa.rabbitmq.rpc.client;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RpcSender<I, O> {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private DirectExchange exchange;

  @Value("${rabbitmq.routing-key:rpc}")
  private String routingKey;

  @SuppressWarnings("unchecked")
  public O send(I message) {
    return (O) rabbitTemplate.convertSendAndReceive(exchange.getName(), routingKey, message);
  }
}
