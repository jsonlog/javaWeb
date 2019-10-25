package demo.msa.okhttp.hello.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloClientApplication implements CommandLineRunner {

  @Autowired
  private HelloClient helloClient;

  @Override
  public void run(String... args) throws Exception {
    System.out.println(helloClient.say1("world"));
    System.out.println(helloClient.say2("world"));
    System.out.println(helloClient.say3("world"));
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloClientApplication.class, args).close();
  }
}
