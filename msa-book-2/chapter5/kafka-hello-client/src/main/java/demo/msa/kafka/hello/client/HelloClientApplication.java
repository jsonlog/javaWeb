package demo.msa.kafka.hello.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class HelloClientApplication {

  @Autowired
  private HelloClient helloClient;

  @PostConstruct
  public void init() {
    helloClient.send("hello world");
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloClientApplication.class, args).close();
  }
}
