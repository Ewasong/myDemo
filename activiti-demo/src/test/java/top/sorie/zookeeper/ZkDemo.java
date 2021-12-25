package top.sorie.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ZkDemo {

    @Test
    public void WatcherTest() throws IOException, InterruptedException {
        class Master implements Watcher {
            ZooKeeper zk;
            String hostPort;

            Master(String hostPort) {
                this.hostPort = hostPort;
            }

            void startZK() throws IOException {
                zk = new ZooKeeper(hostPort, 15000, this);
            }

            public void process(WatchedEvent e) {
                System.out.println(e);
            }
        }
        Master m = new Master("192.168.170.200:2181");
        m.startZK();
        // wait for a bit
        Thread.sleep(60000);
    }
}
