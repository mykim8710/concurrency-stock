package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockService stockService;

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
    @DisplayName("재고감소 로직에 대한 테스트")
    void 재고감소_테스트() throws Exception {
        // given
        Long id = 1L;
        Long decreaseQuantity = 1L;

        // when
        stockService.decreaseStock(id, decreaseQuantity);

        // then
        Stock findStock = stockRepository.findById(id).get();
        Assertions.assertThat(findStock.getQuantity()).isEqualTo(99);
    }

}