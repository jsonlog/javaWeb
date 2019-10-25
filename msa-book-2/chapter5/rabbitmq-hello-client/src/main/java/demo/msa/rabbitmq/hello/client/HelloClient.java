package demo.msa.rabbitmq.hello.client;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloClient {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void send(String message) {
    rabbitTemplate.convertAndSend("hello-queue", message);
  }

  public void send(Pojo pojo) {
    rabbitTemplate.convertAndSend("hello-queue", pojo);
  }
}
