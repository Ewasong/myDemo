package top.sorie;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;

@RestController
public class TestController {

    @Autowired
    RedissonClient redissonClient;
    @RequestMapping("/test")
    public void test() {
        Lock lock = redissonClient.getLock("test");
        lock.lock();
        System.out.println("xxx");
        lock.unlock();
    }
}
