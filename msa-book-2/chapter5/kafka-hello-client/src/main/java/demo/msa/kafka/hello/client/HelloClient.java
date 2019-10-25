package demo.msa.kafka.hello.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class HelloClient {

  @Autowired
  private KafkaTemplate kafkaTemplate;

  @SuppressWarnings("unchecked")
  public void send(String message) {
    kafkaTemplate.send("hello-topic", message);
  }
}
