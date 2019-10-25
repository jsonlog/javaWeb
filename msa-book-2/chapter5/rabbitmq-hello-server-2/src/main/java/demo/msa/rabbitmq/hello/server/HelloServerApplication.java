package demo.msa.rabbitmq.hello.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "demo.msa.rabbitmq")
public class HelloServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(HelloServerApplication.class, args);
  }
}
