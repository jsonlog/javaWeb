package demo.msa.rabbitmq.hello.server;

import demo.msa.rabbitmq.rpc.server.RpcReceiver;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloServer implements RpcReceiver<String, String> {

  @Override
  @RabbitListener(queues = "rpc-queue")
  public String receive(String message) {
    return "hello " + message;
  }
}
