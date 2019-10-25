package demo.msa;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetChildrenAsync2Demo {

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
    // 列出子节点 (异步)
    zk.getChildren("/", null, new AsyncCallback.Children2Callback() {
      @Override
      public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        for (String node : children) {
          System.out.println(node);
        }
      }
    }, null);
    Thread.sleep(Long.MAX_VALUE);
  }
}
