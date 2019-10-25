package demo.msa.grpc.hello.client;

import demo.msa.grpc.hello.api.HelloRequest;
import demo.msa.grpc.hello.api.HelloResponse;
import demo.msa.grpc.hello.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloClient {

  private static final String host = "localhost";
  private static final int port = 50000;

  public static void main(String[] args) throws Exception {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext(true)
        .build();
    try {
      HelloServiceGrpc.HelloServiceBlockingStub helloService = HelloServiceGrpc.newBlockingStub(channel);
      HelloRequest request = HelloRequest
          .newBuilder()
          .setName("world")
          .build();
      HelloResponse response = helloService.say(request);
      System.out.println(response.getMessage());
    } finally {
      channel.shutdown();
    }
  }
}