package demo.msa.rabbitmq.rpc.client;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcClientConfig {

  @Value("${rabbitmq.exchange-name:rpc-exchange}")
  private String exchangeName;

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(exchangeName);
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
