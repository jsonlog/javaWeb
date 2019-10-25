package demo.msa.grpc.hello.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "demo.msa.grpc")
public class HelloServerApplication implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloServerApplication.class, args);
  }
}
