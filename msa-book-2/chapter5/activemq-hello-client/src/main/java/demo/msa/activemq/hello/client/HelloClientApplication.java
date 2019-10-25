package demo.msa.activemq.hello.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class HelloClientApplication {

  @Autowired
  private HelloClient helloClient;

  @PostConstruct
  public void init() {
//    helloClient.send("hello world");

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    for (int i = 0; i < 1000; i++) {
      helloClient.send("hello world");
    }

    stopWatch.stop();
    System.out.println("time: " + stopWatch.getTotalTimeMillis() + "ms"); // time: 56414ms
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloClientApplication.class, args).close();
  }
}
