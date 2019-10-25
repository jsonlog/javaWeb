package demo.msa.rabbitmq.rpc.server;

public interface RpcReceiver<I, O> {

  O receive(I message);
}
