package demo.msa.rabbitmq.hello.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "demo.msa.rabbitmq")
public class HelloClientApplication {

  @Autowired
  private HelloClient helloClient;

  @PostConstruct
  public void init() {
    String result = helloClient.send("world");
    System.out.println(result);
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloClientApplication.class, args).close();
  }
}
