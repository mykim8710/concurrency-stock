package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.facade.NamedLockStockFacade;
import com.example.stock.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
class NamedLockStockFacadeTest {
    @Autowired
    private NamedLockStockFacade namedLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void saveStock() {
        stockRepository.saveAndFlush(Stock.builder()
                                                    .itemId(1L)
                                                    .quantity(100L)
                                                    .build());
    }

    @Test
    @DisplayName("동시에 100개의 요청이 들어올때 재고감소 테스트 : Named Lock 활용")
    void 재고감소_동시100개요청_테스트() throws Exception {
        // given
        int threadCount = 100;

        // ExecutorService : 비동기로 실행하는 작업을 단순화하여 사용할 수 있게 도와주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // CountDownLatch : 다른 스레드에서 수행 중인 작업이 완료될 때까지 대기할수 있도록 도와주는 클래스
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    namedLockStockFacade.decreaseStock(1L, 1L);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // then
        Stock findStock = stockRepository.findById(1L).get();
        Assertions.assertThat(findStock.getQuantity()).isEqualTo(0);
    }

}