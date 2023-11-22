package com.example.stock.facade;

import com.example.stock.service.RedisStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;
    private final RedisStockService stockService;

    public void decreaseStock(Long id, Long deceaseQuantity) throws InterruptedException {
        RLock lock = redissonClient.getLock(id.toString());

        try {
            // 몇 초동안 락 획득을 시도할 것인지, 몇 초동안 점유할 것인지 설정 후 락 획득
            boolean tryLock = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if(!tryLock) {
                log.info("lock 획득 실패");
                return;
            }

            stockService.decreaseStock(id, deceaseQuantity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 락해제
            lock.unlock();
        }
    }
}
