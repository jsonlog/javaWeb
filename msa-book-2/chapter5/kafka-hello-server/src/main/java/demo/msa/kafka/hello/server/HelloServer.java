package demo.msa.kafka.hello.server;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HelloServer {

  @KafkaListener(topics = "hello-topic")
  public void receive(String message) {
    System.out.println(message);
  }
}
