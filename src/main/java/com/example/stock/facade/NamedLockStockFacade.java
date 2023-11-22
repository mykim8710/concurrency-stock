package com.example.stock.facade;

import com.example.stock.repository.LockStockRepository;
import com.example.stock.service.NamedLockStockService;
import com.example.stock.service.OptimisticLockStockService;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NamedLockStockFacade {
    private final LockStockRepository stockRepository;
    private final NamedLockStockService stockService;

    public void decreaseStock(Long id, Long deceaseQuantity) {

        try {
            log.info("01. 락 획득");
            stockRepository.getLock(id.toString());

            log.info("02. 재고감소 로직");
            stockService.decreaseStock(id, deceaseQuantity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("03. 락 해제");
            stockRepository.releaseLock(id.toString());
        }
    }
}
