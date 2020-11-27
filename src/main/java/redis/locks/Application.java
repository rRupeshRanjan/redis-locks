package redis.locks;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        RedissonClient redisson = Redisson.create();
        RLock lock = redisson.getLock("lock");

        while (true) {
            try {
                lock.lock(5, TimeUnit.SECONDS);
                redisson.getKeys()
                        .getKeysByPattern("[1-9]*")
                        .iterator()
                        .forEachRemaining(key -> {
                            RBucket<Object> promotion = redisson.getBucket(key, StringCodec.INSTANCE);
//                        System.out.println(key + ": " + promotion.get());
                            System.out.println(System.currentTimeMillis() + ": " + key);
//                        promotion.delete();
                        });
                Thread.sleep(5000);
                lock.unlock();
            } catch (Exception e) {
                System.out.println("exception");
            }
        }

//        redisson.shutdown();
    }
}

