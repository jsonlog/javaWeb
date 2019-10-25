package demo.msa.grpc.hello.server;

import demo.msa.grpc.hello.api.HelloRequest;
import demo.msa.grpc.hello.api.HelloResponse;
import demo.msa.grpc.hello.api.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

  @Override
  public void say(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    HelloResponse response = null;
    try {
      response = HelloResponse
          .newBuilder()
          .setMessage("hello " + request.getName())
          .build();
    } catch (Exception e) {
      responseObserver.onError(e);
    } finally {
      responseObserver.onNext(response);
    }
    responseObserver.onCompleted();
  }
}