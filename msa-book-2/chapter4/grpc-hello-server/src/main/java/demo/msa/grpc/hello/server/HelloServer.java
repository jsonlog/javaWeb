package demo.msa.grpc.hello.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class HelloServer {

  private static final int port = 50000;

  public static void main(String[] args) throws Exception {
    Server server = ServerBuilder
        .forPort(port)
        .addService(new HelloServiceImpl())
        .build()
        .start();
    System.out.println("server started, listening on " + port);
    server.awaitTermination();
  }
}
