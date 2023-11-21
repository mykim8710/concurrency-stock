package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OptimisticLockStockFacade {
    private final OptimisticLockStockService stockService;

    public void decreaseStock(Long id, Long deceaseQuantity) throws InterruptedException {
        // update에 실패했을때, 재시도를 해야함
        while (true) {
            try {
                stockService.decreaseStock(id, deceaseQuantity);

                break;
            } catch (Exception e) {
                // 수량감소에 실패한다면 50ms 후에 재시도
                Thread.sleep(50);
            }
        }
    }

}
