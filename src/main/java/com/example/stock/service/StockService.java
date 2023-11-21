package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockService {
    private final StockRepository stockRepository;

    // synchronized : 한개의 스레드만 접근이 가능함
    // 하지만 @Transactional으로 인해 문제 해결 X
    // @Transactional : 우리가 만든 클래스를 Wrapping한 클래스를 새로 만들어 실행
    // decrease 메서드가 완료되었고 실제 DB에 업데이트 되기전에 다른 스레드가 decrease 메서드를 호출할 수 있기 때문에 문제가 해결되지 않음
    // @Transactional가 없다면 문제는 해결
    // 하지만 synchronized는 하나의 프로세스 내에서만 보장, 여러대의 서버가 있다면 동일하게 Race Condition 발생
    @Transactional
    public synchronized void decreaseStock(Long id, Long decreaseQuantity) {
        log.info("01. 재고 조회");
        Stock findStock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 재고를 찾을 수 없습니다."));

        log.info("02. 재고 감소");
        findStock.decrease(decreaseQuantity);

        log.info("03. 갱신");
        stockRepository.save(findStock);
    }
}
