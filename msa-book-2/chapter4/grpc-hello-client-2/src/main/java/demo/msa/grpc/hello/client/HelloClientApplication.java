package demo.msa.grpc.hello.client;

import demo.msa.grpc.client.GrpcClient;
import demo.msa.grpc.hello.api.HelloRequest;
import demo.msa.grpc.hello.api.HelloResponse;
import demo.msa.grpc.hello.api.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "demo.msa.grpc")
public class HelloClientApplication implements CommandLineRunner {

  @Autowired
  private GrpcClient grpcClient;

  @Override
  public void run(String... args) throws Exception {
    ManagedChannel channel = grpcClient.buildChannel();
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

  public static void main(String[] args) {
    SpringApplication.run(HelloClientApplication.class, args).close();
  }
}
