package demo.msa.rabbitmq.hello.server;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloServerApplication {

  @Bean
  public Queue helloQueue() {
    return new Queue("hello-queue");
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloServerApplication.class, args);
  }
}
