package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.RedisStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LettuceLockStockFacade {
    private final RedisLockRepository redisLockRepository;
    private final RedisStockService stockService;

    public void decreaseStock(Long id, Long deceaseQuantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);  //  lock 획득에 실패했다면 100ms 텀을 두고 락획득 재시도
        }

        try {
            stockService.decreaseStock(id, deceaseQuantity);
        } catch (Exception e ) {
            e.printStackTrace();
        } finally {
            redisLockRepository.unLock(id);
        }

    }

}
