package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NamedLockStockService {
    private final StockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(Long id, Long decreaseQuantity) {
        log.info("01. 재고 조회");
        Stock findStock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 재고를 찾을 수 없습니다."));

        log.info("02. 재고 감소");
        findStock.decrease(decreaseQuantity);

        log.info("03. 갱신");
        stockRepository.save(findStock);
    }
}
