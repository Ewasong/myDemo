package top.sorie;

//import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class RedissonDemoApplicationTests {
    private static final String redisUrl = "redis://192.168.170.200:6379";
//    @Test
//    void config1() throws IOException {
//        Config config = new Config();
//        config.setTransportMode(TransportMode.NIO);
//
//        config.useSingleServer()
//                //可以用"rediss://"来启用SSL连接
//                .setAddress(redisUrl);
//        RedissonClient client = Redisson.create(config);
//        String jsonFormat = config.toYAML();
////        System.out.println(jsonFormat);
//    }
//
//    @Test
//    void config2Yaml() throws IOException {
//        System.out.println(RedissonDemoApplicationTests.class.getClassLoader().getResource("").getPath());
//        Path path = Paths.get("redisson-conf-local.yml");
//
//        System.out.println(path.toAbsolutePath());
//        Config config = Config.fromYAML(new File("redisson-conf-local.yml"));
//        RedissonClient redisson = Redisson.create(config);
//    }



}
