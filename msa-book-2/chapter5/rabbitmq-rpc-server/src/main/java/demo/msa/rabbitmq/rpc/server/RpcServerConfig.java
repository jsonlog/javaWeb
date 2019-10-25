package demo.msa.rabbitmq.rpc.server;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcServerConfig {

  @Value("${rabbitmq.queue-name:rpc-queue}")
  private String queueName;

  @Value("${rabbitmq.exchange-name:rpc-exchange}")
  private String exchangeName;

  @Value("${rabbitmq.routing-key:rpc}")
  private String routingKey;

  @Bean
  public Queue queue() {
    return new Queue(queueName);
  }

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(exchangeName);
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
