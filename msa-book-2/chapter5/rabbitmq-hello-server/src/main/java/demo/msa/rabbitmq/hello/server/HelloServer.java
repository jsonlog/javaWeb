package demo.msa.rabbitmq.hello.server;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloServer {

  @RabbitListener(queues = "hello-queue")
  public void receive(String message) {
    System.out.println(message);
  }

  @RabbitListener(queues = "hello-queue")
  public void receive(Pojo pojo) {
    System.out.println(pojo);
  }
}
