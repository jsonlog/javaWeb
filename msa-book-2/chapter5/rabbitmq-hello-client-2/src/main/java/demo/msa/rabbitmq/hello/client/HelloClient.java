package demo.msa.rabbitmq.hello.client;

import demo.msa.rabbitmq.rpc.client.RpcSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloClient {

  @Autowired
  private RpcSender<String, String> rpcSender;

  public String send(String message) {
    return rpcSender.send(message);
  }
}
