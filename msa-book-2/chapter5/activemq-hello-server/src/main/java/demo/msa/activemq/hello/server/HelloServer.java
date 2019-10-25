package demo.msa.activemq.hello.server;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class HelloServer {

  @JmsListener(destination = "hello-queue")
  public void receive(String message) {
    System.out.println(message);
  }
}
