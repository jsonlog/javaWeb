package demo.msa;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class CreateAsyncDemo {

  private static final String CONNECTION_STRING = "127.0.0.1:2181";
  private static final int SESSION_TIMEOUT = 5000;

  private static CountDownLatch latch = new CountDownLatch(1);

  public static void main(String[] args) throws Exception {
    // 连接 ZooKeeper
    ZooKeeper zk = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
      @Override
      public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
          latch.countDown();
        }
      }
    });
    latch.await();
    // 创建节点
    zk.create("/foo", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
      @Override
      public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println(name);
      }
    }, null);
    Thread.sleep(Long.MAX_VALUE);
  }
}
