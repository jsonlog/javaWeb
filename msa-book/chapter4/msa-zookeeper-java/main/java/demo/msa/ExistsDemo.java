package demo.msa;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ExistsDemo {

  private static final String CONNECTION_STRING = "127.0.0.1:2181";
  private static final int SESSION_TIMEOUT = 5000;

  private static CountDownLatch latch = new CountDownLatch(1);

  public static void main(String[] args) throws Exception {
    // 连接 ZooKeeper
    ZooKeeper zk = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
      @Override
      public void process(WatchedEvent event) {
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
          latch.countDown();
        }
      }
    });
    latch.await();
    // 判断节点是否存在
    Stat stat = zk.exists("/", null);
    if (stat != null) {
      System.out.println("node exists");
    } else {
      System.out.println("node does not exist");
    }
  }
}
